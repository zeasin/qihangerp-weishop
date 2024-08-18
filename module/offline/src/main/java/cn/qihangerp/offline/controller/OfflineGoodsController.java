package cn.qihangerp.offline.controller;


import cn.qihangerp.common.PageQuery;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.offline.service.OfflineGoodsSkuService;
import cn.qihangerp.offline.domain.OfflineGoodsSku;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 商品管理Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@AllArgsConstructor
@RestController
@RequestMapping("/offline/goods")
public class OfflineGoodsController extends BaseController
{
    private final OfflineGoodsSkuService skuService;

    /**
     * 搜索商品SKU
     * 条件：商品编码、SKU、商品名称
     */
    @GetMapping("/searchSku")
    public TableDataInfo searchSkuBy(String keyword)
    {
        logger.info("========SKU搜索=========",keyword);
        List<OfflineGoodsSku> list = skuService.searchGoodsSpec(keyword);
        return getDataTable(list);
    }

    @GetMapping("/sku_list")
    public TableDataInfo skuList(OfflineGoodsSku bo, PageQuery pageQuery)
    {
        var pageList = skuService.querySkuPageList(bo,pageQuery);
        return getDataTable(pageList);
    }


    /**
     * 获取商品管理详细信息
     */
    @GetMapping(value = "/sku/{id}")
    public AjaxResult getSkuInfo(@PathVariable("id") Long id)
    {
        return success(skuService.getById(id));
    }



    @PostMapping("/sku")
    public AjaxResult addSku(@RequestBody OfflineGoodsSku goodsSku)
    {
        int result = skuService.insertGoodsSku(goodsSku);
        if(result == -1) new AjaxResult(501,"商品编码已存在");
        return toAjax(1);

    }

    @PutMapping("/sku")
    public AjaxResult editSku(@RequestBody OfflineGoodsSku sku)
    {
        return toAjax(skuService.updateById(sku));
    }

    /**
     * 删除商品管理
     */
    @DeleteMapping("/sku/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(skuService.removeBatchByIds(Arrays.stream(ids).toList()));
    }

}
