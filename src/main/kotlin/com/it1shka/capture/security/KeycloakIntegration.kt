package com.it1shka.capture.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class KeycloakIntegration {
  @Bean
  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http
      .cors { }
      .csrf { it.disable() }
      .authorizeExchange { exchanges ->
        exchanges
          .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
          .anyExchange().authenticated()
      }
      .oauth2ResourceServer { oauth2 -> oauth2.jwt { } }
      .build()
  }
}