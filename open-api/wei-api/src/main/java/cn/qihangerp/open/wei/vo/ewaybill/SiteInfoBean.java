package cn.qihangerp.open.wei.vo.ewaybill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SiteInfoBean
 */
@NoArgsConstructor
@Data
public class SiteInfoBean {
    /**
     * address
     */
    @JsonProperty("address")
    private AddressBean address;
    /**
     * contact
     */
    @JsonProperty("contact")
    private ContactBean contact;
    /**
     * deliveryId
     */
    @JsonProperty("delivery_id")
    private String deliveryId;
    /**
     * siteCode
     */
    @JsonProperty("site_code")
    private String siteCode;
    /**
     * siteFullname
     */
    @JsonProperty("site_fullname")
    private String siteFullname;
    /**
     * siteName
     */
    @JsonProperty("site_name")
    private String siteName;
    /**
     * siteStatus
     */
    @JsonProperty("site_status")
    private int siteStatus;
}
