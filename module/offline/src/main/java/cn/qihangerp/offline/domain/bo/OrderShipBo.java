package cn.qihangerp.offline.domain.bo;

import lombok.Data;

@Data
public class OrderShipBo {
    private String orderNum;
    private String shippingCompany;
    private String shippingNumber;

}
