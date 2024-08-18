package cn.qihangerp.open.wei.vo.ewaybill;

import cn.qihangerp.open.wei.vo.BaseResVo;
import lombok.Data;

import java.util.List;

@Data
public class AccountGetVo extends BaseResVo {
    private Integer total_num;
    private List<AccountVo> account_list;
}
