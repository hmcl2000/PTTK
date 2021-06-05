package com.example.doanphantichthietke.controller;


import com.example.doanphantichthietke.model.Cart;
import com.example.doanphantichthietke.model.Dish;
import com.example.doanphantichthietke.model.MainDish;
import com.example.doanphantichthietke.service.admin.AdminService;
import com.example.doanphantichthietke.service.cart.CartService;
import com.example.doanphantichthietke.service.dish.DishService;
import com.example.doanphantichthietke.service.mainDish.MainDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    AdminService admin;
    @Autowired
    DishService dishService;
    @Autowired
    CartService cartService;
    @Autowired
    MainDishService mainDishService;

    //-----HOME PAGE
    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("Carts", cartService.findAll());
        return "Admin/Cart/list";
    }

    //DANH SACH MON
    @GetMapping("/admin/dish")
    public String ShowDish(Model model) {
        model.addAttribute("Dishes", mainDishService.findAll());
        return "Admin/dish/list";
    }

    //THEM MON MOI
    @GetMapping("/admin/dish/createDish")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("Admin/dish/create");
        MainDish mainDish = new MainDish();
        modelAndView.addObject("mainDish", mainDish);
        return modelAndView;
    }

    //LUU MON MOI
    @PostMapping("/admin/dish/save")
    public String save(MainDish mainDish, RedirectAttributes redirectAttributes) {
        mainDishService.save(mainDish);
        redirectAttributes.addFlashAttribute("message", "Created Dish successfully!");
        return "redirect:/admin/dish";
    }

    //SUA THONG TIN MON
    @GetMapping("/admin/dish/{id}/edit")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Optional<MainDish> mainDish = mainDishService.findById(id);
        ModelAndView modelAndView = new ModelAndView("Admin/dish/edit");
        modelAndView.addObject("mainDish", mainDish.get());
        return modelAndView;
    }

    @PostMapping("/admin/dish/update")
    public String update(MainDish mainDish, RedirectAttributes redirect) {
        mainDishService.save(mainDish);
        redirect.addFlashAttribute("message", "Edit dish successfully!");
        return "redirect:/admin/dish";
    }

    //XOA MON
    @GetMapping("/admin/dish/{id}/delete")
    public ModelAndView delete(@PathVariable("id") Long id) {
        Optional<MainDish> mainDish = mainDishService.findById(id);
        ModelAndView modelAndView = new ModelAndView("Admin/dish/delete");
        modelAndView.addObject("mainDish", mainDish.get());
        return modelAndView;
    }

    @PostMapping("/admin/dish/delete")
    public String delete(MainDish mainDish, RedirectAttributes redirect) {
        mainDishService.remove(mainDish.getId());
        redirect.addFlashAttribute("message", "Delete dish successfully!");
        return "redirect:/admin/dish";
    }

    //CHI TIET MON
    @GetMapping("/admin/dish/{id}/view")
    public String view(@PathVariable("id") Long id, Model model) {
        Optional<MainDish> mainDish = mainDishService.findById(id);
        model.addAttribute("mainDish", mainDish.get());
        return "Admin/dish/view";
    }

    //CHI TIET GIO HANG
    @GetMapping("/admin/{id}/view")
    public ModelAndView viewCart(@PathVariable("id") Long id) {
        Optional<Cart> cartOptional = cartService.findById(id);
        Iterable<Dish> dishes = dishService.findAllByCart(cartOptional.get());
        ModelAndView modelAndView = new ModelAndView("/Admin/Cart/view");
        modelAndView.addObject("id", cartOptional.get().getId());
        modelAndView.addObject("cart", cartOptional.get());
        modelAndView.addObject("dishes", dishes);
        return modelAndView;
    }

    //XOA GIO HANG
    @GetMapping("/admin/{id}/delete")
    public ModelAndView deleteCart(@PathVariable("id") Long id) {
        Optional<Cart> cart = cartService.findById(id);
        ModelAndView modelAndView = new ModelAndView("Admin/Cart/delete");
        modelAndView.addObject("cart", cart.get());
        return modelAndView;
    }

    @PostMapping("/admin/delete")
    public String deleteCart(Cart cart, RedirectAttributes redirect) {
        cartService.remove(cart.getId());
        redirect.addFlashAttribute("message", "Delete dish successfully!");
        return "redirect:/admin";
    }
}


