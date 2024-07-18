package com.example.recode.config;

import com.example.recode.service.UserDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userDetailService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomRequestCacheFilter customRequestCacheFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers("/css/**", "/js/**", "/images/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/**", "/join/**", "/find/**", "/", "/product/**", "/etc/**", "/user/delete/finish", "/checkLogin", "/community/**", "/getIncludeNameList").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        )
        .addFilterBefore(customRequestCacheFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin(in -> in
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler) // Custom success handler
                .defaultSuccessUrl("/")
        ).logout(out -> out
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        ).csrf(csrf -> csrf
                .disable()
        ).build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(userDetailService);

        return provider;
    }
}
