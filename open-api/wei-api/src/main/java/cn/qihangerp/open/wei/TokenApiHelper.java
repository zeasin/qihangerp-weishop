package cn.qihangerp.open.wei;

import cn.qihangerp.open.wei.common.ApiResultVo;
import cn.qihangerp.open.wei.common.ApiResultVoEnum;
import cn.qihangerp.open.wei.service.ShopInfoApiService;
import cn.qihangerp.open.wei.service.TokenApiService;
import cn.qihangerp.open.wei.vo.ShopApiResultVo;
import cn.qihangerp.open.wei.vo.Token;

public class TokenApiHelper {
    /**
     * 获取新token
     * @param appId
     * @param secret
     * @return
     */
    public static ApiResultVo<Token> getToken(String appId, String secret){
        String serverUrl = "https://api.weixin.qq.com";
        TokenApiService remoting1 = RemoteUtil.Remoting(serverUrl, TokenApiService.class);


        Token token = remoting1.getToken("client_credential",appId,secret);
        if(token.getErrcode()==null||token.getErrcode()==0) {
            return ApiResultVo.success(token);
        }else{
            return ApiResultVo.error(ApiResultVoEnum.ApiException,token.getErrmsg());
        }
    }

    /**
     * 检查token是否过期并返回新token
     * @param appid
     * @param secret
     * @param accessToken
     * @return
     */
    public static ApiResultVo<Token> checkToken(String appid, String secret, String accessToken){
        String serverUrl = "https://api.weixin.qq.com";
        // 调用 店铺基本信息接口 验证Token
        ShopInfoApiService remoting = RemoteUtil.Remoting(serverUrl, ShopInfoApiService.class);
        ShopApiResultVo shopInfo = remoting.getShopInfo(accessToken);
        if(shopInfo.getErrcode()==null||shopInfo.getErrcode()==0) {
            Token token = new Token();
            token.setAccess_token(accessToken);
            token.setErrcode(0);
            return ApiResultVo.success(token);
        }
        else {
            // Token过期 (shopInfo.getErrcode() == 42001)
//            TokenApiService remoting1 = RemoteUtil.Remoting(serverUrl, TokenApiService.class);
//            Token token = remoting1.getToken("client_credential", appid, secret);
//            if (token.getErrcode() == null || token.getErrcode() == 0) {
//                return ApiResultVo.success(token);
//            } else {
//                return ApiResultVo.error(token.getErrcode(), token.getErrmsg());
//            }
            return ApiResultVo.error(shopInfo.getErrcode(), shopInfo.getErrmsg());
        }
    }
}

