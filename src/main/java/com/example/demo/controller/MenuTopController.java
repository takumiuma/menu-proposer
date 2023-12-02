package com.example.demo.controller;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.MenuDao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MenuTopController {

	private final MenuDao menuDao;
	
	private final SecurityContextLogoutHandler logoutHandler;

	@Autowired
	public MenuTopController(MenuDao menuDao,SecurityContextLogoutHandler logoutHandler) {
		this.menuDao = menuDao;
		this.logoutHandler = logoutHandler;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@PreAuthorize("permitAll")
    public String redirectToMenu() {
        return "redirect:/menu";
    }
	
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	@PreAuthorize("permitAll")
	public String showMenuForm(Model model) {
		String[] genres = { "和食", "中華料理", "洋食", "韓国料理", "ファーストフード", "その他" };
		String[] categories = { "肉", "魚", "野菜", "ご飯もの", "麺類", "パン", "スープ・汁物", "その他" };
		model.addAttribute("genre", genres);
		model.addAttribute("category", categories);
		return "menuTop";
	}

	@RequestMapping(value = "/menu", method = RequestMethod.POST)
	@PreAuthorize("permitAll")
	public String processMenuForm(Model model, 
			@RequestParam(value = "全検索", required = false) String all,
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
					ArrayList<String> menuList = menuDao.getOneMenu();
					model.addAttribute("menuList", menuList);
					showMenuForm(model);
				} else if (genres == null || categories == null || genres.length == 0 || categories.length == 0) {
					message = "少なくともジャンルかカテゴリに１つずつチェックをつけてください";
					model.addAttribute("message", message);
					model.addAttribute("genre", genres);
					model.addAttribute("category", categories);
				} else if (detail != null) {
					// 詳細検索の処理
					ArrayList<String> menuList = menuDao.getMenuList(genres, categories, count);
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
	
	@RequestMapping("/login")
	@PreAuthorize("permitAll") 
	public ModelAndView login(ModelAndView mav,@RequestParam(value="error",required=false) String error) {
		mav.setViewName("login");
		if(error != null) {
			mav.addObject("msg", "ログインできませんでした。");
		}else {
			mav.addObject("msg", "ユーザー名とパスワードを入力：");
		}
		return mav;
	}
	
	@RequestMapping("/mypage")
	@PreAuthorize("isAuthenticated()")
	public ModelAndView secret(ModelAndView mav,HttpServletRequest request) {
		String user = request.getRemoteUser(); 
		String msg = "ユーザー：\"" + user + "\"でログイン中...";
		mav.setViewName("mypage");
		mav.addObject("title", "マイページ");
		mav.addObject("msg", msg);
		return mav;
	}
	
	@RequestMapping("/logout")  
	@PreAuthorize("permitAll") 
	public String logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		this.logoutHandler.logout(request, response, authentication);
		return "redirect:/menu"; 
	}
	
}
