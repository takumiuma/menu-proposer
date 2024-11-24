package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.MenuResponse;
import com.example.demo.service.MenuService;


@RestController
public class MenuRestController {
	
    private final MenuService menuService;
	
	public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
	}
	@RequestMapping(value = "/v1/menus", method = RequestMethod.GET)
	@CrossOrigin(value={"http://localhost:3000/"})
    public Map<String, List<MenuResponse>> posts() {
		// レスポンス用に構造体変換
        List<MenuResponse> restMenus = menuService.getAllMenus();
        Map<String, List<MenuResponse>> response = new HashMap<>();
        response.put("menus", restMenus);
        return response;
    }
}
