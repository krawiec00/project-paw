
package com.tss.controllers;

import com.tss.entities.User;
import com.tss.repositories.UserRepository;
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
     
    
    
}
