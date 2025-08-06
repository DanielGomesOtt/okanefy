package com.okanefy.okanefy.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.okanefy.okanefy.dto.auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.auth.ForgotPasswordDTO;
import com.okanefy.okanefy.dto.auth.RegisterUserDTO;
import com.okanefy.okanefy.dto.auth.UpdatePasswordDTO;
import com.okanefy.okanefy.infra.security.TokenService;
import com.okanefy.okanefy.repositories.UsersRepository;
import com.okanefy.okanefy.services.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
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

        mockMvc.perform(post("/signUp")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUser.id()))
                .andExpect(jsonPath("$.name").value(createdUser.name()))
                .andExpect(jsonPath("$.email").value(createdUser.email()))
                .andExpect(jsonPath("$.token").value(createdUser.token()));
    }

    @Test
    @DisplayName("Should create a recovery code")
    void shouldCreateRecoveryCode() throws Exception {
        ForgotPasswordDTO data = new ForgotPasswordDTO("user@email.com");
        String requestBody = objectMapper.writeValueAsString(data);

        doNothing().when(service).createRecoveryCode(data);

        mockMvc.perform(post("/forgotPassword")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(data.email()));
    }

    @Test
    @DisplayName("Should confirm recovery code.")
    void shouldConfirmRecoveryCode() throws Exception {
        String email = "user@email.com";
        String code = "123456789";

        when(service.confirmRecoveryCode(email, code)).thenReturn(HttpStatusCode.valueOf(200));

        mockMvc.perform(get("/confirmRecoveryCode")
                        .param("email", email)
                        .param("code", code))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(email));
    }

    @Test
    @DisplayName("Should return bad request status in recovery code confirmation flow.")
    void shouldReturnBadRequestStatusRecoveryCode() throws Exception {
        String email = "user@email.com";

        mockMvc.perform(get("/confirmRecoveryCode")
                        .param("email", email))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 status code in recovery code confirmation flow.")
    void shouldReturnNotFoundStatusCodeRecoveryCode() throws Exception {
        String email = "user@email.com";
        String code = "123456789";

        when(service.confirmRecoveryCode(email, code)).thenReturn(HttpStatusCode.valueOf(404));

        mockMvc.perform(get("/confirmRecoveryCode")
                        .param("email", email)
                        .param("code", code))
                .andExpect(status().isNotFound())
                .andExpect(content().string(email));
    }

    @Test
    @DisplayName("Should update password")
    void shouldUpdatePassword() throws Exception {
        String email = "user@email.com";
        String password = "minhasenha123";
        UpdatePasswordDTO data = new UpdatePasswordDTO(email, password);
        String requestBody = objectMapper.writeValueAsString(data);

        doNothing().when(service).updatePassword(data);

        mockMvc.perform(put("/updatePassword")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return bad request status for update password flow")
    void shouldReturnBadRequestUpdatePassword() throws Exception {
        String email = "user@email.com";
        String password = "minhase";
        UpdatePasswordDTO data = new UpdatePasswordDTO(email, password);
        String requestBody = objectMapper.writeValueAsString(data);

        doNothing().when(service).updatePassword(data);

        mockMvc.perform(put("/updatePassword")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message")
                        .value("A senha deve ter no mínimo 8 caracteres."));
    }
}