
package com.tss.controllers;

import com.tss.entities.User;
import com.tss.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
class UserController {
    
    private final UserRepository userRepository;
    
       @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
        @RequestMapping("/adminPanel")
    public String usersList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "adminPanel";
    }
     
@PostMapping("/adminPanel/save")
public String saveAllUsers(@ModelAttribute("users") List<User> users) {
    for (User user : users) {
        Optional<User> existingUser = userRepository.findByUserName(user.getUserName());
        if (existingUser.isPresent()) {
            // Aktualizacja istniejącego użytkownika
            User existing = existingUser.get();
            existing.setRoles(user.getRoles());
            existing.setActive(user.isActive());
            userRepository.save(existing);
        } else {
            // Dodanie nowego użytkownika
            userRepository.save(user);
        }
    }

    return "redirect:/adminPanel";
}
@GetMapping("/deleteUser/{id}")
public String deleteUser(@PathVariable("id") int id, Model model){
User user=userRepository.findById(id)
        .orElseThrow(()->new IllegalArgumentException("Invalid user Id:"+id));
userRepository.delete(user);
return "redirect:/";
}
    
}
