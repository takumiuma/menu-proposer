package com.example.demo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.MenuService;

@Controller
public class MenuManageController {

    private final MenuService menuService;

    public MenuManageController(MenuService menuService) {
        this.menuService = menuService;
    }

    /** メニュー追加フォーム表示（ROLE_ADMIN のみ） */
    @RequestMapping(value = "/addMenu", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddMenuForm(Model model) {
        model.addAttribute("allGenres", menuService.getAllGenres());
        model.addAttribute("allCategories", menuService.getAllCategories());
        return "addMenu";
    }

    /** メニュー追加処理（ROLE_ADMIN のみ） */
    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public String addMenu(
            @RequestParam("menuName") String menuName,
            @RequestParam(value = "genreIds", required = false) List<Integer> genreIds,
            @RequestParam(value = "categoryIds", required = false) List<Integer> categoryIds,
            RedirectAttributes redirectAttributes) {
        try {
            if (menuName == null || menuName.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMsg", "メニュー名を入力してください。");
                return "redirect:/addMenu";
            }
            if (genreIds == null || genreIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMsg", "ジャンルを1つ以上選択してください。");
                return "redirect:/addMenu";
            }
            if (categoryIds == null || categoryIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMsg", "カテゴリを1つ以上選択してください。");
                return "redirect:/addMenu";
            }
            menuService.addMenu(menuName.trim(), genreIds, categoryIds);
            redirectAttributes.addFlashAttribute("successMsg", "「" + menuName + "」を追加しました。");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "追加中にエラーが発生しました。");
        }
        return "redirect:/addMenu";
    }
}
