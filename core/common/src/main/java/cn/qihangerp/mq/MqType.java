package cn.qihangerp.mq;

public class MqType {
    /**
     * 订单消息
     */
    public static final int ORDER_MESSAGE = 1;
    /**
     * 退款消息
     */
    public static final int REFUND_MESSAGE = 2;
    /**
     * 备货消息
     */
    public static final int SHIP_STOCKUP_MESSAGE = 30;

    /**
     * 发货消息
     */
    public static final int SHIP_SEND_MESSAGE = 40;
}
