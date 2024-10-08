package cn.qihangerp.api.controller;

import java.util.List;

import cn.qihangerp.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;

import cn.qihangerp.api.domain.bo.PurchaseOrderAddBo;
import cn.qihangerp.api.domain.bo.PurchaseOrderOptionBo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.api.domain.ScmPurchaseOrder;
import cn.qihangerp.api.service.IScmPurchaseOrderService;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 采购订单Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/purchase/purchaseOrder")
public class ScmPurchaseOrderController extends BaseController
{
    @Autowired
    private IScmPurchaseOrderService scmPurchaseOrderService;

    /**
     * 查询采购订单列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScmPurchaseOrder scmPurchaseOrder)
    {
        startPage();
        List<ScmPurchaseOrder> list = scmPurchaseOrderService.selectScmPurchaseOrderList(scmPurchaseOrder);
        return getDataTable(list);
    }

    /**
     * 导出采购订单列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrder:export')")
    @Log(title = "采购订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScmPurchaseOrder scmPurchaseOrder)
    {
        List<ScmPurchaseOrder> list = scmPurchaseOrderService.selectScmPurchaseOrderList(scmPurchaseOrder);
        ExcelUtil<ScmPurchaseOrder> util = new ExcelUtil<ScmPurchaseOrder>(ScmPurchaseOrder.class);
        util.exportExcel(response, list, "采购订单数据");
    }

    /**
     * 获取采购订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ScmPurchaseOrder order = scmPurchaseOrderService.selectScmPurchaseOrderById(id);
        return success(order);
    }

    /**
     * 新增采购订单
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrder:add')")
    @Log(title = "采购订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PurchaseOrderAddBo addBo)
    {
        addBo.setCreateBy(SecurityUtils.getUsername());
        return toAjax(scmPurchaseOrderService.insertScmPurchaseOrder(addBo));
    }

    /**
     * 修改采购订单
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrder:edit')")
    @Log(title = "采购订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PurchaseOrderOptionBo scmPurchaseOrder)
    {
        scmPurchaseOrder.setUpdateBy(SecurityUtils.getUsername());
        int result = scmPurchaseOrderService.updateScmPurchaseOrder(scmPurchaseOrder);
        if(result == -1){
            return new AjaxResult(0,"状态不正确");
        }else{
            return toAjax(result);
        }

    }

    /**
     * 删除采购订单
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrder:remove')")
    @Log(title = "采购订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(scmPurchaseOrderService.deleteScmPurchaseOrderByIds(ids));
    }
}
