package cn.qihangerp.open.wei.bo.ewaybill;

import lombok.Data;

@Data
public class AccountGetApiBo {
    private String delivery_id;
    private String acct_id;
    private Boolean need_balance;
    private Integer status;
    private Integer offset;
    private Integer limit;
}
