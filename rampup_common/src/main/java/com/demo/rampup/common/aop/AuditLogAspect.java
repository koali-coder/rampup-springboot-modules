package com.demo.rampup.common.aop;

import com.demo.rampup.common.annotation.AuditLog;
import com.demo.rampup.common.model.RestData;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouyw
 * @date 2022-05-07
 * @describe com.demo.rampup.common.aop
 */
@Slf4j
@Aspect
@Component
public class AuditLogAspect {

    @Pointcut("@annotation(com.demo.rampup.common.annotation.AuditLog)")
    private void logPointCut(){

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        LocalDateTime executeTime = LocalDateTime.now();
        Object result = point.proceed();
        try {
            saveLog(point, executeTime);
        } catch (Exception e) {
            log.error("around save log ", e);
        }
        return result;
    }

    private void saveLog(ProceedingJoinPoint point, LocalDateTime executeTime) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        AuditLog auditLog = method.getAnnotation(AuditLog.class);
        // 注解上的描述
        String value = auditLog.value();

        // 请求的 类名、方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();

        try{
            // 请求的参数
            Object[] args = point.getArgs();
            List<String> list = new ArrayList<>();
            for (Object o : args) {
                list.add(new Gson().toJson(o));
            }

            // 返回的数据
            RestData result = (RestData) point.proceed();
        }catch (Exception e){
            log.error("audit save log ", e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}
