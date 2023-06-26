package com.tss.controllers;

import com.tss.entities.User;
import com.tss.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegistrationForm() {
        return "register";
    }

@PostMapping
    public String registerUser(String username, String password) {
        // Sprawdź, czy istnieje już użytkownik o podanej nazwie użytkownika
        Optional<User> existingUser = userRepository.findByUserName(username);
        if (existingUser.isPresent()) {
            // Użytkownik o podanej nazwie użytkownika już istnieje, obsłuż ten przypadek
            // Możesz wyświetlić odpowiedni komunikat lub przekierować na stronę błędu
            return "redirect:/register?error";
        }

        User newUser = new User();
        newUser.setUserName(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setActive(true);
        newUser.setRoles("ROLE_USER");

        userRepository.save(newUser);

        return "redirect:/login";
    }
}