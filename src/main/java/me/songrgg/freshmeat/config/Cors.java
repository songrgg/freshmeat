package me.songrgg.freshmeat.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author songrgg
 * @since 1.0
 */
@Configuration
public class Cors {

  @Bean
  public FilterRegistrationBean corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);

    CorsFilter corsFilter = new CorsFilter(source);
    corsFilter.setCorsProcessor(new NewCorsProcessor());
    FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
    bean.setOrder(10);
    return bean;
  }

}
