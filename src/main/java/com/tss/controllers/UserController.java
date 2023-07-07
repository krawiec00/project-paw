package com.tss.controllers;

import com.tss.entities.Image;
import com.tss.entities.User;
import com.tss.repositories.ImageRepository;
import com.tss.repositories.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class UserController {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public UserController(UserRepository userRepository,ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository=imageRepository;

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
    @GetMapping("/deleteUserPosts/{id}")
public String deleteUserPosts(@PathVariable("id") int id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

    // Pobierz wszystkie zdjęcia przypisane do użytkownika
    List<Image> userImages = imageRepository.findAllByUser(user);

    // Usuń wszystkie zdjęcia użytkownika
    imageRepository.deleteAll(userImages);

    return "redirect:/adminPanel";
}


    
}
