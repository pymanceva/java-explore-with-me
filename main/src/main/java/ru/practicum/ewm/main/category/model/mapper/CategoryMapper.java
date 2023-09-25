package ru.practicum.ewm.main.category.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.category.model.Category;
import ru.practicum.ewm.main.category.model.dto.CategoryDto;
import ru.practicum.ewm.main.category.model.dto.NewCategoryDto;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto mapToCategoryDto(Category category) {
        CategoryDto result = new CategoryDto();

        result.setId(category.getId());
        result.setName(category.getName());

        return result;
    }

    public static List<CategoryDto> mapToCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();

        for (Category category : categories) {
            result.add(mapToCategoryDto(category));
        }

        return result;
    }

    public static Category mapToCategory(CategoryDto categoryDto) {
        Category result = new Category();

        result.setId(categoryDto.getId());
        result.setName(categoryDto.getName());

        return result;
    }

    public static Category mapToCategory(NewCategoryDto newCategoryDto) {
        Category result = new Category();

        result.setName(newCategoryDto.getName());

        return result;
    }
}
