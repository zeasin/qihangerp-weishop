package cn.qihangerp.open.wei.service;



import cn.qihangerp.open.wei.bo.OrderDetailBo;
import cn.qihangerp.open.wei.bo.OrderListBo;
import cn.qihangerp.open.wei.vo.OrderDetailVo;
import cn.qihangerp.open.wei.vo.OrderListVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface OrderApiService {
    @PostExchange("/channels/ec/order/list/get")
    OrderListVo getOrderList(@RequestParam(name = "access_token")  String access_token, @RequestBody OrderListBo bo);
    @PostExchange("/channels/ec/order/get")
    OrderDetailVo getOrderDetail(@RequestParam(name = "access_token")  String access_token, @RequestBody OrderDetailBo bo);
}
