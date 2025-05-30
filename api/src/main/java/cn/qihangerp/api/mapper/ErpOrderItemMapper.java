package cn.qihangerp.api.mapper;

import cn.qihangerp.api.common.PageQuery;
import cn.qihangerp.api.common.PageResult;
import cn.qihangerp.api.domain.ErpOrder;
import cn.qihangerp.api.domain.ErpOrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.qihangerp.api.domain.bo.ErpOrderItemListBo;
import cn.qihangerp.api.domain.vo.ErpOrderItemListVo;

/**
* @author TW
* @description 针对表【erp_order_item(订单明细表)】的数据库操作Mapper
* @createDate 2024-04-03 15:57:41
* @Entity cn.qihangerp.api.domain.ErpOrderItem
*/
public interface ErpOrderItemMapper extends BaseMapper<ErpOrderItem> {
//    PageResult<ErpOrderItemListVo> selectOrderItemList(ErpOrderItemListBo bo);
}




