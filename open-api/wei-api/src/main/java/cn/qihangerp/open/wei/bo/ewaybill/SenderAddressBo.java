package cn.qihangerp.open.wei.bo.ewaybill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SenderAddressBean
 */
@NoArgsConstructor
@Data
public class SenderAddressBo {
    @JsonProperty("name")
    private String name;

    @JsonProperty("mobile")
    private String mobile;
    /**
     * address
     */
    @JsonProperty("address")
    private String address;

    @JsonProperty("street")
    private String street;

    /**
     * city
     */
    @JsonProperty("city")
    private String city;
    /**
     * county
     */
    @JsonProperty("county")
    private String county;
    /**
     * province
     */
    @JsonProperty("province")
    private String province;
}
