package cn.qihangerp.open.wei;

import cn.qihangerp.open.wei.bo.ewaybill.AccountGetApiBo;
import cn.qihangerp.open.wei.bo.ewaybill.DeliveryGetApiBo;
import cn.qihangerp.open.wei.common.ApiResultVo;
import cn.qihangerp.open.wei.common.ApiResultVoEnum;
import cn.qihangerp.open.wei.service.EwaybillApiService;
import cn.qihangerp.open.wei.vo.Token;
import cn.qihangerp.open.wei.vo.ewaybill.AccountVo;
import cn.qihangerp.open.wei.vo.ewaybill.DeliveryVo;
import org.springframework.util.StringUtils;

public class EwaybillAccountApiHelper {

    public static ApiResultVo<DeliveryVo> getDeliveryList(String appId, String appSecret, String accessToken)  {
        String serverUrl = "https://api.weixin.qq.com";
        ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
        if(token.getCode() != ApiResultVoEnum.SUCCESS.getIndex()) return ApiResultVo.error(ApiResultVoEnum.ApiException,"获取Token失败");
        accessToken = token.getData().getAccess_token();


        EwaybillApiService remoting = RemoteUtil.Remoting(serverUrl, EwaybillApiService.class);

        DeliveryGetApiBo apiBo = new DeliveryGetApiBo();

        apiBo.setStatus(3);
        var res = remoting.getDeliveryList(accessToken, apiBo);
        if(res.getErrcode() == 0){
            // 数据
            return ApiResultVo.success(res.getList().size(),res.getList());
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,res.getErrmsg());
        }


    }
    public static ApiResultVo<AccountVo> getAccountList(String appId, String appSecret, String accessToken)  {
        String serverUrl = "https://api.weixin.qq.com";
        if(!StringUtils.hasText(accessToken)) {
            ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
            if (token.getCode() != ApiResultVoEnum.SUCCESS.getIndex())
                return ApiResultVo.error(ApiResultVoEnum.ApiException, "获取Token失败");
            accessToken = token.getData().getAccess_token();
        }

        EwaybillApiService remoting = RemoteUtil.Remoting(serverUrl, EwaybillApiService.class);

        AccountGetApiBo apiBo = new AccountGetApiBo();

        apiBo.setNeed_balance(true);
        apiBo.setLimit(10);
        var res = remoting.getEwaybillAccount(accessToken, apiBo);
        if(res.getErrcode() == 0){
            // 数据
            return ApiResultVo.success(res.getAccount_list().size(),res.getAccount_list());
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,res.getErrmsg());
        }
    }
}
