package com.demo.aop;

import com.demo.datasource.DynamicDataSourceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description DynamicDataSourceContextHolder
 * @Author zhouyw
 * @Date 2022/11/22 22:57
 **/
@Component
public class DynamicDataSourceContextHolder {

    public static final String MASTER_DB = "master-db";

    public static Map<Object, Object> dataSourcesMap = new ConcurrentHashMap<>();

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDB(String db) {
        Object dataSource = dataSourcesMap.get(db);
        if (dataSource == null) {
            // 创建新的数据源
            DynamicDataSourceUtil.setDataSource(db);
        }
        // 切换数据源
        contextHolder.set(db);
    }

    public static String getDB() {
        if (StringUtils.isNotBlank(contextHolder.get())) {
            return contextHolder.get();
        }
        return MASTER_DB;
    }

    public static void clearDB() {
        contextHolder.remove();
    }

}
