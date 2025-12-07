package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.transactions.*;
import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.enums.TransactionFrequency;
import com.okanefy.okanefy.models.*;
import com.okanefy.okanefy.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private TransactionRepository repository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private TransactionsPaymentMethodRepository transactionsPaymentMethodRepository;

    @InjectMocks
    private TransactionService service;

    private Users user;

    private Transaction transaction;

    private Category category;

    private PaymentMethod paymentMethod;

    private TransactionsPaymentMethod transactionsPaymentMethod;

    @BeforeEach
    void setUp() {
        user = new Users(1L, "user", "user@email.com", "12345678", 1);
        category = new Category(1L, "category", CategoriesTypes.EXPENSE, 1, user);
        paymentMethod = new PaymentMethod(1L, "method", false, 1, user, List.of());
        transaction = new Transaction(1L, "2025-09-02", "2025-09-02", 20.00,
                "transaction", 1,
                TransactionFrequency.none, 1, user, category, new ArrayList<>());
        transactionsPaymentMethod = new TransactionsPaymentMethod(1L, transaction, paymentMethod, 1);
        transaction.getTransactionPaymentMethods().add(transactionsPaymentMethod);
    }


    @Test
    @DisplayName("Should create a transaction payment method")
    void shouldCreateTransactionPaymentMethod() {
        CreateTransactionDTO data = new CreateTransactionDTO("2025-09-02", 20.00,
                "transaction", 1, "none", 1L, 1L,
                List.of(1L));

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));
        when(repository.save(any(Transaction.class))).thenReturn(transaction);

        CreatedTransactionDTO result = service.save(data);

        assertEquals(data.initial_date(), result.initial_date());
        assertEquals(transaction.getEnd_date(), result.end_date());
        assertEquals(data.amount(), result.amount());
        assertEquals(data.description(), result.description());
        assertEquals(data.number_installments(), result.number_installments());
        assertEquals(data.frequency(), result.frequency());
        assertEquals(data.category_id(), result.category_id());
        assertEquals(data.payment_methods(), result.payment_methods());

        verify(usersRepository).findById(1L);
        verify(categoryRepository).findById(1L);
        verify(paymentMethodRepository).findById(1L);
        verify(repository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should return a list of transactions")
    void shouldReturnListTransactions() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Transaction> transactions = List.of(transaction);
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, transactions.size());

        when(repository.findAllWithFilters(
                eq(1L),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(pageable)
        )).thenReturn(transactionPage);


        FindPageableTransactionsDTO result = service.findAll(
                1L, 0, 5, null, null,
                null, null, null, null
        );

        assertNotNull(result);
        assertNotNull(result.transactions());
        assertEquals(1, result.transactions().size());
        assertEquals("transaction", result.transactions().getFirst().description());
        assertEquals(1, result.totalElements());
        assertEquals(1, result.totalPages());

        verify(repository).findAllWithFilters(
                eq(1L),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(pageable)
        );

    }

    @Test
    @DisplayName("Should return a transaction by id")
    void shouldReturnTransactionById() {
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionDTO result = service.findById(1L);

        assertEquals(1L, result.id());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should return null when transaction not found")
    void shouldReturnNullWhenTransactionNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        TransactionDTO result = service.findById(1L);

        assertNull(result);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should delete a transaction")
    void shouldDeleteTransaction() {
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        service.delete(1L);

        assertEquals(0, transaction.getStatus());
        assertEquals(0, transaction.getTransactionPaymentMethods().getFirst().getStatus());

        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should update a transaction")
    void shouldUpdateTransaction() {
        UpdateTransactionDTO data = new UpdateTransactionDTO(1L, "2025-09-02", "2025-09-02",
                20.00, "updated transaction", 1, "daily", 1L);

        when(repository.findById(1L)).thenReturn(Optional.of(transaction));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        TransactionDTO result = service.update(data);

        assertEquals("updated transaction", result.description());
        assertEquals("daily", result.frequency());

        verify(repository).findById(1L);
        verify(categoryRepository).findById(1L);
    }
}