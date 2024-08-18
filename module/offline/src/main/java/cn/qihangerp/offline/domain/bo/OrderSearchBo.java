package cn.qihangerp.offline.domain.bo;

import lombok.Data;

@Data
public class OrderSearchBo {
    private Integer shopId;
    private String orderNum;
    private String orderStatus;
    private String startTime;
    private String endTime;
    private Integer erpPushStatus;
}
