package com.money.intercept;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class RequestLogIntercept implements HandlerInterceptor{

    private static final ThreadLocal<Long> TIME_LOGGER=new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put("traceId", UUID.randomUUID().toString());
        log.info("request params:{}",request.getQueryString());
        TIME_LOGGER.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("cost:{}ms",(System.currentTimeMillis()-TIME_LOGGER.get()));
        TIME_LOGGER.remove();
    }
}
