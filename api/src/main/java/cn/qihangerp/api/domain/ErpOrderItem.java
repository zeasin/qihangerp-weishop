package cn.qihangerp.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 订单明细表
 * @TableName erp_order_item
 */
@Data
public class ErpOrderItem implements Serializable {
    /**
     * id，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long shopId;
    private Long tenantId;

    /**
     * 订单ID（o_order外键）
     */
    private String orderId;

    /**
     * 订单号（第三方平台）
     */
    private String orderNum;

    /**
     * 子订单号（第三方平台）
     */
    private String subOrderNum;

    /**
     * 第三方平台skuId
     */
    private String skuId;

    /**
     * erp系统商品id
     */
    private Long erpGoodsId;

    /**
     * erp系统商品规格id
     */
    private Long erpSkuId;

    /**
     * 商品标题
     */
    private String goodsTitle;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品编码
     */
    private String goodsNum;

    /**
     * 商品规格
     */
    private String goodsSpec;

    /**
     * 商品规格编码
     */
    private String skuNum;

    /**
     * 商品单价
     */
    private Double goodsPrice;

    /**
     * 子订单金额
     */
    private Double itemAmount;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 已退货数量
     */
    private Integer refundCount;

    /**
     * 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 
     */
    private Integer refundStatus;

    /**
     * 是否推送到ERP
     */
    private Integer hasPushErp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    //发货状态 0 待发货 1 已分配供应商发货 2全部发货
    private Integer shipStatus;
    // 发货方 0 自己发货1联合发货2供应商发货
    private Integer shipper;
    private Integer shipType;//发货方式1电子面单发货2手动发货

    private static final long serialVersionUID = 1L;
}