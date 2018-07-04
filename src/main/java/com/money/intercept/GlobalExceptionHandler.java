package com.money.intercept;

import com.money.dto.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult handleException(Exception exp){
        log.error(exp.getMessage(),exp);
        BaseResult baseResult=new BaseResult();
        baseResult.setCode(500);
        baseResult.setMsg("server exception.");
        return baseResult;
    }
}
