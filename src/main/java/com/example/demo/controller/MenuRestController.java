package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.MenuDao;
import com.example.demo.model.Menu;


@RestController
public class MenuRestController {
	
	private final MenuDao menuDao;
	
	@Autowired
	public MenuRestController(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
	@RequestMapping(value = "/v1/menus", method = RequestMethod.GET)
	@CrossOrigin(value={"http://localhost:3000/"})
    public Map<String, List<Menu>> posts() {
		// メニュー全件取得
		List<Map<String, Object>> menus = menuDao.getAllMenu();
		// レスポンス用に構造体変換
		List<Menu> restMenus = convertToMenus(menus);
        Map<String, List<Menu>> response = new HashMap<>();
        response.put("menus", restMenus);
        return response;
    }
	//TODO: いずれロジック層を作って、そこで変換するようにする
    private List<Menu> convertToMenus(List<Map<String, Object>> rows) {
        List<Menu> menus = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Menu menu = new Menu();
            menu.setMenuId((Integer) row.get("menu_id"));
            menu.setMenuName((String) row.get("menu_name"));
            menu.setEatingGenreId((Integer) row.get("eating_genre_id"));
            menu.setEatingCategoryId((Integer) row.get("eating_category_id"));
            menus.add(menu);
        }
        return menus;
    }
	
	
}
