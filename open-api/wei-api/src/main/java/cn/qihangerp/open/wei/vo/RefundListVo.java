package cn.qihangerp.open.wei.vo;

import lombok.Data;

@Data
public class RefundListVo extends BaseResVo {

    private Long[] after_sale_order_id_list;

    private boolean has_more;

}
