package cn.qihangerp.open.wei;

import cn.qihangerp.open.wei.bo.CreateTimeRangeBo;
import cn.qihangerp.open.wei.bo.OrderDetailBo;
import cn.qihangerp.open.wei.bo.OrderListBo;
import cn.qihangerp.open.wei.common.ApiResultVo;
import cn.qihangerp.open.wei.common.ApiResultVoEnum;
import cn.qihangerp.open.wei.model.Order;
import cn.qihangerp.open.wei.service.OrderApiService;
import cn.qihangerp.open.wei.vo.OrderDetailVo;
import cn.qihangerp.open.wei.vo.OrderListVo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


public class OrderApiHelper {

    /**
     * 拉取订单
     * @param
     * @return
     * @throws
     */
    public static ApiResultVo<Order> pullOrderList(LocalDateTime startTime, LocalDateTime  endTime, String accessToken ) {
        String serverUrl = "https://api.weixin.qq.com";
//        ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
//        if(token.getCode() != ApiResultVoEnum.SUCCESS.getIndex()) return ApiResultVo.error(ApiResultVoEnum.ApiException,"获取Token失败");
//        String accessToken = token.getData().getAccess_token();

        List<Order> lists = new ArrayList<>();

        OrderApiService remoting = RemoteUtil.Remoting(serverUrl, OrderApiService.class);
        OrderListBo apiBo = new OrderListBo();
        apiBo.setPage_size(100);
        CreateTimeRangeBo tbo= new CreateTimeRangeBo();
        tbo.setStart_time(startTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli()/1000);
        tbo.setEnd_time(endTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli()/1000);
        apiBo.setCreate_time_range(tbo);

        OrderListVo result = remoting.getOrderList(accessToken, apiBo);

        if(result.getErrcode() == 0) {
            // 拉取到了数据 拉取详情
            if(result.getOrder_id_list()!=null&&result.getOrder_id_list().length>0) {
                for (var orderId : result.getOrder_id_list()) {
                    OrderDetailBo bo = new OrderDetailBo();
                    bo.setOrder_id(orderId.toString());
                    OrderDetailVo orderDetail = remoting.getOrderDetail(accessToken, bo);
                    if(orderDetail.getErrcode() == 0) {
                        lists.add(orderDetail.getOrder());
                    }
                }
            }
            Boolean isHas_more = result.isHas_more();
            String next_key = result.getNext_key();
            while (isHas_more){

                apiBo.setNext_key(next_key);
                OrderListVo resultNext = remoting.getOrderList(accessToken, apiBo);
                if(resultNext.getErrcode() == 0) {
                    // 拉取到了数据 拉取详情
                    if(resultNext.getOrder_id_list()!=null&&resultNext.getOrder_id_list().length>0) {
                        for (var orderId : resultNext.getOrder_id_list()) {
                            OrderDetailBo bo = new OrderDetailBo();
                            bo.setOrder_id(orderId.toString());
                            OrderDetailVo orderDetail = remoting.getOrderDetail(accessToken, bo);
                            if(orderDetail.getErrcode() == 0) {
                                lists.add(orderDetail.getOrder());
                            }
                        }
                    }
                    next_key = resultNext.getNext_key();
                    isHas_more = resultNext.isHas_more();
                }
            }
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,result.getErrmsg());
        }
        return ApiResultVo.success(lists.size(), lists);
    }
    public static ApiResultVo<Order> pullOrderDetail(Long orderId, String accessToken ) {
        String serverUrl = "https://api.weixin.qq.com";
//        ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
//        if (token.getCode() != ApiResultVoEnum.SUCCESS.getIndex())
//            return ApiResultVo.error(ApiResultVoEnum.ApiException, "获取Token失败");
//        String accessToken = token.getData().getAccess_token();


        OrderApiService remoting = RemoteUtil.Remoting(serverUrl, OrderApiService.class);
        OrderDetailBo bo = new OrderDetailBo();
        bo.setOrder_id(orderId.toString());
        OrderDetailVo result = remoting.getOrderDetail(accessToken, bo);

        if (result.getErrcode() == 0) {
            // 拉取到了数据 拉取详情
            if (result.getOrder() != null) {
              return ApiResultVo.success(result.getOrder());
            }
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,result.getErrmsg());
        }
        return ApiResultVo.error(ApiResultVoEnum.ApiException);
    }

}


