package com.demo.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.demo.aop.DynamicDataSourceContextHolder;
import com.demo.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;

/**
 * @Description DynamicDataSourceUtil
 * @Author zhouyw
 * @Date 2022/11/22 22:59
 **/
@Slf4j
@Component
public class DynamicDataSourceUtil {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String ds_userName;
    @Value("${spring.datasource.password}")
    private String ds_userPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String ds_driverClassName;

    private static String JDBC_DB_NAME;
    private static String JDBC_PREFIX_URL;
    private static String JDBC_SUFFIX_URL;
    private static String JDBC_DS_USER_NAME;
    private static String JDBC_DS_USER_PASSWORD;
    private static String JDBC_DS_DRIVER_CLASS_NAME;

    @PostConstruct
    public void init() {
        String[] split = url.split("\\?");
        JDBC_DB_NAME = split[0].substring(split[0].lastIndexOf("/")+1);
        JDBC_PREFIX_URL = split[0].substring(0, split[0].lastIndexOf("/")+1);
        JDBC_SUFFIX_URL = split.length>=2 ? "?"+split[1] : "";
        JDBC_DS_USER_NAME = ds_userName;
        JDBC_DS_USER_PASSWORD = ds_userPassword;
        JDBC_DS_DRIVER_CLASS_NAME = ds_driverClassName;
    }

    public static void setDataSource(String dsKey) {
        String url = JDBC_PREFIX_URL + dsKey + JDBC_SUFFIX_URL;
        if (checkDsIsLive(url, JDBC_DS_USER_NAME, JDBC_DS_USER_PASSWORD, dsKey)) {
            // 创建数据源
//            HikariDataSource dataSource = new HikariDataSource();
//            dataSource.setJdbcUrl(url);
//            dataSource.setUsername(JDBC_DS_USER_NAME);
//            dataSource.setPassword(JDBC_DS_USER_PASSWORD);
//            dataSource.setDriverClassName(JDBC_DS_DRIVER_CLASS_NAME);

            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(JDBC_DS_USER_NAME);
            dataSource.setPassword(JDBC_DS_USER_PASSWORD);
            dataSource.setDriverClassName(JDBC_DS_DRIVER_CLASS_NAME);

            // 配置数据源
            DynamicDataSource dynamicDataSource = SpringBeanUtil.getBean("dynamicDataSource");
            DynamicDataSourceContextHolder.dataSourcesMap.put(dsKey, dataSource);
            dynamicDataSource.afterPropertiesSet();
            //切换数据源
            log.debug("#########已添加  {} 数据源#########", dsKey);
        }else {
            log.info("数据库 "+dsKey+" 不存在！");
        }
    }

    private static boolean checkDsIsLive(String url,String userName,String password,String dsKey) {
        String checkDSIsExist= "show databases like '"+dsKey+"'";
        boolean flag = false;
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement stat = connection.createStatement();
            ResultSet result = stat.executeQuery(checkDSIsExist);
            if (result.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void changeDataSource(String key) throws ServiceException {
        if (StringUtils.isNotEmpty(key)) {
            Object dataSource = DynamicDataSourceContextHolder.dataSourcesMap.get(key);
            if (dataSource == null) {//创建新的数据源并切换
                setDataSource(key);
            }else {//如果已存在只切换数据源
                DynamicDataSourceContextHolder.setDB(key);
            }
        }
    }

    public static void clearDataSourceToDefault() throws ServiceException{
        DynamicDataSourceContextHolder.clearDB();
    }

    public static String getDbName() {
        return JDBC_DB_NAME;
    }

}
