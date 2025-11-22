package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.paymentMethod.CreatePaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodListPaginationDTO;
import com.okanefy.okanefy.models.PaymentMethod;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.PaymentMethodRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository repository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private PaymentMethodService service;

    @Test
    @DisplayName("Should return payment methods list without filters")
    void shouldReturnPaymentMethodsList() {
        Users user = new Users(1L, "user", "user@email.com", "12345678", 1);

        PaymentMethod method = new PaymentMethod(
                1L,
                "method1",
                false,
                1,
                user,
                List.of()
        );

        Page<PaymentMethod> page = new PageImpl<>(
                List.of(method),
                PageRequest.of(0, 10),
                1
        );

        when(repository.findAllByUserIdAndStatus(
                1L,
                1,
                PageRequest.of(0, 10)
        )).thenReturn(page);

        PaymentMethodListPaginationDTO result = service.findAll(
                1L,
                null,
                "all",
                0,
                10
        );

        assertEquals(1, result.paymentMethods().size());
        assertEquals("method1", result.paymentMethods().getFirst().name());
        assertFalse(Boolean.parseBoolean(result.paymentMethods().getFirst().isInstallment()));
        assertEquals(1L, result.paymentMethods().getFirst().id());

        verify(repository).findAllByUserIdAndStatus(
                1L,
                1,
                PageRequest.of(0, 10)
        );
    }


    @Test
    @DisplayName("Should return an empty list")
    void shouldReturnEmptyList() {

        Page<PaymentMethod> page = new PageImpl<>(
                List.of(),                 // lista vazia
                PageRequest.of(0, 10),
                0                          // totalElements
        );

        when(repository.findAllByUserIdAndStatus(
                1L,
                1,
                PageRequest.of(0, 10)
        )).thenReturn(page);

        PaymentMethodListPaginationDTO result = service.findAll(
                1L,
                null,
                "all",
                0,
                10
        );

        assertNotNull(result.paymentMethods());
        assertTrue(result.paymentMethods().isEmpty());
        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());

        verify(repository).findAllByUserIdAndStatus(
                1L,
                1,
                PageRequest.of(0, 10)
        );
    }


    @Test
    @DisplayName("Should return a payment method by id")
    void shouldReturnPaymentMethodById() {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user, List.of());

        when(repository.findByIdAndStatus(1L, 1)).thenReturn(Optional.of(paymentMethod));

        PaymentMethodDTO result = service.findById(1L);

        assertEquals("method1", result.name());
        assertEquals("false", result.isInstallment());
        assertEquals(1L, result.id());

        verify(repository).findByIdAndStatus(1L, 1);
    }

    @Test
    @DisplayName("Find by id should return null")
    void findByIdShouldReturnNull() {

        when(repository.findByIdAndStatus(1L, 1)).thenReturn(Optional.empty());

        PaymentMethodDTO result = service.findById(1L);

        assertNull(result);

        verify(repository).findByIdAndStatus(1L, 1);
    }

    @Test
    @DisplayName("Should delete a payment method")
    void shouldDeletePaymentMethod() {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user, List.of());

        when(repository.findById(1L)).thenReturn(Optional.of(paymentMethod));

        service.delete(1L);

        assertEquals(0, paymentMethod.getStatus());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should create a payment method")
    void shouldCreatePaymentMethod() {
        CreatePaymentMethodDTO data = new CreatePaymentMethodDTO("method1", "false", 1L);
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user, List.of());

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethodDTO result = service.save(data);

        assertEquals(1L, result.id());
        assertEquals(data.name(), result.name());
        assertEquals(data.isInstallment(), result.isInstallment());

        verify(usersRepository).findById(1L);
        verify(repository).save(any(PaymentMethod.class));
    }

    @Test
    @DisplayName("Should update a payment method")
    void shouldUpdatePaymentMethod() {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user, List.of());
        PaymentMethodDTO data = new PaymentMethodDTO(1L, "method1 updated", "false");

        when(repository.findById(1L)).thenReturn(Optional.of(paymentMethod));

        PaymentMethodDTO result = service.update(data);

        assertEquals(data.id(), result.id());
        assertEquals(data.name(), result.name());
        assertEquals(data.isInstallment(), result.isInstallment());

        verify(repository).findById(1L);
    }
}