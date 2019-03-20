package com.guli.sysuser.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {


    @Bean
    public Docket adminApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

    }



    private ApiInfo adminApiInfo(){

        return new ApiInfoBuilder()
                .title("后台管理系统-系统用户通用服务")
                .description("本文档描述了系统用户通用服务接口定义")
                .version("1.0")
                .contact(new Contact("Wanba", "http://atguigu.com", "55317332@qq.com"))
                .build();
    }
}
