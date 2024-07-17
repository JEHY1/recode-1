package com.example.recode.controller.product;

import com.example.recode.domain.Product;
import com.example.recode.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;

    //메인 페이지
    @GetMapping("/")
    public String showMain(Model model) {
        List<Product> newProductList = productService.newProduct();
        model.addAttribute("newProducts", newProductList);

        List<Product> bestProductList = productService.bestProduct();
        model.addAttribute("bestProducts", bestProductList);

        return "mains/main";
    }
}
