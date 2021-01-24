package com.data.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerDemoApiInfo()).select().build();
    }

    private ApiInfo swaggerDemoApiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact("IOT", "www.senseprocess.com", "oliver.fu@senseprocess.com"))
                //文档标题
                .title("iot")
                //文档描述
                .description("iot.senseprocess.com")
                //文档版本
                .version("1.0.0")
                .build();
    }
}
