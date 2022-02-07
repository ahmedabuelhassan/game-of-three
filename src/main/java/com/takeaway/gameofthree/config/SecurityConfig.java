package com.takeaway.gameofthree.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This class for configuring the web security.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * creating 3 dummy users in memory to be able for running the application.
     *
     * @param auth: {@link AuthenticationManagerBuilder}
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("ahmed")
                .password("{noop}123")
                .roles("USER")
                .and()
                .withUser("mohamed")
                .password("{noop}123")
                .roles("USER")
                .and()
                .withUser("abuelhassan")
                .password("{noop}123")
                .roles("USER");
    }
}