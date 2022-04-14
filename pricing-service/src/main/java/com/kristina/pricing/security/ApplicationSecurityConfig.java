package com.kristina.pricing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordConfig passwordConfig;

    public ApplicationSecurityConfig(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }
    //Basic Auth
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //whitelisting the requests for the following urls for all users even they are not logged in
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
    //Role-based auth
    //1. Declare user details in memory
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails kristinaUser = User.builder()
                .username("Kristina Cer")
                //Spring enforces password encoding!
                .password(passwordConfig.passwordEncoder().encode("password"))
                .roles("STUDENT") //ROLE_STUDENT
                .build();
        return new InMemoryUserDetailsManager(kristinaUser);
    }
}