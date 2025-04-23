//package com.ruoyi.framework.config.battle;
//
//import cn.hutool.extra.spring.SpringUtil;
//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// * @author hongjiasen
// */
//@Configuration
//@EnableKnife4j
//public class Knife4JSwaggerConfig {
//
////    @Bean(name = "createRestApiKnife4j")
//    @Bean(name = "knife4jDocket")
//    public Docket createRestApi(){
//        return new Docket(DocumentationType.OAS_30)
//                .enable(!SpringUtil.getActiveProfile().equals("aliyun"))
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("scds")
//                .description("士兵对抗项目")
//                .version("1.0.0")
//                .contact(new Contact("hongjiasen", "http://www.baidu.com", "hjs2152@hitqz.com"))
//                .license("Home")
//                .licenseUrl("http://localhost:11000")
//                .termsOfServiceUrl("http://localhost:11000/doc.html")
//                .build();
//    }
//
//}
