package cn.qihangerp.open.wei.service;



import cn.qihangerp.open.wei.bo.RefundDetailBo;
import cn.qihangerp.open.wei.bo.RefundListBo;
import cn.qihangerp.open.wei.vo.RefundDetailVo;
import cn.qihangerp.open.wei.vo.RefundListVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface RefundApiService {
    @PostExchange("/channels/ec/aftersale/getaftersalelist")
    RefundListVo getRefundList(@RequestParam(name = "access_token")  String access_token, @RequestBody RefundListBo bo);
    @PostExchange("/channels/ec/aftersale/getaftersaleorder")
    RefundDetailVo getRefundDetail(@RequestParam(name = "access_token")  String access_token, @RequestBody RefundDetailBo bo);
}
