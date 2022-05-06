package com.demo.rampup.admin.handle;

import com.demo.rampup.common.model.RestData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhouyw
 * @date 2021-11-26
 * @describe com.nesun.smart.parking.handler
 */
@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    /**
     *  拦截Exception类的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestData exceptionHandler(Exception e){
        return RestData.failed(e.getMessage());
    }

    /**
     *  拦截HttpMessageNotReadableException类的异常
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public RestData messageNotReadableExceptionHandler(Exception e){
        return RestData.failed(e.getMessage());
    }

}
