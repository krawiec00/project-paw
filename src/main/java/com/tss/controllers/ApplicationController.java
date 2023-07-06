package com.tss.controllers;

import com.tss.entities.Image;
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
    public String user(Model model) {
        imageModel(model);
        return "user";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        imageModel(model);
        return "admin";
    }

    private void imageModel(Model model) {
        List<Image> images = imageRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<String> base64Images = new ArrayList<>();

        for (Image image : images) {
            byte[] imageData = image.getImageData();
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            base64Images.add(base64Image);
        }

        model.addAttribute("images", base64Images);
        model.addAttribute("titles", images.stream().map(Image::getTitle).collect(Collectors.toList()));
        model.addAttribute("contents", images.stream().map(Image::getContent).collect(Collectors.toList()));
    }
}
