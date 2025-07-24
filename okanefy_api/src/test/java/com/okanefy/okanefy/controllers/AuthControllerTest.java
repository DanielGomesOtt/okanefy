package com.okanefy.okanefy.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.okanefy.okanefy.dto.Auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.Auth.LoginUserDTO;
import com.okanefy.okanefy.dto.Auth.RegisterUserDTO;
import com.okanefy.okanefy.infra.security.SecurityConfiguration;
import com.okanefy.okanefy.infra.security.TokenService;
import com.okanefy.okanefy.repositories.UsersRepository;
import com.okanefy.okanefy.services.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsersRepository repository;

    @Test
    @DisplayName("Should create a user successfully")
    void shouldCreateUserSuccessfully() throws Exception {
        RegisterUserDTO user = new RegisterUserDTO("user", "user@email.com", "12345678");
        String requestBody = objectMapper.writeValueAsString(user);
        CreatedUserDTO createdUser = new CreatedUserDTO(1L, "user", "user@email.com", "12345678");

        when(service.save(user)).thenReturn(createdUser);

        mockMvc.perform(post("/signUp").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUser.id()))
                .andExpect(jsonPath("$.name").value(createdUser.name()))
                .andExpect(jsonPath("$.email").value(createdUser.email()))
                .andExpect(jsonPath("$.token").value(createdUser.token()));
    }
}