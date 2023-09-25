package ru.practicum.ewm.main.category.service;

import ru.practicum.ewm.main.category.model.dto.CategoryDto;
import ru.practicum.ewm.main.category.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto saveCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto);

    void deleteCategoryById(Long catId);

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);
}
