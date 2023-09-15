package com.example.demo.controller;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.MenuDao;

@Controller
public class MenuTopController {

	private final MenuDao menuDao;

	@Autowired
	public MenuTopController(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@GetMapping("/menu")
	public String showMenuForm(Model model) {
		String[] genres = { "和食", "中華料理", "洋食", "韓国料理", "ファーストフード", "その他" };
		String[] categories = { "肉", "魚", "野菜", "ご飯もの", "麺類", "パン", "スープ・汁物", "その他" };
		model.addAttribute("genre", genres);
		model.addAttribute("category", categories);
		return "menuTop";
	}

	@PostMapping("/menu")
	public String processMenuForm(Model model, @RequestParam(value = "全検索", required = false) String all,
			@RequestParam(value = "詳細検索", required = false) String detail,
			@RequestParam(value = "genre", required = false) String[] genres,
			@RequestParam(value = "category", required = false) String[] categories,
			@RequestParam(value = "count", required = false) Integer count) {
		String message = null;
		if (all == null && detail == null) {
			message = "不正アクセスです";
			model.addAttribute("message", message);
		} else {
			try {
				if (all != null) {
					// 全検索の処理
					//ArrayList<String> menuList = new MenuService().requestOne();
					ArrayList<String> menuList = menuDao.getMenuList();
					model.addAttribute("menuList", menuList);
					showMenuForm(model);
				} else if (genres == null || categories == null || genres.length == 0 || categories.length == 0) {
					message = "少なくともジャンルかカテゴリに１つずつチェックをつけてください";
					model.addAttribute("message", message);
					model.addAttribute("genre", genres);
					model.addAttribute("category", categories);
				} else if (detail != null) {
					// 詳細検索の処理
					//ArrayList<String> menuList = new MenuService().detailRequest(genres, categories, count);
					ArrayList<String> menuList = menuDao.getDetailList(genres, categories, count);
					model.addAttribute("menuList", menuList);
					model.addAttribute("genre", genres);
					model.addAttribute("category", categories);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "menuTop";
	}
}
