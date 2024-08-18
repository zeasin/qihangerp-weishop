package cn.qihangerp.open.wei.vo.ewaybill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ContactBean
 */
@NoArgsConstructor
@Data
public class ContactBean {
    /**
     * mobile
     */
    @JsonProperty("mobile")
    private String mobile;
    /**
     * name
     */
    @JsonProperty("name")
    private String name;
    /**
     * phone
     */
    @JsonProperty("phone")
    private String phone;
}
