package cn.qihangerp.offline.controller;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.mq.EnumShopType;
import cn.qihangerp.mq.MqMessage;
import cn.qihangerp.mq.MqType;
import cn.qihangerp.mq.MqUtils;
import cn.qihangerp.offline.domain.bo.OfflineOrderPushBo;
import cn.qihangerp.offline.domain.bo.OrderCreateBo;
import cn.qihangerp.offline.domain.bo.OrderSearchBo;
import cn.qihangerp.offline.service.OfflineOrderService;
import cn.qihangerp.security.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/offline/order")
public class OfflineOrderController extends BaseController {
    private final OfflineOrderService orderService;
    private final MqUtils mqUtils;
    /**
     * 查询店铺订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OrderSearchBo bo, PageQuery pageQuery)
    {
        var pageList = orderService.queryPageList(bo,pageQuery);
        return getDataTable(pageList);
    }


    /**
     * 获取店铺订单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderService.queryDetailById(id));
    }

//    @PostMapping("/pushErp/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        for (Long id : ids) {
//            OOrder oOrder = orderService.getById(id);
//            if (oOrder != null) {
//                oOrder.setItemList(orderItemService.getOrderItemListByOrderId(id));
//                ResultVo resultVo = erpPushHelper.pushOrderSingle(oOrder);
//                OOrder pushUpdate = new OOrder();
//                if (oOrder.getOrderStatus() == 1 || oOrder.getOrderStatus() == 2 || oOrder.getOrderStatus() == 3) {
//                    // 待发货、已发货、已完成 订单推送
//                    pushUpdate.setErpPushStatus(resultVo.getCode() == 0 ? 200 : resultVo.getCode());
//
//                } else if (oOrder.getOrderStatus() == 11) {
//                    pushUpdate.setErpPushStatus(resultVo.getCode() == 0 ? 100 : resultVo.getCode());//推送状态200 订单推送成功 100 取消订单推送成功
//                }
//                pushUpdate.setErpPushResult(resultVo.getMsg());
//                pushUpdate.setErpPushTime(new Date());
//                pushUpdate.setUpdateBy("手动推送到ERP");
//                pushUpdate.setUpdateTime(new Date());
//                pushUpdate.setId(id.toString());
//                orderService.updateById(pushUpdate);
//            }
//        }
//
//        return success();
//    }


    @PostMapping("/create")
    public AjaxResult add(@RequestBody OrderCreateBo order)
    {
        if(order.getGoodsAmount()==null)return new AjaxResult(1503,"请填写商品价格！");

        Long result = orderService.insertOfflineOrder(order, SecurityUtils.getUsername());
        if(result>0) {
            logger.info("渠道訂單添加成功");
            mqUtils.sendApiMessage(MqMessage.build(EnumShopType.OFFLINE, MqType.ORDER_MESSAGE, result.toString()));
        }
        else if(result == -1) return new AjaxResult(501,"订单号已存在！");
        else if(result == -2) return new AjaxResult(502,"请添加订单商品！");
        else if(result == -3) return new AjaxResult(503,"请完善订单商品明细！");
        else if(result == -4) return new AjaxResult(504,"请选择店铺！");
        return toAjax(1);
    }

    /**
     * 手动推送到系统
     * @param bo
     * @return
     */
    @PostMapping("/push_oms")
    @ResponseBody
    public AjaxResult pushOms(@RequestBody OfflineOrderPushBo bo) {
        // TODO:需要优化消息格式
        if(bo!=null && bo.getIds()!=null) {
            for(String id: bo.getIds()) {
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.OFFLINE, MqType.ORDER_MESSAGE, id));
            }
        }
        return success();
    }
}

