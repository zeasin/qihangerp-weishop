package cn.qihangerp.offline.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.offline.domain.OfflineGoodsSku;
import cn.qihangerp.offline.service.OfflineGoodsSkuService;
import cn.qihangerp.offline.mapper.OfflineGoodsSkuMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【offline_goods_sku(OMS商品SKU表)】的数据库操作Service实现
* @createDate 2024-07-27 21:22:27
*/
@AllArgsConstructor
@Service
public class OfflineGoodsSkuServiceImpl extends ServiceImpl<OfflineGoodsSkuMapper, OfflineGoodsSku>
    implements OfflineGoodsSkuService{
    private final OfflineGoodsSkuMapper skuMapper;
    @Override
    public PageResult<OfflineGoodsSku> querySkuPageList(OfflineGoodsSku bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OfflineGoodsSku> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(bo.getId()!=null,OfflineGoodsSku::getId,bo.getId());
        queryWrapper.eq(bo.getOuterErpSkuId()!=null,OfflineGoodsSku::getOuterErpSkuId,bo.getOuterErpSkuId());
        queryWrapper.eq(bo.getOuterErpGoodsId()!=null,OfflineGoodsSku::getOuterErpGoodsId,bo.getOuterErpGoodsId());
        queryWrapper.eq(StringUtils.hasText(bo.getSkuCode()),OfflineGoodsSku::getSkuCode,bo.getSkuCode());
        Page<OfflineGoodsSku> pages = skuMapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }

    @Override
    public List<OfflineGoodsSku> searchGoodsSpec(String keyword) {
        LambdaQueryWrapper<OfflineGoodsSku> queryWrapper =
                new LambdaQueryWrapper<OfflineGoodsSku>()
                        .likeRight(OfflineGoodsSku::getOuterErpSkuId,keyword).or()
                        .likeRight(OfflineGoodsSku::getId,keyword).or()
                        .likeRight(OfflineGoodsSku::getSkuCode,keyword);
        queryWrapper.last("LIMIT 10");
        return skuMapper.selectList(queryWrapper);
    }

    @Transactional
    @Override
    public int insertGoodsSku(OfflineGoodsSku goodsSku) {
        // 是否存在
        List<OfflineGoodsSku> oGoodsSkus = skuMapper.selectList(new LambdaQueryWrapper<OfflineGoodsSku>()
                .eq(OfflineGoodsSku::getSkuCode, goodsSku.getSkuCode()));

        if(oGoodsSkus==null || oGoodsSkus.size() ==0) {
            goodsSku.setCreateTime(new Date());
            goodsSku.setCreateBy("手动添加");
            return skuMapper.insert(goodsSku);
        }
        return -1;
    }
}




