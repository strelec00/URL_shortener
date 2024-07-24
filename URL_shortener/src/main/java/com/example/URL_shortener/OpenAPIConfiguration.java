package com.example.URL_shortener;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

	@Bean
	public OpenAPI defineOpenApi() {
		Server server = new Server();
		server.setUrl("http://localhost:8080");
		server.setDescription("development");

		Contact myContact = new Contact();
		myContact.setName("Jan Strelec");
		myContact.setEmail("jan.strelec853@gmail.com");

		Info information = new Info()
				.title("URL shortening API")
				.version("1.0")
				.description("This API allows administration and URL shortening.")
				.contact(myContact);

		SecurityScheme securityScheme = new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("basic");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("basicAuth");

		return new OpenAPI()
				.components(new Components().addSecuritySchemes("basicAuth", securityScheme))
				.addSecurityItem(securityRequirement)
				.info(information)
				.servers(List.of(server));
	}
}
