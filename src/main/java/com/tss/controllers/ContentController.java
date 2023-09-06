package com.tss.controllers;

import com.tss.entities.Image;
import com.tss.entities.ImageForm;
import com.tss.entities.User;
import com.tss.repositories.ImageRepository;
import com.tss.repositories.UserRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.multipart.MultipartFile;

@Controller
public class ContentController {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContentController(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/uploadImageForm")
    public String showUploadImageForm(Model model) {
        model.addAttribute("imageForm", new ImageForm());
        return "uploadImage";
    }

    @PostMapping("/uploadImage")
    public String uploadImage(
            @ModelAttribute("imageForm") ImageForm imageForm, // Przyjęcie danych z formularza
            Image image, // Obiekt reprezentujący obraz
            Model model, // Model Spring MVC używany do przekazywania danych do widoku
            Authentication authentication // Informacje o uwierzytelnieniu użytkownika
    ) {
        try {
            MultipartFile imageFile = imageForm.getImageFile(); // Pobranie przesłanego pliku obrazu
            byte[] imageData = imageFile.getBytes(); // Konwersja pliku na tablicę bajtów

            image.setImageData(imageData); // Ustawienie danych obrazu
            image.setTitle(image.getTitle()); // Ustawienie tytułu obrazu
            image.setContent(image.getContent()); // Ustawienie treści obrazu

            // Sprawdzanie, czy użytkownik jest uwierzytelniony i przypisanie obrazu do użytkownika
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                User user = userRepository.findByUserName(userDetails.getUsername()).orElse(null);
                image.setUser(user); // Przypisanie użytkownika do obrazu
            }
            imageRepository.save(image); // Zapisanie obrazu w repozytorium
        } catch (IOException e) {
            // Obsługa wyjątku IOException, który może wystąpić podczas przetwarzania pliku
            // Możesz dodać kod obsługi błędu lub zalogować informację o błędzie
        }

        // Po przetworzeniu obrazu, przekierowanie na główną stronę
        return "redirect:/";
    }

}
