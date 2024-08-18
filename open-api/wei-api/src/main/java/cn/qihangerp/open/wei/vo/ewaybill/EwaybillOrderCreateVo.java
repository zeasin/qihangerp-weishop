package cn.qihangerp.open.wei.vo.ewaybill;

import cn.qihangerp.open.wei.vo.BaseResVo;
import lombok.Data;

@Data
public class EwaybillOrderCreateVo extends BaseResVo {
    private String ewaybill_order_id;
    private String waybill_id;
    private String delivery_error_msg;
    private String print_info;
}
