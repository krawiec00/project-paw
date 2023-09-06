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
            // Użytkownik o podanej nazwie użytkownika już istnieje
            // Przygotowanie do obsługi tego przypadku, np. wyświetlenie komunikatu lub przekierowanie na stronę błędu
            return "redirect:/register?error";
        }

        // Tworzenie nowego użytkownika
        User newUser = new User();
        newUser.setUserName(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Hasło jest kodowane przed zapisaniem
        newUser.setActive(true); // Ustawienie aktywności użytkownika na true
        newUser.setRoles("ROLE_USER"); // Przypisanie roli "ROLE_USER"

        // Zapisanie nowego użytkownika w repozytorium (bazie danych)
        userRepository.save(newUser);

        // Po pomyślnym zarejestrowaniu przekierowanie na stronę logowania
        return "redirect:/login";
    }
}
