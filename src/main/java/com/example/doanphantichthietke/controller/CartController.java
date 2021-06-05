package com.example.doanphantichthietke.controller;

import com.example.doanphantichthietke.model.Cart;
import com.example.doanphantichthietke.model.Dish;
import com.example.doanphantichthietke.model.MainDish;
import com.example.doanphantichthietke.service.cart.CartService;
import com.example.doanphantichthietke.service.dish.DishService;
import com.example.doanphantichthietke.service.mainDish.MainDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Controller

public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    DishService dishService;

    @Autowired
    MainDishService mainDishService;
/*@GetMapping("/home/cart")
public String index(Model model) {
    model.addAttribute("carts", cartService.findAll());
    return "cart/list";
}*/

    //-----CREATE NEW cart
    @GetMapping("/home/cart/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("cart/create");
        Cart cart = new Cart();
        LocalDateTime date = LocalDateTime.now();
        cart.setDateCreated(date);
        modelAndView.addObject("cart",cart);
        return modelAndView;
    }

    @PostMapping("/home/cart/save")
    public String save(Cart cart, Model model) {
        cartService.save(cart);

        model.addAttribute("message", "Created cart successfully!");
        model.addAttribute("id", cart.getId());
        model.addAttribute("cart", cart);
        return "cart/home";
    }

    //----- GIAO DIEN DE CHUYEN SANG MUA DO
/*    @GetMapping("/home/cart")
    public ModelAndView homeCart() {
        ModelAndView modelAndView = new ModelAndView("cart/home");

        return modelAndView;
    }*/

    //-----VIEW cart
    @GetMapping("/home/cart/{id}/view")
    public ModelAndView viewCart(@PathVariable("id") Long id) {
        Optional<Cart> cartOptional = cartService.findById(id);
        if (!cartOptional.isPresent()) {
            return new ModelAndView("/error.404");
        }
        Iterable<Dish> dishes = dishService.findAllByCart(cartOptional.get());
        ModelAndView modelAndView = new ModelAndView("/cart/view");
        modelAndView.addObject("id", cartOptional.get().getId());
        modelAndView.addObject("cart", cartOptional.get());
        modelAndView.addObject("dishes", dishes);
        return modelAndView;
    }

    //-----CREATE NEW dish IN cart
    @GetMapping("/home/cart/{id}/create/dish")
    public ModelAndView createDish(@PathVariable Long id) {

        ModelAndView modelAndView = new ModelAndView("cart/create-dish");
        Optional<Cart> cartOptional = cartService.findById(id);
/*        Dish dish = new Dish();
        dish.setCart(cartOptional.get());
        modelAndView.addObject("dish", dish);*/
        modelAndView.addObject("cart", cartOptional.get());
        modelAndView.addObject("dishes", mainDishService.findAll());

        return modelAndView;
    }

    @PostMapping("/home/cart/{id}/save/dish")
    public String save(@PathVariable Long id, Dish dish, RedirectAttributes redirectAttributes) {
        dishService.save(dish);
        redirectAttributes.addFlashAttribute("message", "Đặt hàng thành công! Cửa hàng sẽ liên hệ ngay cho bạn để hoàn tất thủ tục");
        return "redirect:/home/cart/{id}/create/dish";
    }


    //-----EDIT dish IN cart
    @GetMapping("/home/cart/{id}/view/edit/{id2}")
    public ModelAndView editDish(@PathVariable("id") Long id, @PathVariable("id2") Long id2) {
        Optional<Dish> dish = dishService.findById(id2);
        ModelAndView modelAndView = new ModelAndView("cart/edit-dish");
        modelAndView.addObject("id", id);
        modelAndView.addObject("dish", dish.get());
        return modelAndView;
    }

    @PostMapping("/home/cart/{id}/view/update/{id2}")
    public String update(@PathVariable("id") Long id, @PathVariable("id2") Long id2, Dish dish, RedirectAttributes redirectAttributes) {
        dishService.save(dish);
        redirectAttributes.addFlashAttribute("message", "Update dish successfully");
        return "redirect:/home/cart/{id}/view/edit/{id2}";
    }

    //-----DELETE dish IN cart
    @GetMapping("/home/cart/{id}/view/delete/{id2}")
    public ModelAndView deleteDish(@PathVariable("id") Long id, @PathVariable("id2") Long id2) {
        Optional<Dish> dish = dishService.findById(id2);
        ModelAndView modelAndView = new ModelAndView("cart/delete-dish");
        modelAndView.addObject("id", id);
        modelAndView.addObject("dish", dish.get());
        return modelAndView;
    }
    @PostMapping("/home/cart/{id}/view/delete/{id2}")
    public String deleteDish(@PathVariable("id") Long id, @PathVariable("id2") Long id2, Dish dish, RedirectAttributes redirectAttributes) {
        dishService.remove(id2);
        redirectAttributes.addFlashAttribute("message", "Delete dish successfully");
        return "redirect:/home/cart/{id}/view";
    }

    //-----DELETE cart
    @GetMapping("/home/cart/{id}/delete")
    public ModelAndView delete(@PathVariable("id") Long id) {
        Optional<Cart> cartOptional = cartService.findById(id);
        ModelAndView modelAndView = new ModelAndView("cart/delete");
        modelAndView.addObject("cart", cartOptional.get());
        return modelAndView;
    }
    @PostMapping("/home/cart/delete")
    public String delete(Cart cart, RedirectAttributes redirectAttributes) {
        cartService.remove(cart.getId());
        redirectAttributes.addFlashAttribute("message", "Delete cart successfully");
        return "redirect:/home/cart";
    }

    @GetMapping("/home/cart/{id}/dish/{id2}")
    public String buyDish(@PathVariable("id") Long id, @PathVariable("id2") Long id2, Model model, RedirectAttributes redirectAttributes) {
        Optional<MainDish> mainDishOptional = mainDishService.findById(id2);
        Optional<Cart> cartOptional = cartService.findById(id);
        Dish dish = new Dish();
        dish.setCart(cartOptional.get());
        dish.setName(mainDishOptional.get().getName());
        dish.setPrice(mainDishOptional.get().getPrice());
        model.addAttribute("dish", dish);
        return "cart/create-dish";
    }
}
