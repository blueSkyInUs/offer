package com.money.controller;

import com.money.dto.BaseResult;
import com.money.service.CacheService;
import com.money.service.OfferCallbackService;
import com.money.task.StatisticsEveryDayTask;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/offer")
@Slf4j
public class OfferCallbackController {


    @Autowired
    private OfferCallbackService offerCallbackService;

    @Autowired
    private StatisticsEveryDayTask statisticsEveryDayTask;

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/callback")
    @ResponseBody
    public BaseResult callback(HttpServletRequest httpServletRequest ){
        String queryString=httpServletRequest.getQueryString();
        String ip= StringUtils.isEmpty(httpServletRequest.getHeader("X-Real-IP"))?httpServletRequest.getRemoteAddr():httpServletRequest.getHeader("X-Real-IP");
         offerCallbackService.callback(queryString,ip);
         return new BaseResult();
    }

    @RequestMapping("/url/{campaignCode}")
    @SneakyThrows
    public void obtain(HttpServletRequest httpServletRequest, @PathVariable("campaignCode") String campaignCode, HttpServletResponse response){
        String url= offerCallbackService.obtain(httpServletRequest,campaignCode);
        log.info("redirect url:{}",url);
        response.sendRedirect(url);
    }

    @RequestMapping(value = "/cache/refresh",method = RequestMethod.POST)
    @SneakyThrows
    @ResponseBody
    public BaseResult cache(){
        cacheService.init();
        return new BaseResult();

    }


    @RequestMapping("/test/task")
    @ResponseBody
    public void testTask(){
        statisticsEveryDayTask.statistics();
    }

    @RequestMapping("/test/task/hour")
    @ResponseBody
    public void testTaskHour(){
        statisticsEveryDayTask.statisticsCampaginInHour();
    }


}
