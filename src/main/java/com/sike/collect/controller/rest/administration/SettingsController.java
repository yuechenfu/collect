package com.sike.collect.controller.rest.administration;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hiveel.core.log.util.LogUtil;
import com.hiveel.core.util.MemcachedUtil;
import com.hiveel.core.util.ThreadUtil;
import com.sike.collect.manager.CiscoApiManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class SettingsController {
    static Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    private CiscoApiManager apiManager;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(15);

    private final static int EXPIRE = 60;

    private final static String RESULT_KEY = "SettingsController.getSettings";

    private static Map<String, String> URL_MAP = new HashMap<>();

    static {
        URL_MAP.put("Setting", "/dataservice/device/action/ztp/upgrade/setting");
        URL_MAP.put("vBond", "/dataservice/settings/configuration/device");
        URL_MAP.put("Software_Install_Timeout ", "/dataservice/settings/configuration/softwareMaintenance");
        URL_MAP.put("Certificate", "/dataservice/settings/configuration/certificate");
        URL_MAP.put("Client_Session_Timeout", "/dataservice/settings/clientSessionTimeout");
        URL_MAP.put("Organization_Name", "/dataservice/settings/configuration/organization");
        URL_MAP.put("SmartAccount", "/dataservice/settings/configuration/smartAccount");
        URL_MAP.put("Maintenance_Window", "/dataservice/settings/configuration/maintenanceWindow");
        URL_MAP.put("Call_Home", "/dataservice/settings/configuration/callHome");
        URL_MAP.put("Google_Map_API_KeyMaps_API_Key", "/dataservice/settings/configuration/googleMapKey");
        URL_MAP.put("Credentials", "/dataservice/settings/configuration/credentials");
        URL_MAP.put("Statistics_Configuration", "/dataservice/management/statsconfig");
        URL_MAP.put("Controller", "/dataservice/device/action/install/devices/controller");
        URL_MAP.put("Email_Notifications", "/dataservice/settings/configuration/emailNotificationSettings");
        URL_MAP.put("Controller_Certificate_Authorization", "/dataservice/settings/configuration/vedgecloud");
        URL_MAP.put("Statistics_Database_Configuration", "/dataservice/management/elasticsearch/index/size");
        URL_MAP.put("Data_Stream", "/dataservice/settings/configuration/vmanagedatastream");
        URL_MAP.put("vAnalytics", "/dataservice/settings/configuration/analytics");
        URL_MAP.put("Web_Server_Certificate", "/dataservice/setting/configuration/webserver/certificate");
        URL_MAP.put("Tenancy_Mode", "/dataservice/clusterManagement/tenancy/mode");
        URL_MAP.put("csrproperties", "/dataservice/settings/configuration/certificate/csrproperties");
        URL_MAP.put("Enforce_Software_Version", "/dataservice/device/action/software/ztp/version");
        URL_MAP.put("enterpriserootca", "/dataservice/settings/configuration/certificate/enterpriserootca");
        URL_MAP.put("Identity_Provider_Settings", "/dataservice/settings/configuration/identityProvider");
        URL_MAP.put("vmanage", "/dataservice/device/action/install/devices/vmanage");
        URL_MAP.put("Statistics_Setting", "/dataservice/statistics/settings/status");
        URL_MAP.put("Banner", "/dataservice/settings/configuration/banner");
        URL_MAP.put("Reverse_Proxy", "/dataservice/settings/configuration/reverseproxy");
        URL_MAP.put("Cloudexpress","/dataservice/settings/configuration/cloudx");
    }

    @GetMapping({"/administration/settings"})
    public String getSettings(HttpServletRequest request) throws Exception {
        JsonObject newJsonObject = new JsonObject();
        JsonObject retJsonObject = new JsonObject();
        CountDownLatch downLatch = new CountDownLatch(URL_MAP.size());
        URL_MAP.keySet().stream().parallel().forEach(name -> {
            String url = URL_MAP.get(name);
            threadPool.execute(() -> {
                String resultText = "";
                try {
                    resultText = apiManager.getTextFromApi(url, request, new HashMap<>());
                    if (resultText.startsWith("[") && resultText.endsWith("]")) {
                        JsonArray jsonArray = new Gson().fromJson(resultText, JsonArray.class);
                        newJsonObject.add(name, jsonArray);
                    } else if (resultText.startsWith("{") && resultText.endsWith("}")) {
                        JsonObject jsonObject = new Gson().fromJson(resultText, JsonObject.class);
                        newJsonObject.add(name, jsonObject);
                    } else {
                        logger.error("can't parse resultText:", resultText);
                    }

                } catch (Exception e) {
                    logger.error("request err url:" + url + " resultText:" + resultText, e);
                    newJsonObject.add(name, new JsonObject());
                } finally {
                    downLatch.countDown();
                }
            });
        });
        downLatch.await(30, TimeUnit.SECONDS);
        retJsonObject.add("settings", newJsonObject);
        retJsonObject.addProperty("code", "success");
        String result = retJsonObject.toString();
        return result;
    }

}
