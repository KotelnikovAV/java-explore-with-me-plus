package ru.practicum.category.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IntegrityViolationException;
import ru.practicum.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Category addCategory(Category category) {
        categoryRepository.findCategoriesByNameContainingIgnoreCase(category.getName().toLowerCase()).ifPresent(c -> {
            throw new IntegrityViolationException("Category name " + category.getName() + " already exists");
        });
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category " + catId + " does not exist"));
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new IntegrityViolationException("Category " + catId + " already exists");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public Category updateCategory(long catId, Category newCategory) {
        Category updateCategory = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " does not exist"));
        categoryRepository.findCategoriesByNameContainingIgnoreCase(
                newCategory.getName().toLowerCase()).ifPresent(c -> {
            if (c.getId() != catId) {
                throw new IntegrityViolationException("Category name " + newCategory.getName() + " already exists");
            }
        });
        updateCategory.setName(newCategory.getName());
        return categoryRepository.save(updateCategory);
    }

    @Override
    public List<Category> getAllCategories(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        if (categories.hasContent()) {
            return categories.getContent();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Category getCategory(long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " does not exist"));
    }
}
