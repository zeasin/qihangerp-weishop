package tech.qihangec.api.common.wei.vo;

import lombok.Data;

@Data
public class Token {
    private String access_token;
    private Long expires_in;
    private Integer errcode;
    private String errmsg;
}
