package com.example.doanphantichthietke.controller;

import com.example.doanphantichthietke.model.Cart;
import com.example.doanphantichthietke.service.dish.DishService;
import com.example.doanphantichthietke.service.mainDish.MainDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("cart")
public class HomeController {
    @Autowired
    MainDishService mainDishService;
    @ModelAttribute("cart")
    public Cart setupCart() {
        return new Cart();
    }
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("dishes", mainDishService.findAll());
        return "home";
    }

}
