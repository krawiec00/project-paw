package com.tss.controllers;

import com.tss.entities.Image;
import com.tss.entities.ImageForm;
import com.tss.repositories.ImageRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
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
public String uploadImage(@ModelAttribute("imageForm") ImageForm imageForm, Image image , Model model) {
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
    return "redirect:/admin";
}

    @GetMapping("/images")
public String showImages(Model model) {
    List<Image> images = imageRepository.findAll();
    List<String> base64Images = new ArrayList<>();

    for (Image image : images) {
        byte[] imageData = image.getImageData();
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        base64Images.add(base64Image);
    }

    model.addAttribute("images", base64Images);
    model.addAttribute("titles", images.stream().map(Image::getTitle).collect(Collectors.toList()));
    model.addAttribute("contents", images.stream().map(Image::getContent).collect(Collectors.toList()));

    return "imageGallery";
}


}
