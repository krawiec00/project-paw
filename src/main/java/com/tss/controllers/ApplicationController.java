package com.tss.controllers;

import com.tss.entities.Image;
import com.tss.entities.User;
import com.tss.repositories.ImageRepository;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

    private final ImageRepository imageRepository;

    @Autowired
    public ApplicationController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Pobierz rolę zalogowanego użytkownika
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {

                return "redirect:/admin"; // Przekierowanie dla roli "ADMIN" na stronę admina
            } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {

                return "redirect:/user"; // Przekierowanie dla roli "USER" na stronę użytkownika
            }
        }
        return "redirect:/login"; // Przekierowanie na stronę logowania w przypadku braku uwierzytelnienia
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/user")
    public String user(Model model,Authentication authentication) {
        imageModel(model,authentication);
        return "user";
    }

    @GetMapping("/admin")
    public String admin(Model model,Authentication authentication) {
        imageModel(model,authentication);
        return "admin";
    }

    private void imageModel(Model model, Authentication authentication) {
    List<Image> images = imageRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    List<String> base64Images = new ArrayList<>();
    List<String> usernames = new ArrayList<>(); // lista loginów użytkowników
    List<String> titles = new ArrayList<>(); // lista tytułów

    for (Image image : images) {
        byte[] imageData = image.getImageData();
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        base64Images.add(base64Image);
        
        User user = image.getUser();
        if (user != null) {
            usernames.add(user.getUserName()); // dodaj login użytkownika do listy
        } else {
            usernames.add(""); // jeśli użytkownik nie jest przypisany, dodaj pusty ciąg znaków
        }
        titles.add(image.getTitle()); // dodaj tytuł do listy
    }

    model.addAttribute("images", base64Images);
    model.addAttribute("titles", titles);
    model.addAttribute("contents", images.stream().map(Image::getContent).collect(Collectors.toList()));
    model.addAttribute("usernames", usernames); // przekaż listę loginów użytkowników do widoku
}


}
