package cn.qihangerp.offline.service;


import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.offline.domain.OfflineGoodsSku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author qilip
* @description 针对表【offline_goods_sku(OMS商品SKU表)】的数据库操作Service
* @createDate 2024-07-27 21:22:27
*/
public interface OfflineGoodsSkuService extends IService<OfflineGoodsSku> {
    PageResult<OfflineGoodsSku> querySkuPageList(OfflineGoodsSku bo, PageQuery pageQuery);
    List<OfflineGoodsSku> searchGoodsSpec(String keyword);
    int insertGoodsSku(OfflineGoodsSku goodsSku);
}
