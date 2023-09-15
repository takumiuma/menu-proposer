package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.MenuRepository;

@Service
public class MenuService {
	@Autowired
	MenuRepository menuRepository;

	public ArrayList<String> requestOne() {
		List<String> result = menuRepository.getOneMenu();
		ArrayList<String> menuList = new ArrayList<>();
		menuList.addAll(result);
		return menuList;
	}

	public ArrayList<String> detailRequest(String[] genres, String[] categories, Integer count) {
		List<String> result = menuRepository.getMultiMenu(genres, categories, count);
		ArrayList<String> menuList = new ArrayList<>();
		menuList.addAll(result);
		return menuList;
	}
}
