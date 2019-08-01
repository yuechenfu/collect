package com.sike.collect.init;

import com.sike.collect.controller.formatter.LocalDateTimeFormatter;
import com.sike.collect.controller.rest.servlet.DataServiceServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

    @Autowired
    private DataServiceServlet servlet;

    @Bean
    ServletRegistrationBean ciscoServletRegistation(){
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(servlet);
        srb.setUrlMappings(Arrays.asList("/dataservice/*"));
        return srb;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDateTime.class, new LocalDateTimeFormatter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOrigins("*").allowedHeaders("*");
    }
}
