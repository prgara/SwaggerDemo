package io.pragra.SwaggerDemo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@OpenAPIDefinition(info = @Info(title = "Item Application",
        description = "Application for CRUD operation",
        version = "1.0.0"))
@SpringBootApplication
@EnableCaching
public class SwaggerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerDemoApplication.class, args);
    }

}
