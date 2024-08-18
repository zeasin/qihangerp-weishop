package cn.qihangerp.open.wei.service;


import cn.qihangerp.open.wei.vo.Token;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface TokenApiService {

    @GetExchange("/cgi-bin/token")
    Token getToken(@RequestParam(name = "grant_type") String grant_type, @RequestParam(name = "appid") String appid, @RequestParam(name = "secret") String secret);
}
