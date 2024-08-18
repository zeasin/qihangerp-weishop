package cn.qihangerp.open.wei;

import cn.qihangerp.open.wei.bo.GoodsDetailApiBo;
import cn.qihangerp.open.wei.bo.GoodsListApiBo;
import cn.qihangerp.open.wei.common.ApiResultVo;
import cn.qihangerp.open.wei.common.ApiResultVoEnum;
import cn.qihangerp.open.wei.model.Product;
import cn.qihangerp.open.wei.service.GoodsApiService;
import cn.qihangerp.open.wei.vo.GoodsDetailVo;
import cn.qihangerp.open.wei.vo.GoodsListVo;

import java.util.ArrayList;
import java.util.List;

public class GoodsApiHelper {
    /**
     * 获取商品列表
     * @param accessToken
     * @return
     */

    public static ApiResultVo<Product> pullGoodsList(String accessToken)  {
        String serverUrl = "https://api.weixin.qq.com";
//        ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
//        if(token.getCode() != ApiResultVoEnum.SUCCESS.getIndex()) return ApiResultVo.error(ApiResultVoEnum.ApiException,"获取Token失败");
//        String accessToken = token.getData().getAccess_token();

        List<Product> productList = new ArrayList<>();
        GoodsApiService remoting = RemoteUtil.Remoting(serverUrl, GoodsApiService.class);
        Integer pageSize = 30;
//        int getTotal =0;//获取到了总条数
        GoodsListApiBo apiBo = new GoodsListApiBo();
        apiBo.setPage_size(pageSize);
        apiBo.setStatus(5);
        GoodsListVo res = remoting.getGoodsList(accessToken, apiBo);
        if(res.getErrcode() == 0){
            // 第一页数据
            if(res.getProduct_ids()!=null&&res.getProduct_ids().length>0){
                for (var productId:res.getProduct_ids()) {
                    GoodsDetailApiBo apiBo1 = new GoodsDetailApiBo();
                    apiBo1.setProduct_id(productId.toString());
                    GoodsDetailVo goodsDetail = remoting.getGoodsDetail(accessToken, apiBo1);
                    if(goodsDetail.getErrcode()==0){
                        productList.add(goodsDetail.getProduct());
                    }
                }
            }

            // 如果还有分页
            if(res.getTotal_num()>pageSize){
                while (productList.size()<res.getTotal_num()){
                    String next = res.getNext_key();
                    apiBo.setNext_key(next);
                    GoodsListVo res1 = remoting.getGoodsList(accessToken, apiBo);
                    if(res1.getErrcode() == 0){
                        if(res.getProduct_ids()!=null&&res.getProduct_ids().length>0){
                            for (var productId:res.getProduct_ids()) {
                                GoodsDetailApiBo apiBo1 = new GoodsDetailApiBo();
                                apiBo1.setProduct_id(productId.toString());
                                GoodsDetailVo goodsDetail = remoting.getGoodsDetail(accessToken, apiBo1);
                                if(goodsDetail.getErrcode()==0){
                                    productList.add(goodsDetail.getProduct());
                                }
                            }
                        }
                    }
                }
            }
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,res.getErrmsg());
        }

        return ApiResultVo.success(productList.size(), productList);
    }

}
