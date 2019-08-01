package com.sike.collect.controller.rest.dashborad;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sike.collect.manager.CiscoApiManager;
/**
 * Dashboard页面
 */
@RestController
public class DashboardController {

	@Autowired
    private CiscoApiManager apiManager;

	@GetMapping({"/dashboard"})
	public  String getDashboard(HttpServletRequest request)throws Exception {
		JsonObject newJsonObject=new JsonObject();
		JsonObject retJsonObject=new JsonObject();
		JsonObject topSummary = getTopSummary(request) ;
	    if (topSummary.get("LoginFail") != null) return retLogHtmlError();
	    else newJsonObject.add("Summary", getTopSummary(request));
		newJsonObject.add("ControlStatus", getControlStatus(request));
		newJsonObject.add("SiteHealthView", getSiteHealthView(request));
		newJsonObject.add("TransportInterfaceDistribution", getTransportInterfaceDistribution(request));
		newJsonObject.add("WANEdgeInventory", getWanEdgeInventory(request));
		newJsonObject.add("WANEdgeHealth", getWanEdgeHealth(request));
		newJsonObject.add("TransportHealth", getTransportHealth(request,null));
		newJsonObject.add("TopApplications", getTopApplications(request));
		newJsonObject.add("ApplicationAwareRouting", getApplicationAwareRouting(request));
		newJsonObject.add("FireWallEnforcement", getFireWallEnforcement(request,"24"));
		retJsonObject.add("Dashborad", newJsonObject);
		return retJsonObject.toString();
    }

	@GetMapping({"/getTopSummary"})
    public  JsonObject getTopSummary(HttpServletRequest request) {
    	String url = "/dataservice/network/connectionssummary";
    	JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
    	try {
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        newJsonObject.add("vSmart",retNewJsonObject(dataArray.get(0)));
        newJsonObject.add("vEdge",retNewJsonObject(dataArray.get(1)));
        newJsonObject.add("vBone",retNewJsonObject(dataArray.get(2)));
        newJsonObject.add("vManage",getTophealthSummary(request));
        newJsonObject.add("Reboot",getTopRebootSummary(request));
        newJsonObject.add("certificate",getTopStatsSummary(request));
		
    	}
    	catch(Exception e) {
    		newJsonObject.addProperty("LoginFail", "-1"); 
    	}
    	return newJsonObject  ;
    }
	public JsonObject retNewJsonObject(JsonElement element) {
		JsonObject retJsonObject= element.getAsJsonObject();
		JsonObject tempObject=new JsonObject();
		JsonElement statusListElement = retJsonObject.getAsJsonArray("statusList").get(0);
		JsonObject statusListElementObject = statusListElement.getAsJsonObject();
		tempObject.addProperty("success", retJsonObject.get("count").getAsInt() - statusListElementObject.get("count").getAsInt());
        tempObject.addProperty("error", statusListElementObject.get("count").getAsInt());
		return tempObject;
	}
	@GetMapping({"/getTophealthSummary"})
    public  JsonObject getTophealthSummary(HttpServletRequest request)throws Exception {
    	String url = "/dataservice/clusterManagement/health/summary";
		JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        JsonElement jsonElement = dataArray.get(0);
        JsonObject jsonObjectDetail = jsonElement.getAsJsonObject();
        newJsonObject.addProperty("count", jsonObjectDetail.get("count").getAsInt());
		return newJsonObject  ;
    }
	@GetMapping({"/getTopRebootSummary"})
    public  JsonObject getTopRebootSummary(HttpServletRequest request)throws Exception {
		String url = "/dataservice/network/issues/rebootcount";
		JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        JsonElement jsonElement = dataArray.get(0);
        JsonObject jsonObjectDetail = jsonElement.getAsJsonObject();
        newJsonObject.addProperty("count", jsonObjectDetail.get("count").getAsInt());
		return newJsonObject ;
    }
	@GetMapping({"/getTopStatsSummary"})
    public  JsonObject getTopStatsSummary(HttpServletRequest request)throws Exception {
    	String url = "/dataservice/certificate/stats/summary";
		JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        JsonElement jsonElement = dataArray.get(0);
        JsonObject jsonObjectDetail = jsonElement.getAsJsonObject();
        newJsonObject.addProperty("warning", jsonObjectDetail.get("warning").getAsInt());
        newJsonObject.addProperty("invalid", jsonObjectDetail.get("invalid").getAsInt());
		return newJsonObject ;
    }
	@GetMapping({"/getControlStatus"})
    public JsonObject getControlStatus(HttpServletRequest request) throws Exception{
		String url = "/dataservice/device/control/count?isCached=true";
		JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray controlStatus = jsonObject.getAsJsonArray("data");
        JsonElement jsonElement = controlStatus.get(0);
        JsonObject controlStatusDetail = jsonElement.getAsJsonObject();
        newJsonObject.addProperty("total", controlStatusDetail.get("count").getAsInt());
        JsonArray dataArray = controlStatusDetail.getAsJsonArray("statusList");
        if(dataArray.size() >0) {
        	newJsonObject.add(removeSpecialString(dataArray.get(0).getAsJsonObject().get("name").getAsString()) ,retCSJsonObject(dataArray.get(0).getAsJsonObject()));
        	newJsonObject.add(removeSpecialString(dataArray.get(1).getAsJsonObject().get("name").getAsString()) ,retCSJsonObject(dataArray.get(1).getAsJsonObject()));
        	newJsonObject.add(removeSpecialString(dataArray.get(2).getAsJsonObject().get("name").getAsString()),retCSJsonObject(dataArray.get(2).getAsJsonObject()));
        }
		return newJsonObject ;
    }
	@GetMapping({"/getSiteHealthView"})
    public JsonObject getSiteHealthView(HttpServletRequest request) throws Exception{
		String url = "/dataservice/device/bfd/sites/summary?isCached=true";
		JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray controlStatus = jsonObject.getAsJsonArray("data");
        JsonElement jsonElement = controlStatus.get(0);
        JsonObject controlStatusDetail = jsonElement.getAsJsonObject();
        newJsonObject.addProperty("total", controlStatusDetail.get("count").getAsInt());
        JsonArray dataArray = controlStatusDetail.getAsJsonArray("statusList");
        if(dataArray.size() >0) {
        	newJsonObject.add(removeSpecialString(dataArray.get(0).getAsJsonObject().get("name").getAsString()) ,retCSJsonObject(dataArray.get(0).getAsJsonObject()));
        	newJsonObject.add(removeSpecialString(dataArray.get(1).getAsJsonObject().get("name").getAsString()) ,retCSJsonObject(dataArray.get(1).getAsJsonObject()));
        	newJsonObject.add(removeSpecialString(dataArray.get(2).getAsJsonObject().get("name").getAsString()) ,retCSJsonObject(dataArray.get(2).getAsJsonObject()));
        }
		return newJsonObject ;
    }
	public JsonObject retCSJsonObject(JsonObject jsonObject) {
        JsonObject retJsonObject = new JsonObject();
        retJsonObject.addProperty("status", jsonObject.get("status").getAsString());
        retJsonObject.addProperty("count", jsonObject.get("count").getAsInt());
		return retJsonObject;
	}
	@GetMapping({"/getTransportInterfaceDistribution"})
    public JsonObject getTransportInterfaceDistribution(HttpServletRequest request) throws Exception{
		String url = "/dataservice/device/tlocutil";
		JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        if(dataArray.size() >0) {
	        newJsonObject.add(removeSpecialString(dataArray.get(0).getAsJsonObject().get("name").getAsString()),retTIDJsonObject(dataArray.get(0).getAsJsonObject()));
	        newJsonObject.add(removeSpecialString("a"+dataArray.get(1).getAsJsonObject().get("name").getAsString()),retTIDJsonObject(dataArray.get(1).getAsJsonObject()));
	        newJsonObject.add(removeSpecialString("a"+dataArray.get(2).getAsJsonObject().get("name").getAsString()),retTIDJsonObject(dataArray.get(2).getAsJsonObject()));
	        newJsonObject.add(removeSpecialString(dataArray.get(3).getAsJsonObject().get("name").getAsString()),retTIDJsonObject(dataArray.get(3).getAsJsonObject()));
        }
		return newJsonObject  ;
    }
	public JsonObject retTIDJsonObject(JsonObject jsonObject) {
        JsonObject retJsonObject = new JsonObject();
        retJsonObject.addProperty("value", jsonObject.get("value").getAsString());
		return retJsonObject;
	}
	@GetMapping({"/getWanEdgeInventory"})
    public JsonObject getWanEdgeInventory(HttpServletRequest request) throws Exception{
		String url = "/dataservice/device/vedgeinventory/summary";
		JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        if(dataArray.size() >0) {
	        newJsonObject.add(removeSpecialString(dataArray.get(0).getAsJsonObject().get("name").getAsString()),retTIDJsonObject(dataArray.get(0).getAsJsonObject()));
	        newJsonObject.add(removeSpecialString(dataArray.get(1).getAsJsonObject().get("name").getAsString()),retTIDJsonObject(dataArray.get(1).getAsJsonObject()));
	        newJsonObject.add(removeSpecialString(dataArray.get(2).getAsJsonObject().get("name").getAsString()),retTIDJsonObject(dataArray.get(2).getAsJsonObject()));
	        newJsonObject.add(removeSpecialString(dataArray.get(3).getAsJsonObject().get("name").getAsString()),retTIDJsonObject(dataArray.get(3).getAsJsonObject()));
        }
		return newJsonObject  ;
    }
	@GetMapping({"/getWanEdgeHealth"})
    public JsonObject getWanEdgeHealth(HttpServletRequest request) throws Exception{
		String url = "/dataservice/device/hardwarehealth/summary?isCached=true";
		JsonObject newJsonObject=new JsonObject();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray controlStatus = jsonObject.getAsJsonArray("data");
        JsonElement jsonElement = controlStatus.get(0);
        JsonObject controlStatusDetail = jsonElement.getAsJsonObject();
        newJsonObject.addProperty("total", controlStatusDetail.get("count").getAsInt());
        JsonArray dataArray = controlStatusDetail.getAsJsonArray("statusList");
        if(dataArray.size() >0) {
        	newJsonObject.add(removeSpecialString(dataArray.get(0).getAsJsonObject().get("name").getAsString()) ,retCSJsonObject(dataArray.get(0).getAsJsonObject()));
        	newJsonObject.add(removeSpecialString(dataArray.get(1).getAsJsonObject().get("name").getAsString()) ,retCSJsonObject(dataArray.get(1).getAsJsonObject()));
        	newJsonObject.add(removeSpecialString(dataArray.get(2).getAsJsonObject().get("name").getAsString()) ,retCSJsonObject(dataArray.get(2).getAsJsonObject()));
        }
		return newJsonObject  ;
    }
	/**
	 * Type: by Loss url = "dataservice/statistics/approute/transport/summary/loss_percentage?limit=5";
	 * Type: by Jitter url = "dataservice/statistics/approute/transport/summary/jitter?limit=5"
	 * Type: by latency url = "dataservice/statistics/approute/transport/summary/latency?limit=5"
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/getTransportHealth"})
    public JsonObject getTransportHealth(HttpServletRequest request,String type) throws Exception{
    	String url = "/dataservice/statistics/approute/transport/summary/loss_percentage?limit=5";
    	if (type != null && type.equals("latency")) url = "/dataservice/statistics/approute/transport/summary/latency?limit=5";
    	else if (type != null && type.equals("jitter")) url = "/dataservice/statistics/approute/transport/summary/jitter?limit=5";
    	JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject summary = jsonElement.getAsJsonObject();
            JsonObject tempObject=new JsonObject();
            tempObject.addProperty("entrytime", summary.get("entry_time").getAsLong() );
            tempObject.addProperty("jitter", summary.get("jitter").getAsString());
            tempObject.addProperty("losspercentage", String.format("%.2f", summary.get("loss_percentage").getAsDouble()) );
            tempObject.addProperty("latency", summary.get("latency").getAsString());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("dataList", newJsonArray);
		return newJsonObject    ;
    }
	@GetMapping({"/getTopApplications"})
    public JsonObject getTopApplications(HttpServletRequest request) throws Exception{
		String url = "/dataservice/statistics/dpi/applications/summary";
		JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject summary = jsonElement.getAsJsonObject();
            JsonObject tempObject=new JsonObject();
            tempObject.addProperty("application", summary.get("application").getAsString());
            tempObject.addProperty("octets", summary.get("octets").getAsInt());
            newJsonArray.add(tempObject);
        }
        newJsonObject.add("dataList", newJsonArray);
		return newJsonObject   ;
    }
    @GetMapping({"/getApplicationAwareRouting"})
    public JsonObject getApplicationAwareRouting(HttpServletRequest request) throws Exception{
		String url = "/dataservice/statistics/approute/tunnels/summary/latency";
		JsonObject newJsonObject=new JsonObject();
    	JsonArray  newJsonArray = new JsonArray();
        Map<String, String> params = new HashMap<>();
        String resultText =  apiManager.getTextFromApi(url, request, params);
        JsonObject jsonObject = new Gson().fromJson(resultText,JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement jsonElement : dataArray) {
            JsonObject summary = jsonElement.getAsJsonObject();
            JsonObject tempObject=new JsonObject();
            tempObject.addProperty("TunnelEndpoints", summary.get("name").getAsString());
            tempObject.addProperty("AvgLatency", summary.get("latency").getAsFloat());
            tempObject.addProperty("AvgLoss", summary.get("loss_percentage").getAsFloat());
            tempObject.addProperty("AvgJitter", summary.get("jitter").getAsFloat());
            newJsonArray.add(tempObject);
        } 
        newJsonObject.add("dataList", newJsonArray);
		return newJsonObject   ;
    }
    @GetMapping({"/getFireWallEnforcement"})
    public JsonObject getFireWallEnforcement(HttpServletRequest request, @RequestParam(defaultValue = "24")String time) throws Exception{
    	String query  = "%7B%22aggregation%22%3A%7B%22metrics%22%3A%5B%7B%22property%22%3A%22fw_total_insp_count%22%2C%22type%22%3A%22sum%22%2C%22order%22%3A%22desc%22%7D%5D%2C%22histogram%22%3A%7B%22property%22%3A%22entry_time%22%2C%22type%22%3A%22minute%22%2C%22interval%22%3A30%2C%22order%22%3A%22asc%22%7D%7D%2C%22query%22%3A%7B%22condition%22%3A%22AND%22%2C%22rules%22%3A%5B%7B%22value%22%3A%5B%2212%22%5D%2C%22field%22%3A%22entry_time%22%2C%22type%22%3A%22date%22%2C%22operator%22%3A%22last_n_hours%22%7D%2C%7B%22value%22%3A%5B%22total%22%5D%2C%22field%22%3A%22type%22%2C%22type%22%3A%22string%22%2C%22operator%22%3A%22in%22%7D%5D%7D%7D";
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
		return newJsonObject   ;
    }
    /**
     * 去除特殊字符
     * @param oldStr
     * @return
     */
    public String removeSpecialString(String oldStr) {
        return oldStr.replaceAll("\\p{Punct}","").replaceAll("\\s*", "");
    }
    /**
     * 登录失败返回
     * @return
     */
    public String retLogHtmlError( ) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("<html>");
    	sb.append("<body>");
    	sb.append("login error");
    	sb.append("</body>");
    	sb.append("</html>");
        return sb.toString();
    }
  
    
}
