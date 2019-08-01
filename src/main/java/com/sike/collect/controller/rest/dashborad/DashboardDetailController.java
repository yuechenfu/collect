package com.sike.collect.controller.rest.dashborad;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sike.collect.manager.CiscoApiManager;


/**
 * Dashboard 详细页面
 */
@RestController
public class DashboardDetailController {

	@Autowired
    private CiscoApiManager apiManager;


	@GetMapping({"/dashboard/vsmart"})
	public  String getvSmart(HttpServletRequest request)throws Exception {
		String url = "/dataservice/device/reachable?personality=vsmart";
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject vmart = jsonElement.getAsJsonObject();
            JsonObject tempObject = new JsonObject();
            tempObject.addProperty("Reachability", vmart.get("reachability").getAsString());
            tempObject.addProperty("Hostname", vmart.get("host-name").getAsString());
            tempObject.addProperty("SystemIP", vmart.get("system-ip").getAsString());
            tempObject.addProperty("SiteID", vmart.get("site-id").getAsString());
            tempObject.addProperty("DeviceModel", vmart.get("device-model").getAsString());
            tempObject.addProperty("BFD", "");//未找到对应值
            tempObject.addProperty("OMP", vmart.get("ompPeers").getAsString());
            tempObject.addProperty("Control", vmart.get("controlConnections").getAsString());
            tempObject.addProperty("Version", vmart.get("version").getAsString());
            tempObject.addProperty("ChassisNumber/ID", vmart.get("uuid").getAsString());
            tempObject.addProperty("SerialNumber", vmart.get("board-serial").getAsString());
            tempObject.addProperty("LastUpdated", vmart.get("lastupdated").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("vsmart", newJsonArray);
		return newJsonObject.toString()  ;
    }
	/**
	 * reachability : reachable/unreachable
	 * @param request
	 * @param reachability
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/vedge"})
	public  String getVedge(HttpServletRequest request,String reachability)throws Exception {
		String url = "/dataservice/device/"+reachability+"?personality=vedge";
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject vmart = jsonElement.getAsJsonObject();
            JsonObject tempObject = new JsonObject();
            tempObject.addProperty("Reachability", vmart.get("reachability").getAsString());
            tempObject.addProperty("Hostname", vmart.get("host-name").getAsString());
            tempObject.addProperty("SystemIP", vmart.get("system-ip").getAsString());
            tempObject.addProperty("SiteID", vmart.get("site-id").getAsString());
            tempObject.addProperty("DeviceModel", vmart.get("device-model").getAsString());
            tempObject.addProperty("BFD", "");//未找到对应值
            tempObject.addProperty("OMP", vmart.get("ompPeers").getAsString());
            tempObject.addProperty("Control", vmart.get("controlConnections").getAsString());
            tempObject.addProperty("Version", vmart.get("version").getAsString());
            tempObject.addProperty("ChassisNumberID", vmart.get("uuid").getAsString());
            tempObject.addProperty("SerialNumber", vmart.get("board-serial").getAsString());
            tempObject.addProperty("LastUpdated", vmart.get("lastupdated").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("vedge", newJsonArray);
		return newJsonObject.toString()  ;
    }

	@GetMapping({"/dashboard/vbond"})
	public  String getVbond(HttpServletRequest request )throws Exception {
		String url = "/dataservice/device/reachable?personality=vbond";
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject vmart = jsonElement.getAsJsonObject();
            JsonObject tempObject = new JsonObject();
            tempObject.addProperty("Reachability", vmart.get("reachability").getAsString());
            tempObject.addProperty("Hostname", vmart.get("host-name").getAsString());
            tempObject.addProperty("SystemIP", vmart.get("system-ip").getAsString());
            tempObject.addProperty("SiteID", vmart.get("site-id").getAsString());
            tempObject.addProperty("DeviceModel", vmart.get("device-model").getAsString());
            tempObject.addProperty("BFD", "");//未找到对应值
            tempObject.addProperty("OMP", vmart.get("ompPeers").getAsString());
            tempObject.addProperty("Control", vmart.get("controlConnections").getAsString());
            tempObject.addProperty("Version", vmart.get("version").getAsString());
            tempObject.addProperty("ChassisNumberID", vmart.get("uuid").getAsString());
            tempObject.addProperty("SerialNumber", vmart.get("board-serial").getAsString());
            tempObject.addProperty("LastUpdated", vmart.get("lastupdated").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("vbond", newJsonArray);
		return newJsonObject.toString()  ;
    }

	@GetMapping({"/dashboard/vmanage"})
	public  String getVManage(HttpServletRequest request )throws Exception {
		String url = "/dataservice/clusterManagement/health/details";
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject vmart = jsonElement.getAsJsonObject();
            JsonObject tempObject = new JsonObject();
            tempObject.addProperty("DeviceIP", vmart.get("deviceIP").getAsString());
            tempObject.addProperty("Reporters", vmart.get("reporters").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("vmanage", newJsonArray);
		return newJsonObject.toString()  ;
    }
	/**
	 * status : "",warning
	 * @param request
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/certificate"})
	public  String getCertificate(HttpServletRequest request,String status)throws Exception {
		String url = "/dataservice/certificate/stats/detail";
		if(status !=null && !status.isEmpty()) url = url + "?status="+status;
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject vmart = jsonElement.getAsJsonObject();
            JsonObject tempObject = new JsonObject();
            tempObject.addProperty("ControllerType", vmart.get("deviceType").getAsString());
            tempObject.addProperty("Hostname", vmart.get("host-name").getAsString());
            tempObject.addProperty("SystemIP", vmart.get("local-system-ip").getAsString());
            tempObject.addProperty("SerialNo", vmart.get("serialNumber").getAsString());
            tempObject.addProperty("ExpirationDate", vmart.get("expirationDate").getAsString());
            tempObject.addProperty("ExpirationStatus", vmart.get("expirationStatus").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("certificate", newJsonArray);
		return newJsonObject.toString()  ;
    }
	/**
	 * state : up/down
	 * @param request
	 * @param state
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/controlstatus"})
	public  String getControlStatus(HttpServletRequest request,String state)throws Exception {
		String url = "/dataservice/device/control/networksummary?state="+state;
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject vmart = jsonElement.getAsJsonObject();
            JsonObject tempObject = new JsonObject();
            tempObject.addProperty("Hostname", vmart.get("host-name").getAsString());
            tempObject.addProperty("Reachability", vmart.get("reachability").getAsString());
            tempObject.addProperty("SystemIP", vmart.get("local-system-ip").getAsString());
            tempObject.addProperty("SiteID", vmart.get("site-id").getAsString());
            tempObject.addProperty("DeviceModel", vmart.get("device-model").getAsString());
            tempObject.addProperty("ControlConnections", vmart.get("controlConnectionsToVsmarts").getAsString());
            tempObject.addProperty("LastUpdated", vmart.get("lastupdated").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("controlStatus", newJsonArray);
		return newJsonObject.toString()  ;
    }

	/**
	 * state : siteup/sitepartial/sitedown/
	 * @param request
	 * @param state
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/sitehealthview"})
	public  String getSiteHealthView(HttpServletRequest request,String state)throws Exception {
		String url = "/dataservice/device/bfd/sites/detail?state="+state;
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject vmart = jsonElement.getAsJsonObject();
            JsonObject tempObject = new JsonObject();
            tempObject.addProperty("Hostname", vmart.get("host-name").getAsString());
            tempObject.addProperty("Reachability", vmart.get("reachability").getAsString());
            tempObject.addProperty("SystemIP", vmart.get("local-system-ip").getAsString());
            tempObject.addProperty("SiteID", vmart.get("site-id").getAsString());
            tempObject.addProperty("BFDSessions", vmart.get("bfdSessions").getAsString());
            tempObject.addProperty("LastUpdated", vmart.get("lastupdated").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("siteHealthView", newJsonArray);
		return newJsonObject.toString()  ;
    }

	/**
	 * util : lessthan10mbps/10mbps100mbps/100mbps500mbps/greaterthan500mbps
	 * @param request
	 * @param state
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/tid"})
	public  String getTransportInterfaceDistribution(HttpServletRequest request,String util)throws Exception {
		String url = "/dataservice/device/tlocutil/detail?util="+util;
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject vmart = jsonElement.getAsJsonObject();
            JsonObject tempObject = new JsonObject();
            tempObject.addProperty("SystemIP", vmart.get("system-ip").getAsString());
            tempObject.addProperty("Interface", vmart.get("interface").getAsString());
            tempObject.addProperty("Average", vmart.get("average").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("transportInterfaceDistribution", newJsonArray);
		return newJsonObject.toString()  ;
    }

	@GetMapping({"/dashboard/tid/vpu"})
	public  String getViewPercentUtilization(HttpServletRequest request)throws Exception {
		String url = "/dataservice/statistics/interface/ccapacity/distribution";
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonObject newJsonObject = new JsonObject();
        newJsonObject.add("data", jsonObject.get("data"));
        newJsonObject.add("distribution", changeVpuJsonformat(jsonObject.get("distribution").getAsJsonObject()));
		return newJsonObject.toString() ;
    }
	
	public JsonObject changeVpuJsonformat(JsonObject oldJsonObject) {
		JsonObject newJsonObject = new JsonObject();
		newJsonObject.addProperty("a5075", oldJsonObject.get("50-75").toString());
		newJsonObject.addProperty("a025", oldJsonObject.get("0-25").toString());
		newJsonObject.addProperty("a75100", oldJsonObject.get("75-100").toString());
		newJsonObject.addProperty("uncategorized", oldJsonObject.get("uncategorized").toString());
		newJsonObject.addProperty("a2550", oldJsonObject.get("25-50").toString());
		newJsonObject.addProperty("a>100", oldJsonObject.get(">100").toString());
		return newJsonObject ;
	}
	
	/**
	 * status :authorized/deployed/staging
	 * @param request
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/wanedgeinventory"})
	public  String getWANEdgeInventory(HttpServletRequest request,String status)throws Exception {
		String url = "/dataservice/device/vedgeinventory/detail";
		if(status !=null && !status.isEmpty()) url = url + "?status="+status;
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);;
		return jsonObject.toString()  ;
    }
	/**
	 * state : normal/warning/error
	 * @param request
	 * @param state
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/wanedgehealth"})
	public  String getWANEdgeHealth(HttpServletRequest request,String state)throws Exception {
		String url = "/dataservice/device/hardwarehealth/detail?state="+state;
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);;
		return jsonObject.toString()  ;
    }
	/**
	 * 
	 * @param request
	 * @param type :loss_percentage/latency/jitter
	 * @param time
	 * @return chart/detail返回数据相同
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/transporthealth"})
	public  String getTransportHealth(HttpServletRequest request,@RequestParam(defaultValue = "loss_percentage")String type,@RequestParam(defaultValue = "24")String time)throws Exception {
		String query = "%7B%22query%22%3A%7B%22condition%22%3A%22AND%22%2C%22rules%22%3A%5B%7B%22value%22%3A%5B%22"+time+"%22%5D%2C%22field%22%3A%22entry_time%22%2C%22type%22%3A%22date%22%2C%22operator%22%3A%22last_n_hours%22%7D%5D%7D%7D";
		String url = "/dataservice/statistics/approute/transport/summary/"+type+"?limit=5";
        Map<String, String> params = new HashMap<>();
        params.put("query", URLDecoder.decode(query));
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
		return jsonObject.toString()  ;
    }
	/**
	 * 
	 * @param request
	 * @param time
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/dashboard/topapplications"})
	public  String getTopApplications(HttpServletRequest request,@RequestParam(defaultValue = "24")String time)throws Exception {
		String query  = "%7B%22query%22%3A%7B%22condition%22%3A%22AND%22%2C%22rules%22%3A%5B%7B%22value%22%3A%5B%22"+time+"%22%5D%2C%22field%22%3A%22entry_time%22%2C%22type%22%3A%22date%22%2C%22operator%22%3A%22last_n_hours%22%7D%5D%7D%7D";
		String url = "/dataservice/statistics/dpi/applications/summary?limit=25";
        Map<String, String> params = new HashMap<>();
        params.put("query", URLDecoder.decode(query));
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
		return jsonObject.toString()  ;
    }
	/**
	 * 列表
	 * @param request
	 * @param time
	 * @return
	 * @throws Exception
	 */
	@PostMapping({"/dashboard/applicationawarerouting"})
	public  String aarList(HttpServletRequest request,@RequestBody String query)throws Exception {
		 String url = "/dataservice/statistics/approute/fec/aggregation";
		String text = apiManager.postTextFromApi(url,request,query);
        return apiManager.parseJsonFromApi(text).toString();
    }
	
	@PostMapping({"/dashboard/applicationawarerouting/aggregation"})
	public  String aarAggregation(HttpServletRequest request,@RequestBody String query)throws Exception {
		String url = "/dataservice/statistics/approute/fec/aggregation";
		String text = apiManager.postTextFromApi(url,request,query);
        return apiManager.parseJsonFromApi(text).toString();
    }
	
	 
	
	@GetMapping({"/dashboard/fireWallenforcement/dropped"})
    public String getFireWallEnforcement_dropped(HttpServletRequest request,@RequestParam(defaultValue = "24")String time) throws Exception{
    	String query ="%7B%22aggregation%22%3A%7B%22metrics%22%3A%5B%7B%22property%22%3A%22fw_total_drop_count%22%2C%22type%22%3A%22sum%22%2C%22order%22%3A%22desc%22%7D%5D%2C%22histogram%22%3A%7B%22property%22%3A%22entry_time%22%2C%22type%22%3A%22minute%22%2C%22interval%22%3A30%2C%22order%22%3A%22asc%22%7D%7D%2C%22query%22%3A%7B%22condition%22%3A%22AND%22%2C%22rules%22%3A%5B%7B%22value%22%3A%5B%22"+time+"%22%5D%2C%22field%22%3A%22entry_time%22%2C%22type%22%3A%22date%22%2C%22operator%22%3A%22last_n_hours%22%7D%2C%7B%22value%22%3A%5B%22total%22%5D%2C%22field%22%3A%22type%22%2C%22type%22%3A%22string%22%2C%22operator%22%3A%22in%22%7D%5D%7D%7D";
    	String url = "/dataservice/statistics/fwall/aggregation";
		JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        params.put("query", URLDecoder.decode(query));
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        if(dataArray.size() >0 ) {
        	for (JsonElement jsonElement : dataArray) {
	            JsonObject summary = jsonElement.getAsJsonObject();
	            JsonObject tempObject=new JsonObject();
	            tempObject.addProperty("count", summary.get("count").getAsString());
	            tempObject.addProperty("entrytime", summary.get("entry_time").getAsFloat());
	            tempObject.addProperty("fwtotaldropcount", summary.get("fw_total_drop_count").getAsFloat());
	            newJsonArray.add(tempObject);
        	}
        }
        newJsonObject.add("dataList", newJsonArray);
		return newJsonObject.toString()   ;
    }
	
	@GetMapping({"/dashboard/fireWallenforcement/inspected"})
    public String getFireWallEnforcement_inspected(HttpServletRequest request,@RequestParam(defaultValue = "24")String time) throws Exception{
    	String query  = "%7B%22aggregation%22%3A%7B%22metrics%22%3A%5B%7B%22property%22%3A%22fw_total_insp_count%22%2C%22type%22%3A%22sum%22%2C%22order%22%3A%22desc%22%7D%5D%2C%22histogram%22%3A%7B%22property%22%3A%22entry_time%22%2C%22type%22%3A%22minute%22%2C%22interval%22%3A30%2C%22order%22%3A%22asc%22%7D%7D%2C%22query%22%3A%7B%22condition%22%3A%22AND%22%2C%22rules%22%3A%5B%7B%22value%22%3A%5B%22"+time+"%22%5D%2C%22field%22%3A%22entry_time%22%2C%22type%22%3A%22date%22%2C%22operator%22%3A%22last_n_hours%22%7D%2C%7B%22value%22%3A%5B%22total%22%5D%2C%22field%22%3A%22type%22%2C%22type%22%3A%22string%22%2C%22operator%22%3A%22in%22%7D%5D%7D%7D";
    	String url = "/dataservice/statistics/fwall/aggregation";
		JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        params.put("query", URLDecoder.decode(query));
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        if(dataArray.size() >0 ) {
        	for (JsonElement jsonElement : dataArray) {
	            JsonObject summary = jsonElement.getAsJsonObject();
	            JsonObject tempObject=new JsonObject();
	            tempObject.addProperty("count", summary.get("count").getAsString());
	            tempObject.addProperty("entrytime", summary.get("entry_time").getAsFloat());
	            tempObject.addProperty("fwtotalinspcount", summary.get("fw_total_insp_count").getAsFloat());
	            newJsonArray.add(tempObject);
        	}
        }
        newJsonObject.add("dataList", newJsonArray);
		return newJsonObject.toString()   ;
    }
}
