package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.MenuDao;

import reactor.core.publisher.Flux;

@RestController
public class MenuRestController {
	
	private final MenuDao menuDao;
	
	@Autowired
	public MenuRestController(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
	@RequestMapping(value = "/rest", method = RequestMethod.GET)
	@CrossOrigin(value={"http://localhost:3000/"})
    public Flux<Object> posts() {
		List<Map<String, Object>> menus = menuDao.getAllMenu();
		return Flux.fromArray(menus.toArray());
    }
	
	
}
