package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Category;
import com.example.demo.entity.Genre;
import com.example.demo.entity.Menu;
import com.example.demo.model.MenuResponse;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.MenuRepository;


@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final GenreRepository genreRepository;
    private final CategoryRepository categoryRepository;

    public MenuService(MenuRepository menuRepository,
                       GenreRepository genreRepository,
                       CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.genreRepository = genreRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<MenuResponse> getAllMenus() {
        List<Menu> entityMenus = menuRepository.findAllWithRelations();
        return convertToMenuResponseList(entityMenus);
    }

    /** 全ジャンル取得（addMenu フォーム用） */
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    /** 全カテゴリ取得（addMenu フォーム用） */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /** メニュー追加（ROLE_ADMIN のみ呼び出し可） */
    @Transactional
    public void addMenu(String menuName, List<Integer> genreIds, List<Integer> categoryIds) {
        Menu menu = new Menu();
        menu.setName(menuName);
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
        menu.setGenres(genres);
        menu.setCategories(categories);
        menuRepository.save(menu);
    }

    // TODO: いずれロジック層を作って、そこで変換するようにする
    private List<MenuResponse> convertToMenuResponseList(List<Menu> menus) {
        return menus.stream().map(menu -> new MenuResponse(
            menu.getId(),
            menu.getName(),
            menu.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList()),
            menu.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList())
        ))
        .collect(Collectors.toList()); // ここでストリームをリストに収集します
    }
}
