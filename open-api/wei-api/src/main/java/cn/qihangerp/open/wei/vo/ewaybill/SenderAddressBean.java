package cn.qihangerp.open.wei.vo.ewaybill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SenderAddressBean
 */
@NoArgsConstructor
@Data
public class SenderAddressBean {
    /**
     * address
     */
    @JsonProperty("address")
    private String address;
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
