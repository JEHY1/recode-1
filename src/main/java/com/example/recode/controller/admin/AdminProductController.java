package com.example.recode.controller.admin;

import com.example.recode.domain.Notice;
import com.example.recode.domain.Product;
import com.example.recode.domain.ProductImg;
import com.example.recode.dto.UploadProductRequest;
import com.example.recode.service.ProductService;
import com.example.recode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final UserService userService;

    @GetMapping(value = {"/admin/product/insert", "/admin/product/{productId}/insert"}) // admin_product_insert 상품 등록&수정 페이지
    public String newAdminProduct(@PathVariable(required = false) Long productId, Model model) {
        if(productId != null) { // 상품정보 수정
            Product productEntity = productService.findProductByProductId(productId);
            model.addAttribute("product", productEntity);
            List<ProductImg> productImgList = productService.findProductImgByProductId(productId);
            model.addAttribute("productImgs", productImgList);
        }

        return "admins/admin_product_insert";
    }

    @PostMapping("/admin/product/upload") // 상품 업로드&수정
    public String uploadProduct(UploadProductRequest request, RedirectAttributes rttr){
        Product product = productService.uploadProduct(request);
        if(request.getProductId() == null) {
            rttr.addFlashAttribute("msg", "상품이 등록 되었습니다.");
        }
        else {
            rttr.addFlashAttribute("msg", "상품정보가 수정 되었습니다.");
        }
        return "redirect:/admin/product/" + product.getProductId() + "/show";
    }

    @GetMapping("/admin/product/{productId}/delete") // 상품 판매보류
    public String deleteAdminNotice(@PathVariable Long productId, RedirectAttributes rttr, Integer page, Integer group, Integer category, String searchKeyword) throws UnsupportedEncodingException {
        if(productService.findProductByProductId(productId).getProductSold() == 2) {
            rttr.addFlashAttribute("msg", "이미 판매보류된 상품입니다.");
        }
        else {
            productService.updateHoldById(productId);
            rttr.addFlashAttribute("msg", "상품이 판매보류 되었습니다.");
        }
        String encodeSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8); // ASCII 아닌 파라미터 percent encoding
        return "redirect:/admin/product/index?page=" + page + "&group=" + group + "&category=" + category + "&searchKeyword=" + encodeSearchKeyword;
    }

    @GetMapping("/admin/product/{productId}/show") // admin_product_show 상품 보기 페이지
    public String showAdminProduct(@PathVariable Long productId, Model model) {
        Product productEntity = productService.findProductByProductId(productId);
        model.addAttribute("product", productEntity);
        List<ProductImg> productImgList = productService.findProductImgByProductId(productId);
        model.addAttribute("productImgs", productImgList);
        return "admins/admin_product_show";
    }

    @GetMapping("/admin/product/index") // admin_product_index 상품 목록 페이지
    public String indexAdminProduct(Model model, @PageableDefault(page = 0, size = 10, sort = "productId", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) Integer isDel, @RequestParam(defaultValue = "3") Integer group, @RequestParam(defaultValue = "0") Integer category, @RequestParam(defaultValue = "") String searchKeyword) {
        // default 페이지, 한 페이지 게시글 수, 정렬기준 컬럼, 정렬순서
        model.addAttribute("group", group);
        model.addAttribute("category", category);
        model.addAttribute("searchKeyword", searchKeyword);

        Page<Product> productList = null;
        if(searchKeyword == null || searchKeyword.equals("")) { // 검색 안했을 때
            productList = productService.productList(group, pageable);
        }
        else { // searchKeyword 로 검색 됐을 때
            productList = productService.productSearchList(group, category, searchKeyword, pageable);
        }
        model.addAttribute("products", productList);

        // 페이징 관련 변수
        int nowPage = productList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = productList.getTotalPages() == 0 ? 1 : productList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        if(isDel != null && isDel == 1) { // 체크해서 삭제 했을 때 팝업창
            model.addAttribute("msg", "상품이 판매보류 되었습니다.");
        }
        if(isDel != null && isDel == 0) { // 체크 안하고 삭제 눌렀을 때 팝업창
            model.addAttribute("msg", "판매보류할 상품을 선택해주세요.");
        }

        return "admins/admin_product_index";
    }

    @PostMapping("/admin/product/delete")
    @ResponseBody
    public String deleteAdminNoticeSelect(@RequestParam(required = false) List<Long> productIds) {
        if(productIds != null) {
            productService.updateHoldByIds(productIds);
            return "delete";
        }
        else {
            return "null";
        }
    }
}
