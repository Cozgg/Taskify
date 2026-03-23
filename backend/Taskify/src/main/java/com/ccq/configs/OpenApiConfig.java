package com.ccq.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Tài Liệu cho dự án Taskify")
                        .version("1.0.0")
                        .description("Tài liệu hướng dẫn sử dụng các REST API của Taskify Backend.")
                        .contact(new Contact()
                                .name("Phạm Anh Quyền")
                                .email("paqvippro@gmail.com")));
    }
}