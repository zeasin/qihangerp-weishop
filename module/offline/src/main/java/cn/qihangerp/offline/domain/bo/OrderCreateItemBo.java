package cn.qihangerp.offline.domain.bo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderCreateItemBo {
    /** skuId */
    private String skuId;

    /** skuCode */
    private String skuCode;
    private String goodsName;
    private String skuName;
    private String goodsImg;
    private String outerErpSkuId;
    private BigDecimal salePrice;
    private BigDecimal itemAmount;
    private Integer quantity;
    private Integer isGift;
}