package cn.qihangerp.offline.service;


import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.offline.domain.OfflineOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.qihangerp.offline.domain.bo.OrderCreateBo;
import cn.qihangerp.offline.domain.bo.OrderSearchBo;
import cn.qihangerp.offline.domain.bo.OrderShipBo;

/**
* @author qilip
* @description 针对表【offline_order(线下渠道订单表)】的数据库操作Service
* @createDate 2024-07-27 23:03:38
*/
public interface OfflineOrderService extends IService<OfflineOrder> {
    PageResult<OfflineOrder> queryPageList(OrderSearchBo bo, PageQuery pageQuery);

    OfflineOrder queryDetailById(Long id);

    /**
     * 手动添加订单
     * @param bo
     * @return
     */
    Long insertOfflineOrder(OrderCreateBo bo, String createBy);
    int orderLogistics(OrderShipBo bo,String operator);
}
