package com.sike.collect.controller.rest.monitor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hiveel.core.model.rest.BasicRestCode;
import com.hiveel.core.model.rest.Rest;
import com.sike.collect.manager.CiscoApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NetworkController {

    @Autowired
    private CiscoApiManager apiManager;

    @GetMapping({"/monitor/network"})
    public String devices(HttpServletRequest request, String groupId) throws Exception {
        String devicesUrl = "/dataservice/group/devices";
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        String resultText = apiManager.getTextFromApi(devicesUrl, request, params);
        return apiManager.parseJsonFromApi(resultText).toString();
    }


}
