package lk.ijse.aad_final_project.security;

import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.aad_final_project.constant.CommonResponse;
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
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;

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
                        .requestMatchers(HttpMethod.POST, "/v1/user/save").hasRole("ADMIN")

                        .requestMatchers("/v1/user/**").hasRole("ADMIN")

                        // Register Customer APIs
                        .requestMatchers("/v1/user/login", "/v1/customer/register").permitAll()

                        // Customer APIs
                        .requestMatchers(HttpMethod.POST, "/v1/customer/save").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/v1/customer/update").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.PATCH, "/v1/customer/update").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/customer/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/customer/all").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/customer/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/customer/me").hasAnyRole("CUSTOMER")

                        // Driver APIs
                        .requestMatchers(HttpMethod.POST, "/v1/driver/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/driver/all").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/driver/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/v1/driver/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/driver/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/driver/me").hasAnyRole( "DRIVER")

                        // Vehicle Brand APIs
                        .requestMatchers(HttpMethod.POST, "/v1/brand/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/brand/all").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/v1/brand/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/v1/brand/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/brand/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Vehicle Category APIs
                        .requestMatchers(HttpMethod.POST, "/v1/category/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/category/all").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/v1/category/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/v1/category/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/category/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Vehicle Model APIs
                        .requestMatchers(HttpMethod.POST, "/v1/model/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/model/all").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/v1/model/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/v1/model/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/model/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Vehicle APIs
                        .requestMatchers(HttpMethod.POST, "/v1/vehicle/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/vehicle/all").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/v1/vehicle/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/v1/vehicle/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/vehicle/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Vehicle Document APIs
                        .requestMatchers("/v1/vehicleDocument/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Rental Rate APIs
                        .requestMatchers("/v1/rentalRate/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Rental APIs
                        .requestMatchers(HttpMethod.POST, "/v1/rental/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/rental/me").hasRole( "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/v1/rental/all").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/rental/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/v1/rental/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/rental/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Rental Driver APIs
                        .requestMatchers(HttpMethod.POST, "/v1/rentalDriver/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/rentalDriver/all").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/rentalDriver/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/rentalDriver/me").hasRole( "DRIVER")
                        .requestMatchers(HttpMethod.PUT, "/v1/rentalDriver/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/rentalDriver/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Payment APIs
                        .requestMatchers(HttpMethod.POST, "/v1/payment/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/payment/all").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/payment/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/v1/payment/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/payment/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/payment/me").hasRole("CUSTOMER")

                        // Invoice APIs
                        .requestMatchers(HttpMethod.POST, "/v1/invoice/save").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/invoice/all").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/invoice/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/v1/invoice/update").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/invoice/**").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/invoice/me").hasRole("CUSTOMER")

                        // Vehicle Inspection APIs
                        .requestMatchers("/v1/vehicle-inspection/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Maintenance APIs
                        .requestMatchers("/v1/maintenance/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // Review APIs
                        .requestMatchers(HttpMethod.POST, "/v1/review/save").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/v1/review/update").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/review/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/review/me").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/v1/review/all").hasAnyRole("ADMIN", "FLEET_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/v1/review/select/**").hasAnyRole("ADMIN", "FLEET_MANAGER")

                        .anyRequest().authenticated()

                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 401 Unauthorized Handling
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            CommonResponse res = new CommonResponse(1, "Authentication is required to access this resource");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(res));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // 403 Forbidden Handling
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            CommonResponse res = new CommonResponse(1, "You do not have permission to access this resource");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(res));
                        })
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
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
