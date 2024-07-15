package by.petrovich.reminder.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class OpenApiConfig implements WebMvcConfigurer {

    static {
        SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(JsonIgnore.class);
    }

    @Bean
    public GroupedOpenApi customOpenApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }
}
