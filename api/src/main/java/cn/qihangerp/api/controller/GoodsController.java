package cn.qihangerp.api.controller;

import java.util.List;

import cn.qihangerp.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;

import cn.qihangerp.api.domain.vo.GoodsSpecListVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.api.domain.Goods;
import cn.qihangerp.api.service.IGoodsService;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 商品管理Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/goods")
public class GoodsController extends BaseController
{
    @Autowired
    private IGoodsService goodsService;

    /**
     * 搜索商品SKU
     * 条件：商品编码、SKU、商品名称
     */
    @GetMapping("/searchSku")
    public TableDataInfo searchSkuBy(String keyword)
    {
        startPage();
        List<GoodsSpecListVo> list = goodsService.searchGoodsSpec(keyword);
        return getDataTable(list);
    }

    /**
     * 查询商品管理列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goods:list')")
    @GetMapping("/list")
    public TableDataInfo list(Goods goods)
    {
        startPage();
        List<Goods> list = goodsService.selectGoodsList(goods);
        return getDataTable(list);
    }

    /**
     * 导出商品管理列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goods:export')")
    @Log(title = "商品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Goods goods)
    {
        List<Goods> list = goodsService.selectGoodsList(goods);
        ExcelUtil<Goods> util = new ExcelUtil<Goods>(Goods.class);
        util.exportExcel(response, list, "商品管理数据");
    }

    /**
     * 获取商品管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:goods:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(goodsService.selectGoodsById(id));
    }

    /**
     * 新增商品管理
     */
    @PreAuthorize("@ss.hasPermi('goods:goods:add')")
    @Log(title = "商品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Goods goods)
    {
        goods.setCreateBy(SecurityUtils.getUsername());
        int result = goodsService.insertGoods(goods);
        if(result == -1) new AjaxResult(501,"商品编码已存在");
        return toAjax(1);
    }

    /**
     * 修改商品管理
     */
    @PreAuthorize("@ss.hasPermi('goods:goods:edit')")
    @Log(title = "商品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Goods goods)
    {
        return toAjax(goodsService.updateGoods(goods));
    }

//    /**
//     * 删除商品管理
//     */
//    @PreAuthorize("@ss.hasPermi('goods:goods:remove')")
//    @Log(title = "商品管理", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(goodsService.deleteGoodsByIds(ids));
//    }
}
