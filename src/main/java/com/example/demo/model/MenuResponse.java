package com.example.demo.model;

import java.util.List;

public class MenuResponse {
	private Integer menuId;
	private String menuName;
	private List<Integer> genreIds;
	private List<Integer> categoryIds;

	public MenuResponse(Integer menuId, String menuName, List<Integer> genreIds, List<Integer> categoryIds) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.genreIds = genreIds;
		this.categoryIds = categoryIds;
	}

	// Getters and Setters
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public List<Integer> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(List<Integer> genreIds) {
		this.genreIds = genreIds;
	}

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}
}
