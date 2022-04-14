package com.kristina.pricing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.kristina.pricing.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
  private final PasswordEncoder passwordEncoder;

  public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  // Basic Auth
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        // whitelisting the requests for the following urls for all users even they are not logged
        // in
        .antMatchers("/", "index", "/css/*", "/js/*")
        .permitAll()
        // ROLE_BASED AUTH: only students will be able to access this api
        .antMatchers("/api/**")
        .hasRole(ADMIN.name())
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic();
  }
  // Role-based auth
  // 1. Declare user details in memory
  @Override
  @Bean
  protected UserDetailsService userDetailsService() {

    UserDetails kristinaUser =
        User.builder()
            .username("Kristina")
            // Spring enforces password encoding!
            .password(passwordEncoder.encode("pass"))
            .roles(CLIENT.name()) // ROLE_STUDENT
            .build();

    UserDetails lindaUser =
        User.builder()
            .username("Linda")
            // Spring enforces password encoding!
            .password(passwordEncoder.encode("pass"))
            // ApplicationUserRole.ADMIN
            .roles(ADMIN.name()) // ROLE_ADMIN
            .build();

    return new InMemoryUserDetailsManager(kristinaUser, lindaUser);
  }
}
