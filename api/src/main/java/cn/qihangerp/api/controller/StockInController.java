package cn.qihangerp.api.controller;

import cn.qihangerp.api.common.*;
import cn.qihangerp.api.domain.ErpStockIn;
import cn.qihangerp.api.request.StockInCreateRequest;
import cn.qihangerp.api.request.StockInRequest;
import cn.qihangerp.api.service.ErpStockInService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/stockIn")
public class StockInController extends BaseController {
    private final ErpStockInService stockInService;
    @GetMapping("/list")
    public TableDataInfo list(ErpStockIn bo, PageQuery pageQuery)
    {
        bo.setTenantId(getUserId());
        var pageList = stockInService.queryPageList(bo,pageQuery);
        return getDataTable(pageList);
    }

    @PostMapping("/create")
    public AjaxResult createEntry(@RequestBody StockInCreateRequest param, HttpServletRequest request)
    {
        ResultVo<Long> resultVo = stockInService.createEntry(getUserId(), getUsername(), param);
        if(resultVo.getCode()==0)
            return AjaxResult.success();
        else return AjaxResult.error(resultVo.getMsg());
    }

    @PostMapping("/in")
    public AjaxResult in(@RequestBody StockInRequest param, HttpServletRequest request)
    {
        ResultVo<Long> resultVo = stockInService.stockIn(getUserId(), getUsername(), param);
        if(resultVo.getCode()==0)
            return AjaxResult.success();
        else return AjaxResult.error(resultVo.getMsg());
    }

    @GetMapping(value = "/detail/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ErpStockIn entry = stockInService.getDetailAndItemById(id);

        return success(entry);
    }

}
