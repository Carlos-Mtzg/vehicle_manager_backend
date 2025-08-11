package mx.edu.utez.vehicleManager.config;

import mx.edu.utez.vehicleManager.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    SecurityConfig(AuthenticationProvider authProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest -> authRequest
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/api/user/change-password/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/user/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/brand/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/customer/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/employee/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/service/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/vehicle/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/sale/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .anyRequest().authenticated())
                .sessionManagement(
                        sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
