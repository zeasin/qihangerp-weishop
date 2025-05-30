package cn.qihangerp.api.controller;

import cn.qihangerp.api.common.*;
import cn.qihangerp.api.controller.shop.PddApiCommon;
import cn.qihangerp.api.domain.ErpLogisticsCompany;
import cn.qihangerp.api.domain.ErpOrderShipList;
import cn.qihangerp.api.domain.ErpShipment;
import cn.qihangerp.api.domain.ErpShopPullLogs;
import cn.qihangerp.api.mapper.ErpLogisticsCompanyMapper;
import cn.qihangerp.api.request.SupplierAgentShipmentRequest;
import cn.qihangerp.api.service.ErpLogisticsCompanyService;
import cn.qihangerp.api.service.ErpOrderShipListService;
import cn.qihangerp.api.service.ErpShipmentItemService;
import cn.qihangerp.api.service.ErpShipmentService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.pdd.PddLogisticsApiHelper;
import cn.qihangerp.open.pdd.model.WaybillCodeModule;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/orderShip")
public class OrderShipSupplierController extends BaseController {
    private final ErpShipmentService shippingService;
    private final ErpOrderShipListService orderShipListService;
    private final ErpLogisticsCompanyService erpLogisticsCompanyService;
    private final PddApiCommon pddApiCommon;

    /**
     * 备货中-供应商代发
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/supplier_shipment_list")
    public TableDataInfo supplierShipList(ErpOrderShipList bo, PageQuery pageQuery)
    {
        bo.setShipper(1);
        bo.setTenantId(getUserId());

        PageResult<ErpOrderShipList> erpShipmentPageResult = orderShipListService.queryPageList(bo, pageQuery);
        return getDataTable(erpShipmentPageResult);
    }

    /**
     * 供应商发货 确认下单
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/supplierShipmentConfirm/{id}")
    public AjaxResult supplierAgentShipment(@PathVariable Long id) throws IOException {
        ErpOrderShipList orderShipList = orderShipListService.getById(id);
        if(orderShipList==null) return AjaxResult.error("没有找到数据");
        else if (orderShipList.getStatus()!=0) {
            return AjaxResult.error("备货状态不对，不允许操作");
        }
        ErpOrderShipList update = new ErpOrderShipList();
        update.setId(id);
        update.setStatus(1);//状态 供应商发货：0待下单1已下单3已发货
        update.setUpdateBy(getUsername());
        update.setUpdateTime(new Date());
        orderShipListService.updateById(update);
        return AjaxResult.success();
    }

    /**
     * 供应商发货(供应商代发货)
     * @param shipping
     * @return
     */
    @PostMapping("/supplierAgentShipment")
    public AjaxResult supplierAgentShipment(@RequestBody SupplierAgentShipmentRequest shipping) throws IOException {

        ErpOrderShipList orderShipList = orderShipListService.getById(shipping.id());
        if(orderShipList==null) return AjaxResult.error("没有数据");
        ErpLogisticsCompany erpLogisticsCompany = erpLogisticsCompanyService.getById(shipping.shipCompanyId());
        if(erpLogisticsCompany==null) return AjaxResult.error("快递公司选择错误");

//        shipping.setShipType(1);
        var result = orderShipListService.supplierAgentShipment(shipping);
        if(result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
            // 调用接口api
            var checkResult = pddApiCommon.checkBefore(orderShipList.getShopId());
            if (checkResult.getCode() != ResultVoEnum.SUCCESS.getIndex()) {
                return AjaxResult.error(500, checkResult.getMsg());
            }
            String accessToken = checkResult.getData().getAccessToken();
            String appKey = checkResult.getData().getAppKey();
            String appSecret = checkResult.getData().getAppSecret();

            log.info("=====发货参数======logisticsId:{},orderNum:{},shipNo:{}",erpLogisticsCompany.getLogisticsId(),orderShipList.getOrderNum(),shipping.shipNo());
            ApiResultVo apiResultVo = PddLogisticsApiHelper.onlineSend(appKey, appSecret, accessToken, erpLogisticsCompany.getLogisticsId(), orderShipList.getOrderNum(), shipping.shipNo());
            log.info("=====发货结果======{}", JSONObject.toJSONString(apiResultVo));
            return AjaxResult.success();
        }else{
            return AjaxResult.error(result.getCode(),result.getMsg());
        }
    }

}
