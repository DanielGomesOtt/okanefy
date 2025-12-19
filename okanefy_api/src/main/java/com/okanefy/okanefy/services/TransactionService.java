package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.transactions.*;
import com.okanefy.okanefy.dto.transactions_payment_method.TransactionPaymentMethodDTO;
import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.enums.TransactionFrequency;
import com.okanefy.okanefy.models.*;
import com.okanefy.okanefy.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private TransactionsPaymentMethodRepository transactionsPaymentMethodRepository;

    @Transactional
    public CreatedTransactionDTO save(CreateTransactionDTO data) {
        Users user = usersRepository.findById(data.user_id()).get();
        Category category = categoryRepository.findById(data.category_id()).get();
        Transaction transaction = new Transaction(null, data.initial_date(), "", data.amount(),
                data.description(), data.number_installments(), TransactionFrequency.valueOf(data.frequency()),
                1, user, category, new ArrayList<>());

        if(data.frequency().equals("none") || data.number_installments() < 2) {
            transaction.setEnd_date(data.initial_date());
        } else if(data.frequency().equals("daily")) {
            transaction.setEnd_date(LocalDate.parse(data.initial_date()).plusDays(data.number_installments()).toString());
        } else if(data.frequency().equals("monthly")) {
            transaction.setEnd_date(LocalDate.parse(data.initial_date()).plusMonths(data.number_installments()).toString());
        } else if(data.frequency().equals("yearly")) {
            transaction.setEnd_date(LocalDate.parse(data.initial_date()).plusYears(data.number_installments()).toString());
        }

        data.payment_methods().forEach(id -> {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(id).get();
            TransactionsPaymentMethod transactionPaymentMethod = new TransactionsPaymentMethod();
            transactionPaymentMethod.setTransaction(transaction);
            transactionPaymentMethod.setPaymentMethod(paymentMethod);
            transactionPaymentMethod.setStatus(1);
            transaction.getTransactionPaymentMethods().add(transactionPaymentMethod);
        });

        Transaction createdTransaction = repository.save(transaction);

        return new CreatedTransactionDTO(createdTransaction.getId(), createdTransaction.getInitial_date(),
                createdTransaction.getEnd_date(), createdTransaction.getAmount(), createdTransaction.getDescription(),
                createdTransaction.getNumber_installments(), createdTransaction.getFrequency().toString(),
                createdTransaction.getCategory().getId(), data.payment_methods());

    }

    public FindPageableTransactionsDTO findAll(Long userId, int page, int size, String initialDate, String endDate,
                                               String description, String frequency, Long categoryId, Long paymentMethodId, String categoryType) {
        Pageable pageable = PageRequest.of(page, size);
        TransactionFrequency freqEnum = null;
        CategoriesTypes categoryTypeEnum = null;

        if (frequency != null && !frequency.isBlank()) {
            freqEnum = TransactionFrequency.valueOf(frequency);
        }

        if(categoryType != null && !categoryType.isBlank()) {
            categoryTypeEnum = CategoriesTypes.valueOf(categoryType);
        }

        Page<Transaction> transactions = repository.findAllWithFilters(
                userId,
                initialDate,
                endDate,
                description,
                freqEnum,
                categoryId,
                categoryTypeEnum,
                paymentMethodId,
                pageable
        );
        List<TransactionDTO> transactionDTOList = transactions.stream()
                .map(transaction -> new TransactionDTO(
                        transaction.getId(),
                        transaction.getInitial_date(),
                        transaction.getEnd_date(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getNumber_installments(),
                        transaction.getFrequency().name(),
                        transaction.getCategory().getId(),
                        transaction.getCategory().getType().toString(),
                        transaction.getTransactionPaymentMethods().stream()
                                .map(tpm -> new TransactionPaymentMethodDTO(
                                        tpm.getId(),
                                        tpm.getPaymentMethod().getId()
                                ))
                                .toList()
                ))
                .toList();

        return new FindPageableTransactionsDTO(transactionDTOList, transactions.getNumber(), transactions.isFirst(),
                transactions.isLast(), transactions.getNumberOfElements(), transactions.getSize(),
                transactions.getTotalElements(), transactions.getTotalPages());
    }

    public TransactionDTO findById(Long transactionId) {
        Optional<Transaction> transaction = repository.findById(transactionId);

        if(transaction.isPresent()) {
            List<TransactionPaymentMethodDTO> transactionsPaymentMethod = transaction.get()
                    .getTransactionPaymentMethods().stream()
                    .map(transactionPaymentMethod ->
                        new TransactionPaymentMethodDTO(transactionPaymentMethod.getId(),
                                transactionPaymentMethod.getPaymentMethod().getId())
                    ).toList();

            return new TransactionDTO(transaction.get().getId(), transaction.get().getInitial_date(),
                    transaction.get().getEnd_date(), transaction.get().getAmount(), transaction.get().getDescription(),
                    transaction.get().getNumber_installments(), transaction.get().getFrequency().name(),
                    transaction.get().getCategory().getId(), transaction.get().getCategory().getType().toString(),
                    transactionsPaymentMethod);
        }

        return null;
    }

    @Transactional
    public void delete(Long transactionId) {
        Optional<Transaction> transaction = repository.findById(transactionId);

        if(transaction.isPresent()) {
            transaction.get().setStatus(0);
            transaction.get().getTransactionPaymentMethods()
                    .forEach(transactionPaymentMethod -> transactionPaymentMethod.setStatus(0));
        }
    }

    @Transactional
    public TransactionDTO update(UpdateTransactionDTO data) {
        Optional<Transaction> transaction = repository.findById(data.id());
        Optional<Category> category = categoryRepository.findById(data.category_id());

        if(transaction.isPresent()) {
            transaction.get().setInitial_date(data.initial_date());
            transaction.get().setFrequency(TransactionFrequency.valueOf(data.frequency()));
            transaction.get().setAmount(data.amount());
            transaction.get().setDescription(data.description());
            transaction.get().setNumber_installments(data.number_installments());

            if(data.frequency().equals("none") || data.number_installments() < 2) {
                transaction.get().setEnd_date(data.initial_date());
            } else if(data.frequency().equals("daily")) {
                transaction.get().setEnd_date(LocalDate.parse(data.initial_date()).plusDays(data.number_installments()).toString());
            } else if(data.frequency().equals("monthly")) {
                transaction.get().setEnd_date(LocalDate.parse(data.initial_date()).plusMonths(data.number_installments()).toString());
            } else if(data.frequency().equals("yearly")) {
                transaction.get().setEnd_date(LocalDate.parse(data.initial_date()).plusYears(data.number_installments()).toString());
            }

            category.ifPresent(value -> transaction.get().setCategory(value));

            List<TransactionPaymentMethodDTO> transactionsPaymentMethod = transaction.get()
                    .getTransactionPaymentMethods().stream()
                    .map(transactionPaymentMethod ->
                            new TransactionPaymentMethodDTO(transactionPaymentMethod.getId(),
                                    transactionPaymentMethod.getPaymentMethod().getId())
                    ).toList();

            return new TransactionDTO(transaction.get().getId(), transaction.get().getInitial_date(),
                    transaction.get().getEnd_date(), transaction.get().getAmount(), transaction.get().getDescription(),
                    transaction.get().getNumber_installments(), transaction.get().getFrequency().name(),
                    transaction.get().getCategory().getId(), transaction.get().getCategory().getType().toString(),
                    transactionsPaymentMethod);
        }

        return null;
    }
}
