package cn.qihangerp.offline.controller;

import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.domain.OShop;
import cn.qihangerp.mq.EnumShopType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.qihangerp.offline.service.OShopService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 店铺Controller
 * 
 * @author qihang
 * @date 2023-12-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("/offline/shop")
public class OfflineShopController extends BaseController {
    private final OShopService shopService;

    /**
     * 查询店铺列表logistics
     */
    @GetMapping("/list")
    public TableDataInfo list(OShop shop)
    {
        List<OShop> list = shopService.list(new LambdaQueryWrapper<OShop>().eq(OShop::getType, EnumShopType.OFFLINE.getIndex()));
        return getDataTable(list);
    }


    /**
     * 获取店铺详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(shopService.getById(id));
    }

    /**
     * 新增店铺
     */
    @PostMapping()
    public AjaxResult add(@RequestBody OShop shop)
    {
        shop.setType(EnumShopType.OFFLINE.getIndex());
        shop.setModifyOn(System.currentTimeMillis()/1000);
        return toAjax(shopService.save(shop));
    }

    /**
     * 修改店铺
     */
    @PutMapping()
    public AjaxResult edit(@RequestBody OShop shop)
    {
        if(shop.getId()==null) return AjaxResult.error("缺少参数：id");
        shop.setType(null);
        shop.setModifyOn(System.currentTimeMillis() /1000);
        shopService.updateById(shop);
        return toAjax(1);
    }

    /**
     * 删除店铺
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shopService.removeBatchByIds(Arrays.stream(ids).toList()));
    }

}
