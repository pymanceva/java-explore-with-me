package ru.practicum.ewm.main.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.category.model.Category;
import ru.practicum.ewm.main.category.model.dto.CategoryDto;
import ru.practicum.ewm.main.category.model.dto.NewCategoryDto;
import ru.practicum.ewm.main.category.model.mapper.CategoryMapper;
import ru.practicum.ewm.main.category.repository.CategoryRepository;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exception.ConflictException;
import ru.practicum.ewm.main.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.mapToCategory(newCategoryDto);
        try {
            repository.save(category);
            log.info("New category id " + category.getId() + " has been saved.");
            return CategoryMapper.mapToCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Name is not unique " + category.getName());
        }
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = repository.findById(catId).orElseThrow(() ->
                new NotFoundException("Category with id=" + catId + " was not found"));

        category.setName(newCategoryDto.getName());

        try {
            log.info("Existed category id " + category.getId() + " has been updated.");
            return CategoryMapper.mapToCategoryDto(repository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Name is not unique " + category.getName());
        }
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long catId) {
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new ConflictException("The category is not empty");
        }
        int result = repository.deleteCategoryById(catId);

        if (result == 0) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }

        log.info("Existed category id " + catId + " has been deleted.");
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        return CategoryMapper.mapToCategoryDto(repository.findAll(PageRequest.of(from, size)));
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.mapToCategoryDto(repository.findById(catId).orElseThrow(() ->
                new NotFoundException("Category with id=" + catId + " was not found")));
    }


}
