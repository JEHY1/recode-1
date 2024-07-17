package com.example.recode.controller.join;

import com.example.recode.domain.User;
import com.example.recode.dto.*;
import com.example.recode.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    @GetMapping("/join/new") // join 페이지
    public String newJoin() {
        return "logins/join";
    }

    @PostMapping("/join/create") // join 페이지 post
    public String createJoin(JoinRequest request) {
        Long userId = userService.save(request);
        return "redirect:/join/" + userId + "/finish";
    }
    @PostMapping("/join/idCheck") // 아이디 중복 확인 - join.js에 ajax 연결
    @ResponseBody
    public String idCheck(String username) {

        System.out.println("아이디 확인: " + username);

        return userService.idCheck(username);
    }

    @PostMapping("/join/phoneCheck") // 연락처 중복 확인 - join.js에 ajax 연결
    @ResponseBody
    public String phoneCheck(String userPhone) {

        System.out.println("휴대폰번호 확인: " + userPhone);

        return userService.phoneCheck(userPhone);
    }
    @PostMapping("/join/emailCheck") // 이메일 중복 확인 - join.js에 ajax 연결
    @ResponseBody
    public String emailCheck(String userEmail) {

        System.out.println("이메일 확인: " + userEmail);

        return userService.emailCheck(userEmail);
    }

    @GetMapping("/join/{userId}/finish") // join_finish 페이지
    public String showJoinFinish(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);
        return "logins/join_finish";
    }

    @GetMapping("/login") // login 페이지
    public String login() {
        return "logins/login";
    }

    @PostMapping("/login/check") // 로그인 시 아이디, 비밀번호 확인 - login.js에 ajax 연결
    @ResponseBody
    public String loginCheck(String username, String userPassword) {

        System.out.println("아이디 확인: " + username);
        System.out.println("비밀번호 확인: " + userPassword);

        return userService.loginCheck(username, userPassword);
    }

    @GetMapping("/logout") // logout 시키기
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping("/find/id/new") // find_id 페이지
    public String newFindId() {
        return "logins/find_id";
    }

    @PostMapping("find/id/create") // find_id 페이지 post
    public String createFindId(FindIdRequest request, RedirectAttributes rttr) {
        User userEntity = userService.findByUserEmail(request.getUserEmail());
        if(userEntity != null && request.getUserRealName().equals(userEntity.getUserRealName())) {
            return "redirect:/find/id/" + userEntity.getUserId() + "/finish";
        }
        else {
            rttr.addFlashAttribute("msg", "아이디를 찾을 수 없습니다.");
            return "redirect:/find/id/new";
        }
    }

    @GetMapping("/find/id/{userId}/finish") // find_id_finish 페이지
    public String showFindIdFinish(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);
        return "logins/find_id_finish";
    }

    @GetMapping("/find/pw/new") // find_pw 페이지
    public String newFindPw() {
        return "logins/find_pw";
    }

    @PostMapping("/find/pw/create") // find_pw 페이지 post
    public String createFindPw(FindPwRequest request, RedirectAttributes rttr) {
        User userEntity = userService.findByUserEmail(request.getUserEmail());
        if(userEntity != null && request.getUsername().equals(userEntity.getUsername()) && request.getUserRealName().equals(userEntity.getUserRealName())) {
            return "redirect:/find/pw/" + userEntity.getUserId() + "/finish/new";
        }
        else {
            rttr.addFlashAttribute("msg", "사용자 정보가 일치하지 않습니다.");
            return "redirect:/find/pw/new";
        }

    }

    @GetMapping("/find/pw/{userId}/finish/new") // find_pw_finish 페이지
    public String newFindPwFinish(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);
        return "logins/find_pw_finish";
    }

    @PostMapping("/find/pw/finish/create")
    public String createFindPwFinish(ResetPasswordRequest request, RedirectAttributes rttr) { // find_pw_finish 페이지 post
        userService.resetPassword(request); // 비밀번호 재설정
        rttr.addFlashAttribute("msg", "비밀번호가 재설정 되었습니다.");
        return "redirect:/login";
    }


}
