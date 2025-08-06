package com.okanefy.okanefy.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okanefy.okanefy.dto.category.CategoriesListDTO;
import com.okanefy.okanefy.dto.category.CreatedCategoryDTO;
import com.okanefy.okanefy.dto.category.NewCategoryDTO;
import com.okanefy.okanefy.dto.category.UpdateCategoryDTO;
import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.infra.security.TokenService;
import com.okanefy.okanefy.models.Category;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.CategoryRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import com.okanefy.okanefy.services.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @MockitoBean
    private CategoryService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsersRepository usersRepository;

    @MockitoBean
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create a category")
    void shouldCreateCategory() throws Exception {
        NewCategoryDTO data = new NewCategoryDTO("category", "EXPENSE", 1L);
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        Category category = new Category(1L, "category", CategoriesTypes.valueOf("EXPENSE"), 1, user);
        CreatedCategoryDTO createdCategory = new CreatedCategoryDTO(category);
        String requestBody = objectMapper.writeValueAsString(data);

        when(service.save(data)).thenReturn(createdCategory);

        mockMvc.perform(post("/category")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdCategory.id()))
                .andExpect(jsonPath("$.name").value(createdCategory.name()))
                .andExpect(jsonPath("$.type").value(createdCategory.type().toString()));
    }

    @Test
    @DisplayName("Should delete a category")
    void shouldDeleteCategory() throws Exception {

        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/category/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should update a category")
    void shouldUpdateCategory() throws Exception {
        UpdateCategoryDTO data = new UpdateCategoryDTO(1L, "category", "EXPENSE");
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        Category category = new Category(1L, "category", CategoriesTypes.valueOf("EXPENSE"), 1, user);
        CreatedCategoryDTO updatedCategory = new CreatedCategoryDTO(category);
        String requestBody = objectMapper.writeValueAsString(data);

        when(service.update(data)).thenReturn(updatedCategory);

        mockMvc.perform(put("/category")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCategory.id()))
                .andExpect(jsonPath("$.name").value(updatedCategory.name()))
                .andExpect(jsonPath("$.type").value(updatedCategory.type().toString()));
    }

    @Test
    @DisplayName("Should return a list of categories")
    void shouldReturnListCategory() throws Exception {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        Category category = new Category(1L, "category", CategoriesTypes.valueOf("EXPENSE"), 1, user);
        List<CategoriesListDTO> categories = List.of(new CategoriesListDTO(category));

        when(service.findAll(1L, "category", "EXPENSE")).thenReturn(categories);

        mockMvc.perform(get("/category/{userId}", 1L)
                        .param("name", "category")
                        .param("type", "EXPENSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(categories.getFirst().id()))
                .andExpect(jsonPath("$[0].name").value(categories.getFirst().name()))
                .andExpect(jsonPath("$[0].type").value(categories.getFirst().type()));
    }

    @Test
    @DisplayName("Should return an empty list")
    void shouldReturnEmptyList() throws Exception {

        when(service.findAll(1L, "category", "EXPENSE")).thenReturn(List.of());

        mockMvc.perform(get("/category/{userId}", 1L)
                        .param("name", "category")
                        .param("type", "EXPENSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}