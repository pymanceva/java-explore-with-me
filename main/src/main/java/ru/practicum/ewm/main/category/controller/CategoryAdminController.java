package ru.practicum.ewm.main.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.category.model.dto.CategoryDto;
import ru.practicum.ewm.main.category.model.dto.NewCategoryDto;
import ru.practicum.ewm.main.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("POST/addCategory");
        return categoryService.saveCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("DELETE/catId");
        categoryService.deleteCategoryById(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable long catId, @RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("PATCH/catId");
        return categoryService.updateCategory(catId, newCategoryDto);
    }
}
