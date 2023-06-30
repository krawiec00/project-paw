
package com.tss.controllers;

import com.tss.entities.Image;
import com.tss.entities.ImageForm;
import com.tss.entities.User;
import com.tss.repositories.ImageRepository;
import com.tss.repositories.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller
class UserController {
    
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    
       @Autowired
    public UserController(UserRepository userRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }
    
    
        @RequestMapping("/adminPanel")
    public String usersList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "adminPanel";
    }
     
    @GetMapping("/showEditUserForm/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "editUserForm";
    }

    @PostMapping("/edituser/{id}")
    public String editUser(@PathVariable("id") int id, User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "editUserForm";
        }
        userRepository.save(user);
        return "redirect:/adminPanel";
    }
    
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/adminPanel";
    }
    @GetMapping("/uploadImageForm")
    public String showUploadImageForm(Model model) {
        model.addAttribute("imageForm", new ImageForm());
        return "uploadImage";
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@ModelAttribute("imageForm") ImageForm imageForm, Model model) {
        try {
            MultipartFile imageFile = imageForm.getImageFile();
            byte[] imageData = imageFile.getBytes();

            Image image = new Image();
            image.setImageData(imageData);
            imageRepository.save(image);
        } catch (IOException e) {
            // Handle the exception
        }
        return "redirect:/adminPanel";
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
    return "imageGallery";
}
}
