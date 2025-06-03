package com.example.runity.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OAuth2LoginController {
    @GetMapping("/login/success")
    @ResponseBody
    public String loginSuccess(HttpSession session) {
        String token = (String) session.getAttribute("jwt_token");
        return "카카오 로그인 성공!<br>JWT Token: " + token;
    }
}
