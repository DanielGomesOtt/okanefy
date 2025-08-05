package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.category.CategoriesListDTO;
import com.okanefy.okanefy.dto.category.CreatedCategoryDTO;
import com.okanefy.okanefy.dto.category.NewCategoryDTO;
import com.okanefy.okanefy.dto.category.UpdateCategoryDTO;
import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.models.Category;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.CategoryRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private CategoryService service;

    @Test
    @DisplayName("Should create a category successfully")
    void shouldCreateCategory() {
        NewCategoryDTO category = new NewCategoryDTO("category", "INCOME", 1L);
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        Category newCategory = new Category(category, user);
        newCategory.setId(1L);

        when(usersRepository.findById(category.user_id())).thenReturn(Optional.of(user));
        when(repository.save(any(Category.class))).thenReturn(newCategory);

        CreatedCategoryDTO result = service.save(category);

        assertEquals(1L, result.id());
        assertEquals("category", result.name());
        assertEquals("INCOME", result.type().toString());

        verify(usersRepository).findById(category.user_id());
        verify(repository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should delete a category")
    void shouldDeleteCategory() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        Category category = new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user);

        when(repository.findById(category.getId())).thenReturn(Optional.of(category));

        service.delete(1L);

        assertEquals(0, category.getStatus());
        verify(repository).findById(category.getId());
    }

    @Test
    @DisplayName("Should update a category")
    void shouldUpdateCategory() {
        UpdateCategoryDTO data = new UpdateCategoryDTO(1L, "category updated", "EXPENSE");
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        Category category = new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user);

        when(repository.findById(data.id())).thenReturn(Optional.of(category));

        CreatedCategoryDTO result = service.update(data);

        assertEquals(1L, category.getId());
        assertEquals("category updated", category.getName());
        assertEquals("EXPENSE", category.getType().toString());

        verify(repository).findById(data.id());
    }

    @Test
    @DisplayName("Should return categories using name and type")
    void shouldReturnCategoriesByNameAndType() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        List<Category> categories = List.of(new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user));

        when(repository.findAllByNameContainingIgnoreCaseAndTypeAndUserIdAndStatus(
                "category",
                CategoriesTypes.valueOf("INCOME"),
                1L,
                1)).thenReturn(Optional.of(categories));

        List<CategoriesListDTO> result = service.findAll(1L, "category", "INCOME");

        assertEquals(1, result.size());
        assertEquals("category", result.getFirst().name());
        assertEquals("INCOME", result.getFirst().type());

        verify(repository).findAllByNameContainingIgnoreCaseAndTypeAndUserIdAndStatus("category",
                CategoriesTypes.valueOf("INCOME"),
                1L,
                1);
    }

    @Test
    @DisplayName("Should return categories using name")
    void shouldReturnCategoriesByName() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        List<Category> categories = List.of(new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user));

        when(repository.findAllByNameContainingIgnoreCaseAndUserIdAndStatus(
                "category",
                1L,
                1)).thenReturn(Optional.of(categories));

        List<CategoriesListDTO> result = service.findAll(1L, "category", null);

        assertEquals(1, result.size());
        assertEquals("category", result.getFirst().name());

        verify(repository).findAllByNameContainingIgnoreCaseAndUserIdAndStatus("category",
                1L,
                1);
    }

    @Test
    @DisplayName("Should return categories using type")
    void shouldReturnCategoriesByType() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        List<Category> categories = List.of(new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user));

        when(repository.findAllByTypeAndUserIdAndStatus(
                CategoriesTypes.valueOf("INCOME"),
                1L,
                1)).thenReturn(Optional.of(categories));

        List<CategoriesListDTO> result = service.findAll(1L, null, "INCOME");

        assertEquals(1, result.size());
        assertEquals("INCOME", result.getFirst().type());

        verify(repository).findAllByTypeAndUserIdAndStatus(
                CategoriesTypes.valueOf("INCOME"),
                1L,
                1);
    }

    @Test
    @DisplayName("Should return categories without params")
    void shouldReturnCategoriesWithoutParams() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        List<Category> categories = List.of(new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user));

        when(repository.findAllByUserIdAndStatus(
                1L,
                1)).thenReturn(Optional.of(categories));

        List<CategoriesListDTO> result = service.findAll(1L, null, null);

        assertEquals(1, result.size());

        verify(repository).findAllByUserIdAndStatus(
                1L,
                1);
    }

    @Test
    @DisplayName("Should return an empty list using name and type")
    void shouldReturnEmptyListByNameAndType() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        List<Category> categories = List.of(new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user));

        when(repository.findAllByNameContainingIgnoreCaseAndTypeAndUserIdAndStatus(
                "category",
                CategoriesTypes.valueOf("INCOME"),
                1L,
                1)).thenReturn(Optional.empty());

        List<CategoriesListDTO> result = service.findAll(1L, "category", "INCOME");

        assertTrue(result.isEmpty());


        verify(repository).findAllByNameContainingIgnoreCaseAndTypeAndUserIdAndStatus("category",
                CategoriesTypes.valueOf("INCOME"),
                1L,
                1);
    }

    @Test
    @DisplayName("Should return an empty list using name")
    void shouldReturnEmptyListByName() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        List<Category> categories = List.of(new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user));

        when(repository.findAllByNameContainingIgnoreCaseAndUserIdAndStatus(
                "category",
                1L,
                1)).thenReturn(Optional.empty());

        List<CategoriesListDTO> result = service.findAll(1L, "category", null);

        assertTrue(result.isEmpty());

        verify(repository).findAllByNameContainingIgnoreCaseAndUserIdAndStatus("category",
                1L,
                1);
    }

    @Test
    @DisplayName("Should return an empty list using type")
    void shouldReturnEmptyListByType() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        List<Category> categories = List.of(new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user));

        when(repository.findAllByTypeAndUserIdAndStatus(
                CategoriesTypes.valueOf("INCOME"),
                1L,
                1)).thenReturn(Optional.empty());

        List<CategoriesListDTO> result = service.findAll(1L, null, "INCOME");

        assertTrue(result.isEmpty());

        verify(repository).findAllByTypeAndUserIdAndStatus(
                CategoriesTypes.valueOf("INCOME"),
                1L,
                1);
    }

    @Test
    @DisplayName("Should return an empty list without params")
    void shouldReturnEmptyListWithoutParams() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        List<Category> categories = List.of(new Category(1L, "category", CategoriesTypes.valueOf("INCOME"), 1, user));

        when(repository.findAllByUserIdAndStatus(
                1L,
                1)).thenReturn(Optional.empty());

        List<CategoriesListDTO> result = service.findAll(1L, null, null);

        assertTrue(result.isEmpty());

        verify(repository).findAllByUserIdAndStatus(
                1L,
                1);
    }
}