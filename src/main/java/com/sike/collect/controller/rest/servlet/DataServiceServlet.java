package com.sike.collect.controller.rest.servlet;

import com.google.gson.Gson;
import com.hiveel.core.model.rest.Rest;
import com.sike.collect.manager.CiscoApiManager;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;

@Component
public class DataServiceServlet extends HttpServlet {
    @Value("${cisco.apiAddr}")
    private String baseApiServiceAddr;
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Autowired
    private CiscoApiManager ciscoApiManager;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (!StringUtils.isEmpty(contextPath) && uri.startsWith(contextPath)) {
            uri = uri.replace(contextPath, "");
        }
        String method = req.getMethod();
        RequestBuilder requestBuilder = null;
        switch (method) {
            case "POST":
                requestBuilder = RequestBuilder.post(baseApiServiceAddr + uri);
                setRequstBody(req, requestBuilder);
                break;
            case "GET":
                requestBuilder = RequestBuilder.get(baseApiServiceAddr + uri);
                break;
            case "PUT":
                requestBuilder = RequestBuilder.put(baseApiServiceAddr + uri);
                break;
            case "DELETE":
                requestBuilder = RequestBuilder.delete(baseApiServiceAddr + uri);
                break;
            case "OPTIONS":
                requestBuilder = RequestBuilder.options(baseApiServiceAddr + uri);
                break;
            case "TRACE":
                requestBuilder = RequestBuilder.trace(baseApiServiceAddr + uri);
                break;
        }
        ciscoApiManager.setHeader(req,requestBuilder);
        Enumeration<?> enu = req.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            String value = req.getParameter(name);
            if (value != null) {
                requestBuilder.addParameter(name, value);
            }
        }
        String result = ciscoApiManager.requestCiscoApiTextByHttps(requestBuilder.build());
        resp.setContentType("application/json");
        setResponseAccess(resp);
        PrintWriter pw = resp.getWriter();
        pw.println(result);
    }

    private void setResponseAccess(HttpServletResponse response) {
        // 允许该域发起跨域请求
        response.setHeader("Access-Control-Allow-Origin", "*");//*允许任何域
        // 允许的外域请求方式
        response.setHeader("Access-Control-Allow-Methods", "*");
        // 在999999秒内，不需要再发送预检验请求，可以缓存该结果
        response.setHeader("Access-Control-Max-Age", "999999");
        // 允许跨域请求包含某请求头,x-requested-with请求头为异步请求
        response.setHeader("Access-Control-Allow-Headers",
                "x-requested-with");
    }

    private void setRequstBody(HttpServletRequest request, RequestBuilder requestBuilder) throws IOException {
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            if (inputStream != null) {
                bufferedReader =  new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                    sb.append(charBuffer, 0, bytesRead);
                }
                HttpEntity httpEntity = new StringEntity(sb.toString());
                requestBuilder.setEntity(httpEntity);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
    }
}
