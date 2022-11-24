package com.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Description 数据库切换AOP
 * @Author zhouyw
 * @Date 2022/11/22 22:56
 **/
@Slf4j
@Aspect
@Component
public class AspectDatabaseQuery {

    @Pointcut("execution(public * com.demo.repository.master..*.*(..))")
    public void master() {
    }

    /**
     * master包下切换为主库
     * @param point point
     */
    @Before("master()")
    public void setMasterDB(JoinPoint point){
        DynamicDataSourceContextHolder.setDB(DynamicDataSourceContextHolder.MASTER_DB);
    }

    @Pointcut("execution(public * com.demo.repository.slave..*.*(..))")
    public void slave() {
    }

    /**
     * slave包下切换为子库
     * @param point point
     */
    @Before("slave()")
    public void setSlaveDB(JoinPoint point){
        DynamicDataSourceContextHolder.setDB("druid_test_slave");
    }

    /**
     * 子库切换完毕后清除子库信息
     * @param point point
     */
    @After("slave()")
    public void clearSlaveDB(JoinPoint point){
        DynamicDataSourceContextHolder.clearDB();
    }

}