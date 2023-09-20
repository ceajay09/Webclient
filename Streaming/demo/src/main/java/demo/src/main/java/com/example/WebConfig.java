package demo.src.main.java.com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // config.addAllowedOrigin("*"); // Erlaubt alle Ursprünge, Sie können hier auch
        // eine spezifische URL angeben
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*"); // Erlaubt alle Header
        config.addAllowedMethod("*"); // Erlaubt alle HTTP-Methoden
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}