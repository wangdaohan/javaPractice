package com.patrick.disruptoromssettlement.api;

import com.patrick.disruptoromssettlement.bean.res.CounterRes;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public CounterRes exceptionHandler(
            HttpServletRequest request,Exception e
    ){
        e.printStackTrace();
        log.error(e);
        return new CounterRes(CounterRes.FAIL,
                "发生错误",
                null);
    }

}
