package cn.qihangerp.open.wei.vo.ewaybill;

import cn.qihangerp.open.wei.vo.BaseResVo;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryGetVo extends BaseResVo {

    private List<DeliveryVo> list;

    private String shop_id;

}
