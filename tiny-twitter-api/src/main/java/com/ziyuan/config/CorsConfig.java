package com.ziyuan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter() {
        // CORS allow origins list
        CorsConfiguration config = new CorsConfiguration();
        // config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("*");

        // set allow credentials(cookies)
        config.setAllowCredentials(true);

        // set allow methods
        config.addAllowedMethod("*");

        // set allow headers
        config.addAllowedHeader("*");


        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(corsSource);
    }

}
