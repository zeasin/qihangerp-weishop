package cn.qihangerp.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.domain.Shop;
import cn.qihangerp.api.service.ShopService;
import cn.qihangerp.api.mapper.ShopMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
* @author qilip
* @description 针对表【erp_shop(数据中心-店铺)】的数据库操作Service实现
* @createDate 2024-04-21 18:20:49
*/
@AllArgsConstructor
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop>
    implements ShopService{
    private final ShopMapper mapper;
    @Override
    public List<Shop> selectShopList(Shop shop) {
        LambdaQueryWrapper<Shop> qw = new LambdaQueryWrapper<Shop>()
                .eq(shop.getType()!=null,Shop::getType,shop.getType())
                .eq(shop.getTenantId()!=null,Shop::getTenantId,shop.getTenantId())
                ;
        return mapper.selectList(qw);
    }

    @Override
    public void updateSessionKey(Long shopId, String token,String refreshToken) {
        Shop shop = new Shop();
        shop.setId(shopId.toString());
        shop.setAccessToken(token);
        shop.setRefreshToken(refreshToken);
        mapper.updateById(shop);
    }

    @Override
    public List<Shop> selectShopByShopType(Integer shopType) {
        LambdaQueryWrapper<Shop> qw = new LambdaQueryWrapper<Shop>()
                .eq(Shop::getType,shopType)
                .eq(Shop::getStatus,0);
        return mapper.selectList(qw);
    }

}




