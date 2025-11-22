package com.okanefy.okanefy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okanefy.okanefy.dto.category.CategoriesListPaginationDTO;
import com.okanefy.okanefy.dto.paymentMethod.CreatePaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodListPaginationDTO;
import com.okanefy.okanefy.models.PaymentMethod;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.PaymentMethodRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import com.okanefy.okanefy.services.PaymentMethodService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PaymentMethodController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentMethodControllerTest {

    @MockitoBean
    private PaymentMethodService service;

    @MockitoBean
    private UsersRepository usersRepository;

    @MockitoBean
    private PaymentMethodRepository repository;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return a list of payment methods")
    void shouldReturnListPaymentMethod() throws Exception {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user, List.of());
        List<PaymentMethodDTO> paymentMethodsList = List.of(new PaymentMethodDTO(paymentMethod));

        PaymentMethodListPaginationDTO paginationDTO = new PaymentMethodListPaginationDTO(
                paymentMethodsList,
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


        when(service.findAll(eq(1L), any(), any(), eq(0), eq(10)))
                .thenReturn(paginationDTO);

        mockMvc.perform(get("/paymentMethod")
                        .param("userId", "1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethods[0].id").value(paymentMethodsList.getFirst().id()))
                .andExpect(jsonPath("$.paymentMethods[0].name").value(paymentMethodsList.getFirst().name()))
                .andExpect(jsonPath("$.paymentMethods[0].isInstallment").value(paymentMethodsList.getFirst().isInstallment()));
    }


    @Test
    @DisplayName("Should return an empty list")
    void shouldReturnEmptyList() throws Exception {
        PaymentMethodListPaginationDTO paginationDTO = new PaymentMethodListPaginationDTO(
                List.of(),
                1L,
                1,
                0,
                0,
                10,
                true,
                true,
                false,
                PageRequest.of(0, 10));


        when(service.findAll(eq(1L), any(), any(), eq(0), eq(10)))
                .thenReturn(paginationDTO);

        mockMvc.perform(get("/paymentMethod")
                        .param("userId", "1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethods", hasSize(0)));
    }


    @Test
    @DisplayName("Should return a payment method")
    void shouldReturnPaymentMethod() throws Exception {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user, List.of());
        PaymentMethodDTO paymentMethodFormatted = new PaymentMethodDTO(paymentMethod);

        when(service.findById(1L)).thenReturn(paymentMethodFormatted);

        mockMvc.perform(get("/paymentMethod/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentMethodFormatted.id()))
                .andExpect(jsonPath("$.name").value(paymentMethodFormatted.name()))
                .andExpect(jsonPath("$.isInstallment").value(paymentMethodFormatted.isInstallment()));
    }

    @Test
    @DisplayName("Should return nothing")
    void shouldReturnNothing() throws Exception {

        when(service.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/paymentMethod/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Should delete a payment method")
    void shouldDeletePaymentMethod() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/paymentMethod/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should create a payment method")
    void shouldCreatePaymentMethod() throws Exception {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user, List.of());
        PaymentMethodDTO createdPaymentMethod = new PaymentMethodDTO(paymentMethod);
        CreatePaymentMethodDTO data = new CreatePaymentMethodDTO("method1", "false", 1L);
        String requestBody = objectMapper.writeValueAsString(data);

        when(service.save(data)).thenReturn(createdPaymentMethod);

        mockMvc.perform(post("/paymentMethod")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdPaymentMethod.id()))
                .andExpect(jsonPath("$.name").value(createdPaymentMethod.name()))
                .andExpect(jsonPath("$.isInstallment").value(createdPaymentMethod.isInstallment()));
    }

    @Test
    @DisplayName("Should update a payment method")
    void shouldUpdatePaymentMethod() throws Exception {
        PaymentMethodDTO data = new PaymentMethodDTO(1L, "method updated", "false");
        String requestBody = objectMapper.writeValueAsString(data);

        when(service.update(data)).thenReturn(data);

        mockMvc.perform(put("/paymentMethod")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(data.id()))
                .andExpect(jsonPath("$.name").value(data.name()))
                .andExpect(jsonPath("$.isInstallment").value(data.isInstallment()));
    }
}