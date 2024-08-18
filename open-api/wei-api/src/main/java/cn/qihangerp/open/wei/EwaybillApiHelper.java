package cn.qihangerp.open.wei;

import cn.qihangerp.open.wei.bo.ewaybill.WaybillRequest;
import cn.qihangerp.open.wei.common.ApiResultVo;
import cn.qihangerp.open.wei.common.ApiResultVoEnum;
import cn.qihangerp.open.wei.service.EwaybillApiService;
import cn.qihangerp.open.wei.vo.Token;
import cn.qihangerp.open.wei.vo.ewaybill.EwaybillOrderCreateVo;
import org.springframework.util.StringUtils;

public class EwaybillApiHelper {

    public static ApiResultVo<EwaybillOrderCreateVo> getWaybillCode(String appId, String appSecret, String accessToken,WaybillRequest request){
        if(!StringUtils.hasText(accessToken)) {
            ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
            if (token.getCode() != ApiResultVoEnum.SUCCESS.getIndex())
                return ApiResultVo.error(ApiResultVoEnum.ApiException, "获取Token失败");
            accessToken = token.getData().getAccess_token();
        }
        ApiResultVo<String> preCreateResult = ewaybillOrderPreCreate(appId, appSecret, accessToken, request);
        if (preCreateResult.getCode() != ApiResultVoEnum.SUCCESS.getIndex()){
            return ApiResultVo.error(preCreateResult.getCode(),preCreateResult.getMsg());
        }
        request.setEwaybill_order_id(preCreateResult.getData());
        request.setTemplate_id("single");
        return ewaybillOrderCreate(appId, appSecret, accessToken, request);
    }


    public static ApiResultVo<String> ewaybillOrderPreCreate(String appId, String appSecret, String accessToken,WaybillRequest request)  {
        String serverUrl = "https://api.weixin.qq.com";
        if(!StringUtils.hasText(accessToken)) {
            ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
            if (token.getCode() != ApiResultVoEnum.SUCCESS.getIndex())
                return ApiResultVo.error(ApiResultVoEnum.ApiException, "获取Token失败");
            accessToken = token.getData().getAccess_token();
        }

        EwaybillApiService remoting = RemoteUtil.Remoting(serverUrl, EwaybillApiService.class);

        var res = remoting.ewaybillOrderPreCreate(accessToken, request);
        if(res.getErrcode() == 0){
            // 数据
            return ApiResultVo.success(res.getEwaybill_order_id());
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,res.getErrmsg());
        }
        //ewaybill_order_id -> 3486612324495114245
        //ewaybill_order_id -> 3486613093898141699
    }


    public static ApiResultVo<EwaybillOrderCreateVo> ewaybillOrderCreate(String appId, String appSecret, String accessToken,WaybillRequest request)  {
        String serverUrl = "https://api.weixin.qq.com";
        if(!StringUtils.hasText(accessToken)) {
            ApiResultVo<Token> token = TokenApiHelper.getToken(appId, appSecret);
            if (token.getCode() != ApiResultVoEnum.SUCCESS.getIndex())
                return ApiResultVo.error(ApiResultVoEnum.ApiException, "获取Token失败");
            accessToken = token.getData().getAccess_token();
        }

        EwaybillApiService remoting = RemoteUtil.Remoting(serverUrl, EwaybillApiService.class);

        var res = remoting.ewaybillOrderCreate(accessToken, request);
        if(res.getErrcode() == 0){
            // 数据
            return ApiResultVo.success(res);
        }else {
            return ApiResultVo.error(ApiResultVoEnum.ApiException,res.getErrmsg());
        }
    }


}
