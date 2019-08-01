package com.sike.collect.manager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hiveel.core.exception.FailException;
import com.hiveel.core.log.util.LogUtil;
import com.hiveel.core.util.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@Component
public class CiscoApiManager {

    @Value("${cisco.apiAddr}")
    private String baseApiServiceAddr;

    public JsonObject parseJsonFromApi(String resultText) throws Exception {
        JsonObject jsonObject = new Gson().fromJson(resultText, JsonObject.class);
        JsonObject result = new JsonObject();
        JsonElement data = jsonObject.get("data");
        result.add("data", data);
        result.addProperty("code", "SUCCESS");
        result.addProperty("msg", "ok");
        return result;
    }

    public String getTextFromApi(String url, HttpServletRequest request, Map<String, String> requestParams) throws Exception {
        RequestBuilder requestBuilder = RequestBuilder.get(baseApiServiceAddr + url);
        if (requestParams != null && !requestParams.isEmpty()) {
            requestParams.keySet().stream().forEach(paramName -> {
                Object paramValue = requestParams.get(paramName);
                requestBuilder.addParameter(paramName, String.valueOf(paramValue));
            });
        }
        setHeader(request, requestBuilder);
        String result = requestCiscoApiTextByHttps(requestBuilder.build());
        if (StringUtils.isEmpty(result)) {
            throw new FailException("connection fail");
        }
        return result;
    }

    public String postTextFromApi(String url, HttpServletRequest request, String query) throws Exception {
        RequestBuilder requestBuilder = RequestBuilder.post(baseApiServiceAddr + url);
        setHeader(request, requestBuilder);
        HttpEntity httpEntity = new StringEntity(query);
        requestBuilder.setEntity(httpEntity);
        String result = requestCiscoApiTextByHttps(requestBuilder.build());
        if (StringUtils.isEmpty(result)) {
            throw new FailException("connection fail");
        }
        return result;
    }

    public void setHeader(HttpServletRequest request, RequestBuilder requestBuilder) {
        Enumeration<?> enu = request.getHeaderNames();
        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            String value = request.getHeader(name);
            if (value != null) {
                requestBuilder.addHeader(name, value);
            }

        }
    }

    public String requestCiscoApiTextByHttps(HttpUriRequest httpUriRequest) throws IOException {
        httpUriRequest.removeHeaders(HTTP.CONTENT_LEN);
        String responseText = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
            client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            response = client.execute(httpUriRequest);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) response.close();
                if (client != null) client.close();
            } catch (IOException e) {
            }
        }
        return responseText;
    }


    public String getCiscoCookieJob(String userName, String password) {
        String ciscoCookie = null;
        RequestBuilder requestBuilder = RequestBuilder.post(baseApiServiceAddr + "/j_security_check");
        requestBuilder.addParameter("j_username", "devnetuser");
        requestBuilder.addParameter("j_password", "Cisco123!");
        HttpUriRequest httpUriRequest = requestBuilder.build();

        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
            client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            response = client.execute(httpUriRequest);
            HttpEntity entity = response.getEntity();
            Header[] headers = response.getHeaders("Set-Cookie");
            if (headers.length > 0) {
                Header header = headers[0];
                String command = header.getValue();
                String[] cx = command.split(";");
                for (String content : cx) {
                    if (content.startsWith("JSESSIONID=")) {
                        ciscoCookie = content.replace("JSESSIONID=", "");
                        return ciscoCookie;
                    }
                }
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            LogUtil.debug(e);
            LogUtil.error("请求 " + httpUriRequest.getURI().toString() + " 异常: " + e.getMessage());
        } finally {
            try {
                if (response != null) response.close();
                if (client != null) client.close();
            } catch (IOException e) {
            }
        }
        return ciscoCookie;
    }



}
