package com.sike.collect.controller.rest.monitor;

import com.sike.collect.manager.CiscoApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EventsController {

    @Autowired
    private CiscoApiManager apiManager;

    @PostMapping({"/monitor/event"})
    public String devices(HttpServletRequest request, @RequestBody String query) throws Exception {
        String devicesUrl = "/dataservice/event";
        String text = apiManager.postTextFromApi(devicesUrl,request,query);
        return apiManager.parseJsonFromApi(text).toString();
    }


    @PostMapping({"/monitor/event/aggregation"})
    public String aggregation(HttpServletRequest request, @RequestBody String query) throws Exception {
        String devicesUrl = "/dataservice/event/aggregation";
        String text = apiManager.postTextFromApi(devicesUrl,request,query);
        return apiManager.parseJsonFromApi(text).toString();
    }


}
