package com.sike.collect.controller.rest.monitor;

import com.sike.collect.manager.CiscoApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AlarmsController {

    @Autowired
    private CiscoApiManager apiManager;

    @PostMapping({"/monitor/alarms"})
    public String devices(HttpServletRequest request, @RequestBody String query) throws Exception {
        String devicesUrl = "/dataservice/alarms";
        String text = apiManager.postTextFromApi(devicesUrl,request,query);
        return apiManager.parseJsonFromApi(text).toString();
    }


    @PostMapping({"/monitor/alarms/aggregation"})
    public String aggregation(HttpServletRequest request, @RequestBody String query) throws Exception {
        String devicesUrl = "/dataservice/alarms/aggregation";
        String text = apiManager.postTextFromApi(devicesUrl,request,query);
        return apiManager.parseJsonFromApi(text).toString();
    }


}
