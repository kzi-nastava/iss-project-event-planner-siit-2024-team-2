package com.example.eventplanner.config;

//import com.example.eventplanner.config.jwt.JwtRequestFilter;
import com.example.eventplanner.config.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // Enable @PreAuthorize
public class WebSecurityConfiguration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (not recommended for production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events", "/api/events/summaries", "/api/events/top5").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events/{id}", "/api/events/{id}/agenda").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/service-products/top5"
                                , "/api/service-products/summaries"
                                , "/api/service-products/filtering-values").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/event-types").permitAll()
                        .requestMatchers("/socket", "/socket/**", "/send-message-rest", "/send/message").permitAll()
                        .requestMatchers("/api/images/{path}", "/api/images").permitAll()
                        .requestMatchers("/api/invitations/{token}/accept").permitAll()

                        // Protected endpoints (JWT required)
                        // Events
                        .requestMatchers(HttpMethod.POST, "/api/events").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.PUT, "/api/events/{id}").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/{id}").hasRole("EVENT_ORGANIZER")

                        // Event types
                        .requestMatchers(HttpMethod.POST, "/api/event-types").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.PUT, "/api/event-types/{id}").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.DELETE, "/api/event-types/{id}").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.GET, "/api/event-types/paginated").hasRole("ADMIN")

                        // Event agenda
                        .requestMatchers(HttpMethod.POST, "/api/events/{id}/agenda/**").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.PUT, "/api/events/{id}/agenda/**").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/{id}/agenda/**").hasRole("EVENT_ORGANIZER")

                        .requestMatchers(HttpMethod.GET, "/api/events/{id}/purchases", "/api/events/{id}/bookings").hasAnyRole("EVENT_ORGANIZER", "ADMIN")

                        // Service products
                        .requestMatchers(HttpMethod.POST, "/api/service-products").hasRole("SERVICE_PRODUCT_PROVIDER")
                        .requestMatchers(HttpMethod.PUT, "/api/service-products/{id}").hasRole("SERVICE_PRODUCT_PROVIDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/service-products/{id}").hasRole("SERVICE_PRODUCT_PROVIDER")

                        .requestMatchers(HttpMethod.POST, "/api/services").hasAnyRole("SERVICE_PRODUCT_PROVIDER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/services/{id}").hasRole("SERVICE_PRODUCT_PROVIDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/services/{id}").hasRole("SERVICE_PRODUCT_PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/services/all").hasRole("SERVICE_PRODUCT_PROVIDER")

                        .requestMatchers(HttpMethod.POST, "/api/products").hasAnyRole("SERVICE_PRODUCT_PROVIDER")
                        .requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole("SERVICE_PRODUCT_PROVIDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole("SERVICE_PRODUCT_PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/products/mine").hasRole("SERVICE_PRODUCT_PROVIDER")

                        // Invitations
                        .requestMatchers(HttpMethod.GET, "/api/invitations").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/invitations/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/invitations/{id}").hasRole("ADMIN")

                        // Notifications
                        .requestMatchers(HttpMethod.GET, "/api/notifications/mine").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/notifications/dismiss").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/notifications/seen").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/notifications").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/notifications/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/notifications").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/notifications/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/notifications/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/notifications/send-category-request").hasRole("SERVICE_PRODUCT_PROVIDER")

                        // Everything else requires authentication
                        .anyRequest().permitAll())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                ;

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(List.of(authProvider));
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}