package com.example.recode.controller.product;

import com.example.recode.service.ProductService;
import com.example.recode.service.QnAService;
import com.example.recode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;
    private final QnAService qnAService;
    private final UserService userService;


    //상품 상세 페이지
    @GetMapping("/product/productDetail/{productId}")
    public String showProductDetails(@PathVariable long productId, Model model, Principal principal){

        model.addAttribute("product", productService.getProductInfoByProductId(productId, principal));
        model.addAttribute("QnAPages", qnAService.getQnAByProductIdPaging(productId, 10));
        model.addAttribute("QnATotalSize", qnAService.QnATotalSize(productId));
        model.addAttribute("userId", principal != null ? userService.getUserId(principal) : null);
        model.addAttribute("principal", principal);
        return "product/productDetail";
    }

    //상품 그룹(카테고리 선택, 상품 검색, 최근본 상품)
    @GetMapping("/product/productGroup")
    public String showProductGroup(@RequestParam(required = false) String searchText, @RequestParam(required = false) String productCategory, @RequestParam(required = false) Integer recentViewProduct, @PageableDefault(page = 0, size = 12) Pageable pageable, Model model, Principal principal){
        if(recentViewProduct != null){
            model.addAttribute("products", productService.getRecentViewProductList(principal));
        }
        else{
            model.addAttribute("products", productService.searchProduct(searchText, productCategory, pageable));
        }
        return "product/productGroup";
    }
}
