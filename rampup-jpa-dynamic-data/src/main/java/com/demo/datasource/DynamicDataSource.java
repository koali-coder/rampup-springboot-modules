package com.demo.datasource;

import com.demo.aop.DynamicDataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Description 继承 AbstractRoutingDataSource 重写获取数据源方法
 * @Author zhouyw
 * @Date 2022/11/22 22:56
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDB();
    }

}
