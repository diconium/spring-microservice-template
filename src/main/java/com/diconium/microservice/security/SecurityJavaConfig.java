package com.diconium.microservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityJavaConfig(
        RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin")
            .password(encoder().encode("adminPass"))
            .roles("ADMIN")
            .and()
            .withUser("user")
            .password(encoder().encode("userPass"))
            .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/ext/**").authenticated()
            .and()
            .httpBasic()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
        ;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
