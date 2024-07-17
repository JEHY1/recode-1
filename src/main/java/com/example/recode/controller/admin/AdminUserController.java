package com.example.recode.controller.admin;

import com.example.recode.domain.Address;
import com.example.recode.domain.User;
import com.example.recode.dto.AdminUserRequest;
import com.example.recode.dto.NoticeViewResponse;
import com.example.recode.service.AddressService;
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
public class AdminUserController {

    final private UserService userService;
    final private AddressService addressService;

    @GetMapping("/admin/user/{userId}/show") // admin_user_show 회원정보 보기 페이지
    public String showAdminUser(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);
        List<Address> addressList = addressService.findAddressByUserId(userId);
        addressList.sort((o1, o2) -> o2.getAddressDefault() - o1.getAddressDefault()); // addressDefault 기준 내림차순 정렬
        model.addAttribute("addresses", addressList);

        return "admins/admin_user_show";
    }

    @GetMapping("/admin/user/address/{addressId}/delete")
    public String deleteAdminUserAddress(@PathVariable Long addressId, RedirectAttributes rttr) {
        long userId = addressService.findAddressByAddressId(addressId).getUserId();
        addressService.deleteAddressByAddressId(addressId);
        rttr.addFlashAttribute("msg", "배송지가 삭제 되었습니다.");
        return "redirect:/admin/user/" + userId + "/edit";
    }

    @GetMapping("/admin/user/{userId}/edit") // admin_user_edit 회원정보 수정 페이지
    public String editAdminUser(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);
        List<Address> addressList = addressService.findAddressByUserId(userId);
        addressList.sort((o1, o2) -> o2.getAddressDefault() - o1.getAddressDefault()); // addressDefault 기준 내림차순 정렬
        model.addAttribute("addresses", addressList);

        return "admins/admin_user_edit";
    }

    @PostMapping("/admin/user/update") // admin_user_edit 회원정보 수정 post
    public String updateAdminUser(AdminUserRequest request, RedirectAttributes rttr) {
        addressService.updateAdminUserAddress(request);
        rttr.addFlashAttribute("msg", "회원정보가 수정 되었습니다.");

        return "redirect:/admin/user/" + request.getUserId() + "/show";
    }

    @GetMapping("/admin/user/{userId}/delete")
    public String deleteAdminUser(@PathVariable Long userId, RedirectAttributes rttr, Integer page, Integer group, Integer category, String searchKeyword) throws UnsupportedEncodingException {
        if(userService.findById(userId).getUserDeleteDate() == null) {
            userService.deleteUser(userId);
            rttr.addFlashAttribute("msg", "회원이 탈퇴 되었습니다.");
        }
        else {
            rttr.addFlashAttribute("msg", "이미 탈퇴된 회원입니다.");
        }
        String encodeSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8); // ASCII 아닌 파라미터 percent encoding
        return "redirect:/admin/user/index?page=" + page + "&group=" + group + "&category=" + category + "&searchKeyword=" + encodeSearchKeyword;
    }

    @GetMapping("/admin/user/index") // admin_user_index 회원 목록 페이지
    public String indexAdminUser(Model model, @PageableDefault(page = 0, size = 10, sort = "userRealName", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) Integer isDel, @RequestParam(defaultValue = "1") Integer group, @RequestParam(defaultValue = "0") Integer category, @RequestParam(defaultValue = "") String searchKeyword) {
        // default 페이지, 한 페이지 게시글 수, 정렬기준 컬럼, 정렬순서
        model.addAttribute("group", group);
        model.addAttribute("category", category);
        model.addAttribute("searchKeyword", searchKeyword);

        Page<User> userList = null;
        if(searchKeyword == null || searchKeyword.equals("")) { // 검색 안했을 때
            userList = userService.userList(group, pageable);
        }
        else { // searchKeyword 로 검색 됐을 때
            userList = userService.userSearchList(group, category, searchKeyword, pageable);
        }
        model.addAttribute("users", userList);

        // 페이징 관련 변수
        int nowPage = userList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = userList.getTotalPages() == 0 ? 1 : userList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        if(isDel != null && isDel == 1) { // 체크해서 삭제 했을 때 팝업창
            model.addAttribute("msg", "회원이 탈퇴 되었습니다.");
        }
        if(isDel != null && isDel == 0) { // 체크 안하고 삭제 눌렀을 때 팝업창
            model.addAttribute("msg", "탈퇴할 회원을 선택해주세요.");
        }

        return "admins/admin_user_index";

    }

    @PostMapping("/admin/user/delete")
    @ResponseBody
    public String deleteAdminUserSelect(@RequestParam(required = false) List<Long> userIds) {
        if(userIds != null) {
            userService.deleteByIds(userIds);
            return "delete";
        }
        else {
            return "null";
        }
    }



}
