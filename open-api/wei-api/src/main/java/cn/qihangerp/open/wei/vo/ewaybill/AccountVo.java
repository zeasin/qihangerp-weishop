package cn.qihangerp.open.wei.vo.ewaybill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AccountVo {

    /**
     * acctId
     */
    @JsonProperty("acct_id")
    private String acctId;
    /**
     * acctType
     */
    @JsonProperty("acct_type")
    private int acctType;
    /**
     * allocated
     */
    @JsonProperty("allocated")
    private int allocated;
    /**
     * available
     */
    @JsonProperty("available")
    private int available;
    /**
     * cancel
     */
    @JsonProperty("cancel")
    private int cancel;
    /**
     * companyType
     */
    @JsonProperty("company_type")
    private int companyType;
    /**
     * deliveryId
     */
    @JsonProperty("delivery_id")
    private String deliveryId;
    /**
     * monthlyCard
     */
    @JsonProperty("monthly_card")
    private String monthlyCard;
    /**
     * recycled
     */
    @JsonProperty("recycled")
    private int recycled;
    /**
     * senderAddress
     */
    @JsonProperty("sender_address")
    private SenderAddressBean senderAddress;
    /**
     * share
     */
    @JsonProperty("share")
    private ShareBean share;
    /**
     * shopId
     */
    @JsonProperty("shop_id")
    private String shopId;
    /**
     * siteInfo
     */
    @JsonProperty("site_info")
    private SiteInfoBean siteInfo;
    /**
     * status
     */
    @JsonProperty("status")
    private int status;
}
