package cn.qihangerp.open.wei.bo.ewaybill;

import lombok.Data;

import java.util.List;

@Data
public class WaybillRequest {
    //https://developers.weixin.qq.com/doc/channels/API/ewaybill/create_order.html
    private String ewaybill_order_id;
    private String delivery_id;
    private String site_code;
    private String ewaybill_acct_id;
    private SenderAddressBo sender;
    private SenderAddressBo receiver;
    private List<EcOrderInfo> ec_order_list;
    private String remark;
    private String shop_id;
    private SenderAddressBo return_address;
    private String template_id;//如果需要获取打印报文，则填该字段。回包返回print_info。 如无需使用后台模板，可直接传递template_type做为默认模板， 如‘single’
    private Integer order_type;
//    private List<>order_vas_list
//    private  EWaybillOrderInfoExt ext_info
}
