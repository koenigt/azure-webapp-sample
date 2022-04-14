package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@Profile("basicauth")
public class SecurityBasicAuthConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().permitAll();
        http.httpBasic();
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/error").permitAll();
        http.authorizeRequests().antMatchers("/webjars/**").permitAll();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
    

        http.authorizeRequests().antMatchers("/restricted").hasRole("FLX.USER").anyRequest().authenticated();

        //http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        // ensure the passwords are encoded properly
        UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("admin").password("admin").roles("FLX.USER", "FLX.EDITOR", "FLX.ADMIN")
                .build());

        manager.createUser(users.username("editor").password("editor")
                .roles("FLX.USER", "FLX.EDITOR")
                .build());

        manager.createUser(users.username("user").password("user")
                .roles("FLX.EDITOR")
                .build());

        return manager;
    }

}
