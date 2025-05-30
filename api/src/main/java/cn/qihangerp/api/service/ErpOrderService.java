package cn.qihangerp.api.service;

import cn.qihangerp.api.common.PageQuery;
import cn.qihangerp.api.common.PageResult;
import cn.qihangerp.api.common.ResultVo;
import cn.qihangerp.api.common.bo.ErpOrderAllocateShipBo;
import cn.qihangerp.api.common.bo.ErpOrderShipBo;
import cn.qihangerp.api.domain.ErpOrder;
import cn.qihangerp.api.domain.vo.SalesDailyVo;
import cn.qihangerp.api.request.OrderSearchRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author TW
* @description 针对表【erp_order(订单表)】的数据库操作Service
* @createDate 2024-04-03 15:57:41
*/
public interface ErpOrderService extends IService<ErpOrder> {
    PageResult<ErpOrder> queryPageList(OrderSearchRequest bo, PageQuery pageQuery);

    /**
     * 获取待发货list（去除处理过的）
     * @param bo
     * @param pageQuery
     * @return
     */
    PageResult<ErpOrder> queryWaitShipmentPageList(OrderSearchRequest bo, PageQuery pageQuery);

    /**
     * 已经发货的list
     * @param bo
     * @param pageQuery
     * @return
     */
    PageResult<ErpOrder> queryShippedPageList(OrderSearchRequest bo, PageQuery pageQuery);
    PageResult<ErpOrder> queryAssignedShipmentList(OrderSearchRequest bo, PageQuery pageQuery);
    ErpOrder queryDetailById(Long id);

    /**
     * 手动发货
     * @param shipBo
     * @return
     */
    ResultVo<Integer> manualShipmentOrder(ErpOrderShipBo shipBo,String createBy);

    /**
     * 分配给供应商发货
     * @param shipBo
     * @param createBy
     * @return
     */
    ResultVo<Integer> allocateShipmentOrder(ErpOrderAllocateShipBo shipBo, String createBy);

    List<SalesDailyVo> salesDaily(Long tenantId);
    SalesDailyVo getTodaySalesDaily(Long tenantId);
    Integer getWaitShipOrderAllCount(Long tenantId);
}
