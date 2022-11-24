package com.demo.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.demo.aop.DynamicDataSourceContextHolder;
import com.demo.utils.SpringBeanUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @Description 初始化数据源
 * @Author zhouyw
 * @Date 2022/11/22 23:38
 **/
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class DynamicDataSourceConfig {

    /**
     * 主数据源
     * @return DataSource
     */
    @Bean(DynamicDataSourceContextHolder.MASTER_DB)
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 动态数据源
     * @return DataSource
     */
    @Bean
    @Primary
    @DependsOn({DynamicDataSourceContextHolder.MASTER_DB, "springBeanUtil", "dynamicDataSourceContextHolder"})
    public DataSource dynamicDataSource() {
        // 默认保存主数据源到内存中
        DynamicDataSourceContextHolder.dataSourcesMap.put(DynamicDataSourceContextHolder.MASTER_DB, SpringBeanUtil.getBean(DynamicDataSourceContextHolder.MASTER_DB));
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(DynamicDataSourceContextHolder.dataSourcesMap);
        dynamicDataSource.setDefaultTargetDataSource(SpringBeanUtil.getBean(DynamicDataSourceContextHolder.MASTER_DB));
        return dynamicDataSource;
    }

}
