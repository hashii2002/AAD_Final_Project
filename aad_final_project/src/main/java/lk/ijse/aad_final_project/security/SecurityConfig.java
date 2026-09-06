package lk.ijse.aad_final_project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Login API
                        .requestMatchers(HttpMethod.POST, "/v1/user/login").permitAll()

                        // Role APIs
                        .requestMatchers("/v1/role/**").hasRole("ADMIN")

                        // Create User API - temporary for initial user creation
                        .requestMatchers(HttpMethod.POST, "/v1/user/save").permitAll()

                        .requestMatchers("/v1/user/**").hasRole("ADMIN")

                        // Customer APIs
                        .requestMatchers(HttpMethod.POST, "/v1/customer/save")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")

                        .requestMatchers(HttpMethod.PUT, "/v1/customer/update")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")

                        .requestMatchers(HttpMethod.DELETE, "/v1/customer/**")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .requestMatchers(HttpMethod.GET, "/v1/customer/all")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .requestMatchers(HttpMethod.GET, "/v1/customer/select/**")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .requestMatchers(HttpMethod.GET, "/v1/customer/me")
                        .hasAnyRole("CUSTOMER")

                        // Driver APIs
                        .requestMatchers(HttpMethod.POST, "/v1/driver/save")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .requestMatchers(HttpMethod.GET, "/v1/driver/all")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .requestMatchers(HttpMethod.GET, "/v1/driver/select/**")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .requestMatchers(HttpMethod.PUT, "/v1/driver/update")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/v1/driver/**")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .requestMatchers(HttpMethod.GET, "/v1/driver/me")
                        .hasAnyRole( "DRIVER")

                        .anyRequest().authenticated()

                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
