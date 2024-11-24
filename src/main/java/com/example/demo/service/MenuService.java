package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Genre;
import com.example.demo.entity.Menu;
import com.example.demo.model.MenuResponse;
import com.example.demo.repository.MenuRepository;


@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuResponse> getAllMenus() {
        List<Menu> entityMenus = menuRepository.findAllWithRelations();
        return convertToMenuResponseList(entityMenus);
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
