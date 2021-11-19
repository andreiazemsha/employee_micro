package com.example.employee.employee_management.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.employee.employee_management"))
                .paths(regex("/.*"))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Simple employee management system (sEMS)",
                "It's a simple application which provides several endpoints through which employees can't be manged. " +
                        "Supports creation, removal, updates and state changes for employees",
                "1.1",
                null,
                null,
                "GNU GPL",
                "https://www.gnu.org/licenses/gpl-3.0.html",
                Collections.emptyList()
        );
    }
}
