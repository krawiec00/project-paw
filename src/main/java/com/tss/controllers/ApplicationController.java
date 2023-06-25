package com.tss.controllers;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

   @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Pobierz rolę zalogowanego użytkownika
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                return "admin"; // Przekierowanie dla roli "ADMIN" na stronę admina
            } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
                return "user"; // Przekierowanie dla roli "USER" na stronę użytkownika
            }
        }
        return "redirect:/login"; // Przekierowanie na stronę logowania w przypadku braku uwierzytelnienia
    }

       @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }
    
    
    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}

