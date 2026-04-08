package com.adouk.finacombackend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class FinacomBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinacomBackendApplication.class, args);
	}

	@Bean
    public ModelMapper modelMapper() {
        return  new ModelMapper();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FINACOM+ API")
                        .version("1.0")
                        .description("API")
                        .contact(new Contact()
                                .name("Support")
                                .email("support@adouk.tech"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }



}
