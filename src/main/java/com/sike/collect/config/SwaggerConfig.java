package com.sike.collect.config;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {  
        
        Docket dock=new Docket(DocumentationType.SWAGGER_2);
        dock.apiInfo(apiInfo());
        dock.select().apis(
        		RequestHandlerSelectors.basePackage("com.sike.collect.controller.rest")).build()
			        .genericModelSubstitutes(ResponseEntity.class)
			        .apiInfo(apiInfo())
			        .securitySchemes(Lists.newArrayList(apiKey()))
			        .securityContexts(Arrays.asList(securityContext()));
       
        return dock;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Sike")
            .description("Sike Collection document")
            .termsOfServiceUrl("http://www.webxury.com")
            .version("1.0.0")
            .build();
    } 
    
    @Bean
    public SecurityConfiguration security() {
    return SecurityConfigurationBuilder.builder().scopeSeparator(",")
        .additionalQueryStringParams(null)
        .useBasicAuthenticationWithAccessCodeGrant(false).build();
    }

    private ApiKey apiKey() {
    return new ApiKey("apiKey", "ck", "header");
    }

    private SecurityContext securityContext() {
	    return SecurityContext.builder().securityReferences(defaultAuth())
	        .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
	    AuthorizationScope authorizationScope = new AuthorizationScope(
	        "global", "accessEverything");
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	    authorizationScopes[0] = authorizationScope;
	    return Arrays.asList(new SecurityReference("apiKey",
	        authorizationScopes));
    }


    
  

}