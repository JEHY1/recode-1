package com.example.recode.service;

import com.example.recode.domain.Notice;
import com.example.recode.domain.User;
import com.example.recode.dto.*;
import com.example.recode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long getUserId(Principal principal){ // 로그인 된 사용자 UserId 가져오기
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("not found user"))
                .getUserId();
    }
    public User getUserByPrincipal(Principal principal) { // 로그인 된 사용자 User Entity 가져오기
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }

    public Long save(JoinRequest dto) { // 회원가입 - 회원정보 등록
        return userRepository.save(User.builder()
                .username(dto.getUsername())
                .userPassword(bCryptPasswordEncoder.encode(dto.getUserPassword()))
                .userRealName(dto.getUserRealName())
                .userPhone(dto.getUserPhone1() + "-" + dto.getUserPhone2() + "-" + dto.getUserPhone3())
                .userEmail(dto.getUserEmail())
                .userSmsAgreement(dto.getUserSmsAgreement())
                .userEmailAgreement(dto.getUserEmailAgreement())
                .userRole("COSTOMER")
                .build()).getUserId();
    }

    public User findById(Long userId) { // userId로 User 가져오기
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }

    public User findByUserEmail(String userEmail) { // userEmail로 User 가져오기
        return userRepository.findByUserEmail(userEmail).orElse(null);
    }

    public String getUsername(Long userId){ // userId로 Username 가져오기
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found user"))
                .getUsername();
    }

    public String idCheck(String username) { // 회원가입 시 아이디 중복 체크 - join.js에 ajax 연결
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null) {
            return "available"; // 아이디 사용가능
        }

        return "duplicated"; // 아이디 중복
    }

    public String phoneCheck(String userPhone) { // 회원가입 시 연락처 중복 체크 - join.js에 ajax 연결
        User user = userRepository.findByUserPhone(userPhone).orElse(null);
        if(user == null) {
            return "available"; // 휴대폰번호 사용가능
        }

        return "duplicated"; // 휴대폰번호 중복
    }

    public String emailCheck(String userEmail) { // 회원가입 시 이메일 중복 체크 - join.js에 ajax 연결
        User user = findByUserEmail(userEmail);
        if(user == null) {
            return "available"; // 이메일 사용가능
        }

        return "duplicated"; // 이메일 중복
    }

    public String loginCheck(String username, String userPassword) { // 로그인 시 아이디, 비밀번호 확인 - login.js에 ajax 연결
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null || user.getUserDeleteDate() != null) { // 없는 user 이거나 탈퇴한 user 일 경우
            return "error_id"; // 아이디 오류
        }
        else {

            if(bCryptPasswordEncoder.matches(userPassword, user.getUserPassword())) { // 비밀번호 일치 확인
                return "success_login"; // 로그인 성공
            }
            else {
                return "error_pw"; // 비밀번호 오류
            }
        }
    }


    public String pwCheck(String userPassword, Principal principal) { // 기존 비밀번호 일치 여부 확인 - user_information_confirm/modify.js, user_delete_confirm.js에 ajax 연결
        boolean pwCheck = bCryptPasswordEncoder.matches(userPassword, getUserByPrincipal(principal).getUserPassword()); // 기존 비밀번호 일치 여부 확인
        if(pwCheck) {
            return "accord"; // 비밀번호 일치
        }
        return "discord"; // 비밀번호 불일치
    }

    @Transactional
    public void updatePassword(UpdatePasswordRequest dto, Principal principal) { // 회원정보 수정에서 비밀번호 변경
        getUserByPrincipal(principal).updatePassword(bCryptPasswordEncoder.encode(dto.getUserPassword()));
    }

    @Transactional
    public void updateUser(UpdateUserRequest dto, Principal principal) { // 회원정보 수정(연락처, 이메일)
        getUserByPrincipal(principal).updateUser(dto);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest dto) { // 비밀번호 찾기에서 비밀번호 재설정
        findById(dto.getUserId()).updatePassword(bCryptPasswordEncoder.encode(dto.getUserPassword()));
    }

    public String phoneReCheck(String userPhone, Principal principal) { // 회원정보 수정 시 기존 연락처 및 중복 체크 - user_information_modify.js에 ajax 연결
        User user = userRepository.findByUserPhone(userPhone).orElse(null);
        if(user == null) {
            return "available"; // 연락처 사용가능
        }
        else {
            if(userPhone.equals(getUserByPrincipal(principal).getUserPhone())) {
                return "equal"; // 기존 연락처와 동일
            }
            else {
                return "duplicated"; // 연락처 중복
            }
        }

    }

    public String emailReCheck(String userEmail, Principal principal) { // 회원정보 수정 시 기존 이메일 및 중복 체크 - user_information_modify.js에 ajax 연결
        User user = userRepository.findByUserEmail(userEmail).orElse(null);
        if(user == null) {
            return "available"; // 이메일 사용가능
        }
        else {
            if(userEmail.equals(getUserByPrincipal(principal).getUserEmail())) {
                return "equal"; // 기존 이메일과 동일
            }
            else {
                return "duplicated"; // 이메일 중복
            }
        }
    }
    @Transactional
    public void deleteUser(Principal principal) { // 로그인 된 사용자 userDeleteDate 추가하기 - 회원 탈퇴
        getUserByPrincipal(principal).deleteUser();
    }

    @Transactional
    public void deleteUser(Long userId) { // userId로 userDeleteDate 추가하기 - 관리자페이지에서 회원 탈퇴
        findById(userId).deleteUser();
    }

    public List<User> findUserByUsernameContaining(String username){
        return userRepository.findByUsernameContaining(username)
                .orElse(null);
    }

    public List<User> findAll() { // 전체회원 List<User> 가져오기
        return userRepository.findAll();
    }
    public List<User> findJoinUser() { // 가입회원 이름순 정렬하여 List<User> 가져오기
        return userRepository.findByUserDeleteDateIsNull()
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }
    public List<User> findDeleteUser() { // 탈퇴회원 이름순 정렬하여 List<User> 가져오기
        return userRepository.findByUserDeleteDateNotNull()
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }


    public Page<User> userList(Integer group, Pageable pageable) { // group 별 회원 조회
        Page<User> userList = null;
        if(group == 1) { // 가입회원(1) 조회
            userList = userRepository.findByUserDeleteDateIsNull(pageable)
                    .orElseThrow(() -> new IllegalArgumentException("not found user"));
        }
        else if(group == 2) { // 탈퇴회원(2) 조회
            userList = userRepository.findByUserDeleteDateNotNull(pageable)
                    .orElseThrow(() -> new IllegalArgumentException("not found user"));
        }
        else if(group == 3) { // 전체회원(3) 조회
            userList = userRepository.findAll(pageable);
        }
        return userList;
    }

    public Page<User> userSearchList(Integer group, Integer category, String searchKeyword, Pageable pageable) { // group 별 category 선택 후 검색해서 페이징 처리한 Page<User> 가져옴
        Page<User> userSearchList = null;
        if(group == 1) { // 가입회원(1) 일 경우
            if(category == 1) {  // category 가 '아이디'일 때
                userSearchList = userRepository.findByUsernameContainingAndUserDeleteDateIsNull(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userName 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 2) {  // category 가 '이름'일 때
                userSearchList = userRepository.findByUserRealNameContainingAndUserDeleteDateIsNull(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userRealName 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 3) {  // category 가 '연락처'일 때
                userSearchList = userRepository.findByUserPhoneContainingAndUserDeleteDateIsNull(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userPhone 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 4) {  // category 가 '이메일'일 때
                userSearchList = userRepository.findByUserEmailContainingAndUserDeleteDateIsNull(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userEmail 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 5) { // category 가 '가입일'일 때
                // searchKeyword 가 포함된 List<User> 만듬
                List<User> keywordList = new ArrayList<>();
                findJoinUser().forEach(user -> {
                    if(user.getUserSubmitDate().toString().contains(searchKeyword)){
                        keywordList.add(user);
                    }
                });
                keywordList.sort((o1, o2) -> o2.getUserRealName().compareTo(o1.getUserRealName())); // userRealName 기준 오름차순 정렬
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), keywordList.size());
                userSearchList = new PageImpl<>(keywordList.subList(start, end), pageable, keywordList.size()); // Page<User> 객체 만듬
            }
        }
        else if(group == 2) { // 탈퇴회원(2) 일 경우
            if(category == 1) {  // category 가 '아이디'일 때
                userSearchList = userRepository.findByUsernameContainingAndUserDeleteDateNotNull(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userName 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 2) {  // category 가 '이름'일 때
                userSearchList = userRepository.findByUserRealNameContainingAndUserDeleteDateNotNull(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userRealName 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 3) {  // category 가 '연락처'일 때
                userSearchList = userRepository.findByUserPhoneContainingAndUserDeleteDateNotNull(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userPhone 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 4) {  // category 가 '이메일'일 때
                userSearchList = userRepository.findByUserEmailContainingAndUserDeleteDateNotNull(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userEmail 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 5) { // category 가 '가입일'일 때
                // searchKeyword 가 포함된 List<User> 만듬
                List<User> keywordList = new ArrayList<>();
                findJoinUser().forEach(user -> {
                    if(user.getUserSubmitDate().toString().contains(searchKeyword)){
                        keywordList.add(user);
                    }
                });
                keywordList.sort((o1, o2) -> o2.getUserRealName().compareTo(o1.getUserRealName())); // userRealName 기준 오름차순 정렬
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), keywordList.size());
                userSearchList = new PageImpl<>(keywordList.subList(start, end), pageable, keywordList.size()); // Page<User> 객체 만듬
            }
            else if(category == 6) { // category 가 '탈퇴일'일 때
                // searchKeyword 가 포함된 List<User> 만듬
                List<User> keywordList = new ArrayList<>();
                findDeleteUser().forEach(user -> {
                    if(user.getUserDeleteDate().toString().contains(searchKeyword)){
                        keywordList.add(user);
                    }
                });
                keywordList.sort((o1, o2) -> o2.getUserRealName().compareTo(o1.getUserRealName())); // userRealName 기준 오름차순 정렬
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), keywordList.size());
                userSearchList = new PageImpl<>(keywordList.subList(start, end), pageable, keywordList.size()); // Page<User> 객체 만듬
            }
        }
        else if(group == 3) { // 전체회원(3) 일 경우
            if(category == 1) {  // category 가 '아이디'일 때
                userSearchList = userRepository.findByUsernameContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userName 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 2) {  // category 가 '이름'일 때
                userSearchList = userRepository.findByUserRealNameContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userRealName 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 3) {  // category 가 '연락처'일 때
                userSearchList = userRepository.findByUserPhoneContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userPhone 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 4) {  // category 가 '이메일'일 때
                userSearchList = userRepository.findByUserEmailContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found user")); // userEmail 으로 검색해서 페이징 처리한 Page<User>
            }
            else if(category == 5) { // category 가 '가입일'일 때
                // searchKeyword 가 포함된 List<User> 만듬
                List<User> keywordList = new ArrayList<>();
                findAll().forEach(user -> {
                    if(user.getUserSubmitDate().toString().contains(searchKeyword)){
                        keywordList.add(user);
                    }
                });
                keywordList.sort((o1, o2) -> o2.getUserRealName().compareTo(o1.getUserRealName())); // userRealName 기준 오름차순 정렬
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), keywordList.size());
                userSearchList = new PageImpl<>(keywordList.subList(start, end), pageable, keywordList.size()); // Page<User> 객체 만듬
            }
            else if(category == 6) { // category 가 '탈퇴일'일 때
                // searchKeyword 가 포함된 List<User> 만듬
                List<User> keywordList = new ArrayList<>();
                findAll().forEach(user -> {
                    if(user.getUserDeleteDate().toString().contains(searchKeyword)){
                        keywordList.add(user);
                    }
                });
                keywordList.sort((o1, o2) -> o2.getUserRealName().compareTo(o1.getUserRealName())); // userRealName 기준 오름차순 정렬
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), keywordList.size());
                userSearchList = new PageImpl<>(keywordList.subList(start, end), pageable, keywordList.size()); // Page<User> 객체 만듬
            }
        }
        return userSearchList;
    }

    @Transactional
    public void deleteByIds(List<Long> userIds) { // userId로 userDeleteDate 추가하기 - 관리자페이지에서 회원 탈퇴
        for (Long userId : userIds) {
            findById(userId).deleteUser();
        }
    }
}
