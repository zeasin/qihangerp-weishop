package cn.qihangerp.open.wei;


import cn.qihangerp.open.wei.bo.RefundDetailBo;
import cn.qihangerp.open.wei.bo.RefundListBo;
import cn.qihangerp.open.wei.common.ApiResultVo;
import cn.qihangerp.open.wei.common.ApiResultVoEnum;
import cn.qihangerp.open.wei.model.AfterSaleOrder;
import cn.qihangerp.open.wei.service.RefundApiService;
import cn.qihangerp.open.wei.vo.RefundDetailVo;
import cn.qihangerp.open.wei.vo.RefundListVo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


public class RefundApiHelper {


    public  static ApiResultVo<AfterSaleOrder> pullRefundList(LocalDateTime startTime, LocalDateTime  endTime, String accessToken) {
        String serverUrl = "https://api.weixin.qq.com";
//        ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
//        if(token.getCode() != ApiResultVoEnum.SUCCESS.getIndex()) return ApiResultVo.error(ApiResultVoEnum.ApiException,"获取Token失败");
//        String accessToken = token.getData().getAccess_token();

        List<AfterSaleOrder> lists = new ArrayList<>();

        RefundApiService remoting = RemoteUtil.Remoting(serverUrl, RefundApiService.class);
        RefundListBo apiBo = new RefundListBo();
        Long begin = startTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000;
        Long end = endTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000;
        apiBo.setBegin_create_time(begin.intValue());
        apiBo.setEnd_create_time(end.intValue());


        RefundListVo result = remoting.getRefundList(accessToken, apiBo);


        if(result.getErrcode() == 0) {
            // 拉取到了数据 拉取详情
            if(result.getAfter_sale_order_id_list()!=null&&result.getAfter_sale_order_id_list().length>0) {
                for (var refundId : result.getAfter_sale_order_id_list()) {
                    RefundDetailBo bo = new RefundDetailBo();
                    bo.setAfter_sale_order_id(refundId.toString());
                    RefundDetailVo refundDetail = remoting.getRefundDetail(accessToken, bo);
                    if(refundDetail.getErrcode() == 0){
                        lists.add(refundDetail.getAfter_sale_order());
                    }
                }

                Boolean isHas_more = result.isHas_more();
                String next_key = result.getNext_key();
                while (isHas_more){

                    apiBo.setNext_key(next_key);
                    RefundListVo resultNext = remoting.getRefundList(accessToken, apiBo);
                    if(resultNext.getErrcode() == 0) {
                        // 拉取到了数据 拉取详情
                        if(resultNext.getAfter_sale_order_id_list()!=null&&resultNext.getAfter_sale_order_id_list().length>0) {
                            for (var refundId : resultNext.getAfter_sale_order_id_list()) {
                                RefundDetailBo bo = new RefundDetailBo();
                                bo.setAfter_sale_order_id(refundId.toString());
                                RefundDetailVo refundDetail = remoting.getRefundDetail(accessToken, bo);
                                if(refundDetail.getErrcode() == 0){
                                    lists.add(refundDetail.getAfter_sale_order());
                                }
                            }
                        }
                        next_key = resultNext.getNext_key();
                        isHas_more = resultNext.isHas_more();
                    }
                }

            }
            return ApiResultVo.success(lists.size(), lists);
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,result.getErrmsg());
        }

    }
    public  static ApiResultVo<AfterSaleOrder> pullRefundDetail(Long refundId, String accessToken) {
        String serverUrl = "https://api.weixin.qq.com";
//        ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
//        if(token.getCode() != ApiResultVoEnum.SUCCESS.getIndex()) return ApiResultVo.error(ApiResultVoEnum.ApiException,"获取Token失败");
//        String accessToken = token.getData().getAccess_token();


        RefundApiService remoting = RemoteUtil.Remoting(serverUrl, RefundApiService.class);

        RefundDetailBo bo = new RefundDetailBo();
        bo.setAfter_sale_order_id(refundId.toString());
        RefundDetailVo result = remoting.getRefundDetail(accessToken, bo);

        if(result.getErrcode() == 0) {
            // 拉取到了数据 拉取详情
            return ApiResultVo.success(result.getAfter_sale_order());
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,result.getErrmsg());
        }

    }
}
