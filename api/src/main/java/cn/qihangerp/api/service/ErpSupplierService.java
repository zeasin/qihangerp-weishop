package cn.qihangerp.api.service;

import cn.qihangerp.api.common.PageQuery;
import cn.qihangerp.api.common.PageResult;
import cn.qihangerp.api.domain.ErpSupplier;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【erp_supplier】的数据库操作Service
* @createDate 2024-04-21 16:16:24
*/
public interface ErpSupplierService extends IService<ErpSupplier> {
    PageResult<ErpSupplier> queryPageList(ErpSupplier bo, PageQuery pageQuery);
}
