package by.petrovich.reminder.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reminder API")
                        .version("1.0.1")
                        .description("This API manages reminders for users.")
                        .contact(new Contact().name("Alexandr Petrovich")
                                .url("https://reminder.com")
                                .email("a.piatrovich@gmail.com"))
                );
    }

}
