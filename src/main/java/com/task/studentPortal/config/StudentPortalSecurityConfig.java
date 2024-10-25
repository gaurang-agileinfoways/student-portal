package com.task.studentPortal.config;

import com.task.studentPortal.config.filter.JwtTokenValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class StudentPortalSecurityConfig {
    private final StudentPortalUserDetailService userDetailsService;

    @Autowired
    private JwtTokenValidatorFilter jwtTokenValidatorFilter;

    public StudentPortalSecurityConfig(StudentPortalUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler csrfTokenHandler = new CsrfTokenRequestAttributeHandler();

        return http
                // Cors config
                .cors(corsConfigurer -> corsConfigurer.configurationSource(
                        new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration cors = new CorsConfiguration();
                                // add url in below singletonList which you want to allow
                                cors.setAllowedOrigins(Collections.singletonList("*"));
                                cors.addAllowedHeader("*");
                                cors.setAllowCredentials(true);
                                cors.setAllowedMethods(Collections.singletonList("*"));
                                cors.setExposedHeaders(List.of("Authorization"));
                                cors.setMaxAge(3600L);
                                return cors;
                            }
                        }
                ))

                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                //  .csrfTokenRequestHandler(csrfTokenHandler)
                //  .ignoringRequestMatchers("/auth/**")
                //  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                .authorizeHttpRequests(request ->
                        request.requestMatchers("/auth/register", "/auth/login", "/auth/**").permitAll()
                                .anyRequest().authenticated())

                // jwt filters
                .addFilterBefore(jwtTokenValidatorFilter, UsernamePasswordAuthenticationFilter.class)
                //  .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        StudentPortalAuthenticationProvider myAuth =
                new StudentPortalAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager pm = new ProviderManager(myAuth);
        pm.setEraseCredentialsAfterAuthentication(false);
        return pm;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
