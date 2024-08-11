package by.petrovich.reminder.config;

import by.petrovich.reminder.interceptor.ReminderValidationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ReminderValidationInterceptor reminderValidationInterceptor() {
        return new ReminderValidationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reminderValidationInterceptor()).addPathPatterns("/api/v1/reminders/**");
    }
}
