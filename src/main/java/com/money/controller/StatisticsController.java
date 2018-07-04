package com.money.controller;


import com.money.domain.OfferRate;
import com.money.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offer")
@Slf4j
public class StatisticsController {


    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/statistics",method = RequestMethod.POST)
    public Map<String,List<OfferRate>> getAllOfferRate(){
        return cacheService.getAllOfferRate();
    }
}
