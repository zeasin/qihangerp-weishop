package cn.qihangerp.offline.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.offline.domain.OfflineOrderItem;
import cn.qihangerp.offline.service.OfflineOrderItemService;
import cn.qihangerp.offline.mapper.OfflineOrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author qilip
* @description 针对表【offline_order_item(渠道订单明细表)】的数据库操作Service实现
* @createDate 2024-07-27 23:03:38
*/
@Service
public class OfflineOrderItemServiceImpl extends ServiceImpl<OfflineOrderItemMapper, OfflineOrderItem>
    implements OfflineOrderItemService{

}




