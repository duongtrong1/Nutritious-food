package com.spring.dev2chuc.nutritious_food.config;

import com.spring.dev2chuc.nutritious_food.security.CustomUserDetailsService;
import com.spring.dev2chuc.nutritious_food.security.JwtAuthenticationEntryPoint;
import com.spring.dev2chuc.nutritious_food.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/api/food/**",
            "/api/category/**",
            "/api/schedule/**",
            "/api/schedule-combo/**"
    };

    private static final String ROLE = "ROLE_ADMIN";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/api/auth/**", "/api/db/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS).hasAnyAuthority(ROLE)
                .antMatchers(HttpMethod.PUT, PUBLIC_MATCHERS).hasAnyAuthority(ROLE)
                .antMatchers(HttpMethod.DELETE, PUBLIC_MATCHERS).hasAnyAuthority(ROLE)
                .antMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority(ROLE)
                .antMatchers(HttpMethod.GET,
                        "/api/category/**",
                        "/api/user-profile/**",
                        "/api/food/**",
                        "/api/schedule/**",
                        "/api/schedule-combo/**",
                        "/api/combo/**",
                        "/api/push/**",
                        "/api/payment/**",
                        "/api/ratting-food/**",
                        "/api/ratting-combo/**",
                        "/api/ratting-schedule/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}