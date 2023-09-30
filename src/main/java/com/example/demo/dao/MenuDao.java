package com.example.demo.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class MenuDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MenuDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ArrayList<String> getOneMenu() {
        String sql = "SELECT menu_name FROM menu_list ORDER BY RAND() LIMIT 1";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        ArrayList<String> menuList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            menuList.add((String) row.get("menu_name"));
        }
        return menuList;
    }

    public ArrayList<String> getMenuList(String[] genres, String[] categories, Integer count) {
        String sql = "SELECT menu_name FROM menu_view WHERE genre_name IN (:genres) \n" 
        		+"AND category_name IN (:categories) ORDER BY RAND() LIMIT :count";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("genres", Arrays.asList(genres));
        paramMap.put("categories", Arrays.asList(categories));
        paramMap.put("count", count);

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(sql, paramMap);

        ArrayList<String> menuList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            menuList.add((String) row.get("menu_name"));
        }
        return menuList;
    }
}
