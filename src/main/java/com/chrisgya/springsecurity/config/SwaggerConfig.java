package com.chrisgya.springsecurity.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@OpenAPIDefinition(
        info = @Info(
                title = "Chrisgya Research Services",
                description = "Exploring Spring Boot to the core",
                contact = @Contact(
                        name = "Christian Gyaban",
                        url = "https://www.chrisgya.com",
                        email = "chrisgya500@gmail.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"))
       // servers = @Server(url = "http://localhost:6080/business")
)
@SecurityScheme(name = "api", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {
}
