package cn.qihangerp.open.wei.bo.ewaybill;

import lombok.Data;

import java.util.List;

@Data
public class EcOrderInfo {
    private Long ec_order_id;
    private String ewaybill_order_code;
    private String ewaybill_order_appid;
    private List<GoodsInfo> goods_list;
}
