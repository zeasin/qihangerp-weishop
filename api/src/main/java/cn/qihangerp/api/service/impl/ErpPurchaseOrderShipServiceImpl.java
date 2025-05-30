package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.common.PageQuery;
import cn.qihangerp.api.common.PageResult;
import cn.qihangerp.api.common.utils.DateUtils;
import cn.qihangerp.api.domain.ErpPurchaseOrder;
import cn.qihangerp.api.domain.ErpStockIn;
import cn.qihangerp.api.domain.ErpStockInItem;
import cn.qihangerp.api.domain.bo.PurchaseOrderStockInBo;
import cn.qihangerp.api.mapper.ErpPurchaseOrderMapper;
import cn.qihangerp.api.mapper.ErpStockInItemMapper;
import cn.qihangerp.api.mapper.ErpStockInMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.domain.ErpPurchaseOrderShip;
import cn.qihangerp.api.service.ErpPurchaseOrderShipService;
import cn.qihangerp.api.mapper.ErpPurchaseOrderShipMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【erp_purchase_order_ship(采购订单物流表)】的数据库操作Service实现
* @createDate 2024-04-21 20:27:10
*/
@AllArgsConstructor
@Service
public class ErpPurchaseOrderShipServiceImpl extends ServiceImpl<ErpPurchaseOrderShipMapper, ErpPurchaseOrderShip>
    implements ErpPurchaseOrderShipService{
    private final ErpPurchaseOrderShipMapper mapper;
    private final ErpPurchaseOrderMapper orderMapper;
    private final ErpStockInMapper stockInEntryMapper;
    private final ErpStockInItemMapper stockInEntryItemMapper;

    @Override
    public PageResult<ErpPurchaseOrderShip> queryPageList(ErpPurchaseOrderShip bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpPurchaseOrderShip> queryWrapper = new LambdaQueryWrapper<ErpPurchaseOrderShip>()
                .eq(ErpPurchaseOrderShip::getTenantId,bo.getTenantId())
                .eq(bo.getSupplierId()!=null,ErpPurchaseOrderShip::getSupplierId,bo.getSupplierId())
                .eq(org.springframework.util.StringUtils.hasText(bo.getOrderNo()),ErpPurchaseOrderShip::getOrderNo,bo.getOrderNo())
                ;
        Page<ErpPurchaseOrderShip> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }

    @Transactional
    @Override
    public int confirmPurchaseOrderShip(ErpPurchaseOrderShip bo)
    {
        ErpPurchaseOrderShip ship = mapper.selectById(bo.getId());
        if(ship== null) return -1;
        else if(ship.getStatus()!=0)return -2;
        // 更新采购单状态
        ErpPurchaseOrder order = new ErpPurchaseOrder();
        order.setId(ship.getOrderId());
        order.setStatus(2);
        order.setReceivedTime(bo.getReceiptTime());
        order.setUpdateTime(DateUtils.getNowDate());
        order.setUpdateBy(bo.getUpdateBy());
        orderMapper.updateById(order);
        //更新
        ErpPurchaseOrderShip update = new ErpPurchaseOrderShip();
        update.setUpdateTime(DateUtils.getNowDate());
        update.setUpdateBy(bo.getUpdateBy());
        update.setStatus(1);
        update.setRemark(bo.getRemark());
        update.setReceiptTime(bo.getReceiptTime());
        update.setId(ship.getId());
        return mapper.updateById(update);
    }

    /**
     * 创建入库单
     * @param bo
     * @return
     */
    @Transactional
    @Override
    public int createStockInEntry(PurchaseOrderStockInBo bo) {
        ErpPurchaseOrderShip ship = mapper.selectById(bo.getId());
        if(ship == null) return -1;//数据不存在

        else if(ship.getStatus().intValue() == 0) return -2;//未确认收货不允许操作
        else if(ship.getStatus().intValue() == 2) return -3;//已入库请勿重复操作
        else if (ship.getStatus().intValue() == 1) {
            ErpStockIn entry = new ErpStockIn();
            entry.setStockInNum(DateUtils.parseDateToStr("yyyyMMddHHmmss",new Date()));
            entry.setSourceId(Long.parseLong(ship.getId()));
            entry.setTenantId(ship.getTenantId());
            entry.setSourceNo(ship.getOrderNo());
            entry.setSourceSpecUnit(ship.getOrderSpecUnit());
            entry.setSourceGoodsUnit(ship.getOrderGoodsUnit());
            entry.setSourceSpecUnitTotal(ship.getOrderSpecUnitTotal().intValue());
            entry.setStockInType(1);
            entry.setStatus(0);
            entry.setCreateBy(bo.getCreateBy());
            entry.setCreateTime(new Date());
            stockInEntryMapper.insert(entry);

            // 子表
            if(bo.getGoodsList()!=null && bo.getGoodsList().size()>0){
                List<ErpStockInItem> items = new ArrayList<>();
                for (var item:bo.getGoodsList()) {
                    ErpStockInItem entryItem = new ErpStockInItem();
                    entryItem.setStockInId(entry.getId());
                    entryItem.setTenantId(entry.getTenantId());
                    entryItem.setStockInType(1);
                    entryItem.setSourceId(Long.parseLong(ship.getId()));
                    entryItem.setSourceItemId(item.getId());
                    entryItem.setGoodsId(item.getGoodsId());
                    entryItem.setGoodsNum(item.getGoodsNum());
                    entryItem.setGoodsName(item.getGoodsName());
                    entryItem.setGoodsImage(item.getColorImage());
                    entryItem.setSkuId(item.getSpecId());
                    entryItem.setSkuCode(item.getSpecNum());
                    String skuName="" ;
                    if(StringUtils.hasText(item.getColorValue())){
                        skuName=item.getColorValue();
                    }
                    if(StringUtils.hasText(item.getSizeValue())){
                        skuName+=item.getSizeValue();
                    }
                    if(StringUtils.hasText(item.getStyleValue())){
                        skuName+=item.getStyleValue();
                    }
                    entryItem.setSkuName(skuName);
                    entryItem.setPurPrice(item.getPurPrice());
                    entryItem.setQuantity(item.getQuantity());
                    entryItem.setInQuantity(0);
                    entryItem.setCreateBy(bo.getCreateBy());
                    entryItem.setCreateTime(new Date());
                    entryItem.setRemark("");
//                    entryItem.setLocationId(0L);
                    entryItem.setStatus(0);
                    items.add(entryItem);
                    stockInEntryItemMapper.insert(entryItem);
                }

            }

            // 更新表状态
            ErpPurchaseOrderShip update = new ErpPurchaseOrderShip();
            update.setUpdateTime(DateUtils.getNowDate());
            update.setStockInTime(DateUtils.getNowDate());
            update.setUpdateBy(bo.getCreateBy());
            update.setStatus(2);
            update.setId(ship.getId());
            mapper.updateById(update);

            //更新 采购订单
            // 更新采购单状态
            ErpPurchaseOrder order = new ErpPurchaseOrder();
            order.setId(bo.getId().toString());
            order.setStatus(3);
            order.setStockInTime(DateUtils.getNowDate());
            order.setUpdateTime(DateUtils.getNowDate());
            order.setUpdateBy(bo.getCreateBy());
            orderMapper.updateById(order);

            return 1;
        }
        else return -4;
    }
}




