package com.tss.controllers;

import com.tss.entities.Image;
import com.tss.entities.ImageForm;
import com.tss.repositories.ImageRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.multipart.MultipartFile;

@Controller
public class ContentController {

    private final ImageRepository imageRepository;

    @Autowired
    public ContentController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/uploadImageForm")
    public String showUploadImageForm(Model model) {
        model.addAttribute("imageForm", new ImageForm());
        return "uploadImage";
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@ModelAttribute("imageForm") ImageForm imageForm, Image image, Model model) {
        try {
            MultipartFile imageFile = imageForm.getImageFile();
            byte[] imageData = imageFile.getBytes();

            image.setImageData(imageData);
            image.setTitle(image.getTitle());
            image.setContent(image.getContent());
            imageRepository.save(image);

        } catch (IOException e) {
            // Handle the exception
        }
        return "redirect:/";
    }



}
