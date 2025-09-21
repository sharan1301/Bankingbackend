package com.example.BankingBackend.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")  // apply to all endpoints
                    .allowedOrigins("http://127.0.0.1:5500")  // your frontend origin
                    .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                    .allowedHeaders("*")  // allow all headers
                    .allowCredentials(true);
        }
}
