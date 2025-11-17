package com.okanefy.okanefy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okanefy.okanefy.dto.category.*;
import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.models.Category;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.CategoryRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import com.okanefy.okanefy.services.CategoryService;
import com.okanefy.okanefy.services.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        Category category = new Category(1L, "category", CategoriesTypes.EXPENSE, 1, user);
        CreatedCategoryDTO createdCategory = new CreatedCategoryDTO(category);

        when(service.save(data)).thenReturn(createdCategory);

        mockMvc.perform(post("/category")
                        .content(objectMapper.writeValueAsString(data))
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

        verify(service).delete(1L);
    }

    @Test
    @DisplayName("Should update a category")
    void shouldUpdateCategory() throws Exception {
        UpdateCategoryDTO data = new UpdateCategoryDTO(1L, "category", "EXPENSE");
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        Category category = new Category(1L, "category", CategoriesTypes.EXPENSE, 1, user);
        CreatedCategoryDTO updatedCategory = new CreatedCategoryDTO(category);

        when(service.update(data)).thenReturn(updatedCategory);

        mockMvc.perform(put("/category")
                        .content(objectMapper.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCategory.id()))
                .andExpect(jsonPath("$.name").value(updatedCategory.name()))
                .andExpect(jsonPath("$.type").value(updatedCategory.type().toString()));
    }

    @Test
    @DisplayName("Should return a list of categories with pagination info")
    void shouldReturnListCategory() throws Exception {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);
        Category category = new Category(1L, "category", CategoriesTypes.EXPENSE, 1, user);
        List<CategoriesListDTO> categoryList = List.of(new CategoriesListDTO(category));

        CategoriesListPaginationDTO paginationDTO = new CategoriesListPaginationDTO(
                categoryList,
                1L,
                1,
                0,
                1,
                10,
                true,
                true,
                false,
                PageRequest.of(0, 10)
        );

        when(service.findAll(1L, "category", "EXPENSE", 0, 10)).thenReturn(paginationDTO);

        mockMvc.perform(get("/category/{userId}", 1L)
                        .param("name", "category")
                        .param("type", "EXPENSE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(1)))
                .andExpect(jsonPath("$.categories[0].id").value(categoryList.getFirst().id()))
                .andExpect(jsonPath("$.categories[0].name").value(categoryList.getFirst().name()))
                .andExpect(jsonPath("$.categories[0].type").value(categoryList.getFirst().type()))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andExpect(jsonPath("$.isLast").value(true))
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    @DisplayName("Should return an empty list with pagination info")
    void shouldReturnEmptyList() throws Exception {
        CategoriesListPaginationDTO paginationDTO = new CategoriesListPaginationDTO(
                List.of(),
                0L,
                0,
                0,
                0,
                10,
                true,
                true,
                true,
                PageRequest.of(0, 10)
        );

        when(service.findAll(1L, "category", "EXPENSE", 0, 10)).thenReturn(paginationDTO);

        mockMvc.perform(get("/category/{userId}", 1L)
                        .param("name", "category")
                        .param("type", "EXPENSE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }
}
