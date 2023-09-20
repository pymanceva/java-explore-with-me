package ru.practicum.ewm.main.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.category.model.dto.CategoryDto;
import ru.practicum.ewm.main.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                              @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("GET/getAllCategories");
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable Long catId) {
        log.info("GET/catId");
        return categoryService.getCategoryById(catId);
    }
}
