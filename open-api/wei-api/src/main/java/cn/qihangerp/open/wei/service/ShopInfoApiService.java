package cn.qihangerp.open.wei.service;


import cn.qihangerp.open.wei.vo.ShopApiResultVo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ShopInfoApiService {
    @GetExchange("/channels/ec/basics/info/get")
    ShopApiResultVo getShopInfo(@RequestParam(name = "access_token")  String access_token);
}
