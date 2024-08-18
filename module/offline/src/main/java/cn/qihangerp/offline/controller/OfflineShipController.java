package cn.qihangerp.offline.controller;

import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.mq.EnumShopType;
import cn.qihangerp.mq.MqMessage;
import cn.qihangerp.mq.MqType;
import cn.qihangerp.mq.MqUtils;
import cn.qihangerp.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import cn.qihangerp.offline.domain.OfflineOrder;
import cn.qihangerp.offline.domain.bo.OrderShipBo;
import cn.qihangerp.offline.domain.bo.OrderShipSendBo;
import cn.qihangerp.offline.service.OfflineOrderService;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/offline/ship")
public class OfflineShipController extends BaseController {
    private final OfflineOrderService orderService;
    private final MqUtils mqUtils;
    /**
     * 手动填写物流单号（）
     * @param bo
     * @return
     */
    @PostMapping("/order_logistics")
    public AjaxResult add(@RequestBody OrderShipBo bo)
    {
        int result = orderService.orderLogistics(bo, SecurityUtils.getUsername());
        if(result==-1) return AjaxResult.error("参数错误：orderNum不能为空");
        else if(result==-2) return AjaxResult.error("参数错误：快递信息不能为空");
        else if(result==-3) return AjaxResult.error("参数错误：找不到订单");
        else if(result==-4) return AjaxResult.error("数据错误：订单状态不是待发货的状态");
        else if(result==-5) return AjaxResult.error("数据错误：订单正在售后中");

        return toAjax(1);
    }

    /**
     * 批量发货
     * @param bo
     * @return
     */
    @PostMapping("/order_batch_send")
    @ResponseBody
    public AjaxResult orderSend(@RequestBody OrderShipSendBo bo) {
        // TODO:需要优化消息格式
        if(bo!=null && bo.getOrderNums()!=null) {
            for(String sn: bo.getOrderNums()) {
                List<OfflineOrder> orders = orderService.list(new LambdaQueryWrapper<OfflineOrder>().eq(OfflineOrder::getOrderNum,sn));
                if(orders!=null&& orders.size()>0) {
                    if (orders.get(0).getOrderStatus() == 1 && orders.get(0).getRefundStatus() == 1 && StringUtils.hasText(orders.get(0).getShippingNumber())) {
                        OfflineOrder update = new OfflineOrder();
                        update.setId(orders.get(0).getId());
                        update.setOrderStatus(2);
                        update.setUpdateTime(new Date());
                        update.setUpdateBy("批量发货");
                        update.setShippingTime(new Date());
                        update.setShippingMan(SecurityUtils.getUsername());
                        orderService.updateById(update);
                        // 通知已发货
                        mqUtils.sendApiMessage(MqMessage.build(EnumShopType.OFFLINE, MqType.SHIP_SEND_MESSAGE, sn, orders.get(0).getShippingCompany(), orders.get(0).getShippingNumber()));
                    }
                }
            }
        }
        return success();
    }
}

