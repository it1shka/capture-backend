package com.it1shka.capture.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
  @Bean
  fun corsWebFilter(): CorsWebFilter {
    val corsConfiguration = CorsConfiguration()
    // for development purposes: 5173 is the Vite port
    corsConfiguration.allowedOrigins = listOf("http://localhost:5173")
    corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    corsConfiguration.allowedHeaders = listOf("*")
    corsConfiguration.allowCredentials = true
    corsConfiguration.maxAge = 3600L

    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", corsConfiguration)

    return CorsWebFilter(source)
  }
}