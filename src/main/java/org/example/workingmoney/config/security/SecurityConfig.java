package org.example.workingmoney.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.cors.allowed-origins:${ALLOWED_ORIGINS}}")
    private String allowedOriginsProperty;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
        List<String> validatedOrigins = validateOriginsProperty();
		configuration.setAllowedOrigins(validatedOrigins);
        // TODO: setAllowedMethods, setAllowedHeaders, setAllowCredentials 설정 추후 수정 필요
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Set-Cookie", "access"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/health", "/api/v1/auth/join").permitAll()
                                .anyRequest().authenticated()
                )
                .cors(Customizer.withDefaults())
                .build();
    }

    private List<String> validateOriginsProperty() {
        if (allowedOriginsProperty == null || allowedOriginsProperty.trim().isEmpty()) {
            throw new IllegalStateException("CORS allowed-origins 설정이 비어 있습니다.");
        }

        List<String> validatedOrigins = Arrays.stream(allowedOriginsProperty.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        boolean hasWildcard = validatedOrigins.stream().anyMatch(origin -> origin.contains("*"));
        if (hasWildcard) {
            throw new IllegalStateException("CORS allowed-origins에 와일드카드(*)는 허용되지 않습니다.");
        }
        if (validatedOrigins.isEmpty()) {
            throw new IllegalStateException("CORS allowed-origins에 유효한 값이 없습니다.");
        }

        return validatedOrigins;
    }
}
