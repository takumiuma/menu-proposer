package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserRegistrationController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserRegistrationController(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    /** 登録フォーム表示 */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public String showRegisterForm() {
        return "register";
    }

    /** ユーザー登録処理 */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @PreAuthorize("permitAll")
    public String register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        // 入力チェック
        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "ユーザー名を入力してください。");
            return "redirect:/register";
        }
        if (password == null || password.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMsg", "パスワードは6文字以上で入力してください。");
            return "redirect:/register";
        }
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMsg", "パスワードが一致しません。");
            return "redirect:/register";
        }

        // ユーザー名の重複チェック
        if (userDetailsManager.userExists(username.trim())) {
            redirectAttributes.addFlashAttribute("errorMsg", "そのユーザー名はすでに使われています。");
            return "redirect:/register";
        }

        // 一般ユーザー（ROLE_USER）として登録
        UserDetails newUser = User.withUsername(username.trim())
                .password(passwordEncoder.encode(password))
                .roles("USER")   // → DB に "ROLE_USER" で保存される
                .build();
        userDetailsManager.createUser(newUser);

        redirectAttributes.addFlashAttribute("successMsg", "登録が完了しました。ログインしてください。");
        return "redirect:/login";
    }
}
