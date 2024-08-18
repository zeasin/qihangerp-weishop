package cn.qihangerp.open.wei.service;


import cn.qihangerp.open.wei.bo.GoodsDetailApiBo;
import cn.qihangerp.open.wei.bo.GoodsListApiBo;
import cn.qihangerp.open.wei.vo.GoodsDetailVo;
import cn.qihangerp.open.wei.vo.GoodsListVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface GoodsApiService {
    @PostExchange("/channels/ec/product/list/get")
    GoodsListVo getGoodsList(@RequestParam(name = "access_token") String access_token, @RequestBody GoodsListApiBo bo);
    @PostExchange("/channels/ec/product/get")
    GoodsDetailVo getGoodsDetail(@RequestParam(name = "access_token") String access_token, @RequestBody GoodsDetailApiBo bo);
}
