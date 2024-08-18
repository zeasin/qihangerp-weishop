package cn.qihangerp.api;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = {"cn.qihangerp.api.mapper", "cn.qihangerp.auth.mapper", "cn.qihangerp.tao.mapper"
        ,"cn.qihangerp.api.pdd.mapper","cn.qihangerp.api.dou.mapper","cn.qihangerp.api.xhs.mapper"
        ,"cn.qihangerp.api.wei.mapper","cn.qihangerp.jd.mapper","cn.qihangerp.offline.mapper"})
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
