package com.example.demo.model;

public class Menu {
	private int menuId;
	private String menuName;
	private int eatingGenreId;
	private int eatingCategoryId;

	// Getters and Setters
	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getEatingGenreId() {
		return eatingGenreId;
	}

	public void setEatingGenreId(int eatingGenreId) {
		this.eatingGenreId = eatingGenreId;
	}

	public int getEatingCategoryId() {
		return eatingCategoryId;
	}

	public void setEatingCategoryId(int eatingCategoryId) {
		this.eatingCategoryId = eatingCategoryId;
	}
}
