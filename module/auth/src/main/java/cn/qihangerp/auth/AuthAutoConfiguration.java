package cn.qihangerp.auth;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class ,MybatisAutoConfiguration.class})
//@ComponentScan
//@Configuration
//@EnableAutoConfiguration
@Configuration
// @EnableAutoConfiguration(exclude = MybatisAutoConfiguration.class)
@ComponentScan(basePackages = {"cn.qihangerp"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AuthAutoConfiguration.class))
public class AuthAutoConfiguration {
}