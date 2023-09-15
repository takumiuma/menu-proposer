package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.MenuEntity;
@Repository
public interface MenuRepository extends JpaRepository<MenuEntity,String> {
	@Query(value = "SELECT menu_name FROM menu_view WHERE genre_name IN :genres AND category_name IN :categories ORDER BY RAND() LIMIT :count", nativeQuery = true)
	List<String> getMultiMenu(
				@Param("genres") String[] genre ,
				@Param("categories") String[] categories ,
				@Param("count") Integer count
			);
	
	@Query(value = "SELECT menu_name FROM menu_view ORDER BY RAND() LIMIT 1", nativeQuery = true)
    List<String> getOneMenu();
	
}
