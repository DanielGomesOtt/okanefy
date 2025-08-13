package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.paymentMethod.CreatePaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodDTO;
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
    @DisplayName("Should return a payment methods list")
    void shouldReturnPaymentMethodList() {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        List<PaymentMethod> paymentMethods = List.of(new PaymentMethod(1L, "method1", false, 1, user));

        when(repository.findAllByUserIdAndStatus(1L, 1)).thenReturn(Optional.of(paymentMethods));

        List<PaymentMethodDTO> result = service.findAll(1L);

        assertEquals(1, result.size());
        assertEquals("method1", result.getFirst().name());
        assertEquals(false, result.getFirst().is_installment());
        assertEquals(1L, result.getFirst().id());

        verify(repository).findAllByUserIdAndStatus(1L, 1);
    }

    @Test
    @DisplayName("Should return an empty list")
    void shouldReturnEmptyList() {

        when(repository.findAllByUserIdAndStatus(1L, 1)).thenReturn(Optional.empty());

        List<PaymentMethodDTO> result = service.findAll(1L);

        assertEquals(0, result.size());

        verify(repository).findAllByUserIdAndStatus(1L, 1);
    }

    @Test
    @DisplayName("Should return a payment method by id")
    void shouldReturnPaymentMethodById() {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user);

        when(repository.findByIdAndStatus(1L, 1)).thenReturn(Optional.of(paymentMethod));

        PaymentMethodDTO result = service.findById(1L);

        assertEquals("method1", result.name());
        assertEquals(false, result.is_installment());
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
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user);

        when(repository.findById(1L)).thenReturn(Optional.of(paymentMethod));

        service.delete(1L);

        assertEquals(0, paymentMethod.getStatus());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should create a payment method")
    void shouldCreatePaymentMethod() {
        CreatePaymentMethodDTO data = new CreatePaymentMethodDTO("method1", false, 1L);
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethodDTO result = service.save(data);

        assertEquals(1L, result.id());
        assertEquals(data.name(), result.name());
        assertEquals(data.is_installment(), result.is_installment());

        verify(usersRepository).findById(1L);
        verify(repository).save(any(PaymentMethod.class));
    }

    @Test
    @DisplayName("Should update a payment method")
    void shouldUpdatePaymentMethod() {
        Users user = new Users(1L, "user", "user@email.com", "123456789", 1);
        PaymentMethod paymentMethod = new PaymentMethod(1L, "method1", false, 1, user);
        PaymentMethodDTO data = new PaymentMethodDTO(1L, "method1 updated", false);

        when(repository.findById(1L)).thenReturn(Optional.of(paymentMethod));

        PaymentMethodDTO result = service.update(data);

        assertEquals(data.id(), result.id());
        assertEquals(data.name(), result.name());
        assertEquals(data.is_installment(), result.is_installment());

        verify(repository).findById(1L);
    }
}