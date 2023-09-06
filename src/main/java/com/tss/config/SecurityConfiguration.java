package com.tss.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        // Konfiguracja dostępu do ścieżki "/admin" - wymagane uprawnienia roli "ADMIN"
        .antMatchers("/admin").hasRole("ADMIN")   
        
        // Konfiguracja dostępu do ścieżki "/user" - wymagane uprawnienia ról "ADMIN" lub "USER"
        .antMatchers("/user").hasAnyRole("ADMIN", "USER")
        
        // Konfiguracja dostępu do głównej ścieżki "/" - dostęp dla wszystkich (nawet niezalogowanych)
        .antMatchers("/").permitAll()
        
        // Konfiguracja dostępu do ścieżki "/register" - dostęp dla wszystkich (nawet niezalogowanych)
        .antMatchers("/register").permitAll()
        
        .and()
        .formLogin()
        // Konfiguracja strony logowania na ścieżkę "/login" - dostęp dla wszystkich (nawet niezalogowanych)
        .loginPage("/login").permitAll()
        
        // Po pomyślnym zalogowaniu przekierowanie na główną ścieżkę "/"
        .defaultSuccessUrl("/")
        
        .and()
        .logout()
        // Po wylogowaniu przekierowanie na stronę logowania "/login"
        .logoutSuccessUrl("/login")
        
        // Konfiguracja żądania wylogowania poprzez dopasowanie ścieżki "/logout"
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
}

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
