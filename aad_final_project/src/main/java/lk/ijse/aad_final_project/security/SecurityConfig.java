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
                        .requestMatchers(HttpMethod.POST, "/v1/customer/save").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/v1/customer/update").hasAnyRole("ADMIN", "FLEET_MANAGER", "CUSTOMER")
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
