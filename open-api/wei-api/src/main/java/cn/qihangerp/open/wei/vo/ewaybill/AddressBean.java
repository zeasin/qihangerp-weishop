package cn.qihangerp.open.wei.vo.ewaybill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AddressBean
 */
@NoArgsConstructor
@Data
public class AddressBean {
    /**
     * cityCode
     */
    @JsonProperty("city_code")
    private String cityCode;
    /**
     * cityName
     */
    @JsonProperty("city_name")
    private String cityName;
    /**
     * countryCode
     */
    @JsonProperty("country_code")
    private String countryCode;
    /**
     * detailAddress
     */
    @JsonProperty("detail_address")
    private String detailAddress;
    /**
     * districtCode
     */
    @JsonProperty("district_code")
    private String districtCode;
    /**
     * districtName
     */
    @JsonProperty("district_name")
    private String districtName;
    /**
     * provinceCode
     */
    @JsonProperty("province_code")
    private String provinceCode;
    /**
     * provinceName
     */
    @JsonProperty("province_name")
    private String provinceName;
    /**
     * streetCode
     */
    @JsonProperty("street_code")
    private String streetCode;
    /**
     * streetName
     */
    @JsonProperty("street_name")
    private String streetName;
}
