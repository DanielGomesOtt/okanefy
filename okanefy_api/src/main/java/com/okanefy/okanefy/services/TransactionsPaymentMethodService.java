package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.transactions_payment_method.CreateTransactionPaymentMethodDTO;
import com.okanefy.okanefy.dto.transactions_payment_method.FindTransactionPaymentMethodDTO;
import com.okanefy.okanefy.models.PaymentMethod;
import com.okanefy.okanefy.models.Transaction;
import com.okanefy.okanefy.models.TransactionsPaymentMethod;
import com.okanefy.okanefy.repositories.PaymentMethodRepository;
import com.okanefy.okanefy.repositories.TransactionRepository;
import com.okanefy.okanefy.repositories.TransactionsPaymentMethodRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionsPaymentMethodService {

    @Autowired
    private TransactionsPaymentMethodRepository repository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public CreateTransactionPaymentMethodDTO save(CreateTransactionPaymentMethodDTO data) {
        Optional<Transaction> transaction = transactionRepository.findById(data.transaction_id());
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(data.payment_method_id());

        if(transaction.isPresent() && paymentMethod.isPresent()) {
            return new CreateTransactionPaymentMethodDTO(
                    repository.save(new TransactionsPaymentMethod(null,
                            transaction.get(), paymentMethod.get(), 1))
            );

        }

        return null;
    }

    @Transactional
    public void delete(Long id) {
        Optional<TransactionsPaymentMethod> transactionPaymentMethod = repository.findById(id);

        transactionPaymentMethod.ifPresent(object ->
                object.setStatus(0));
    }

    public FindTransactionPaymentMethodDTO findById(Long id) {
        Optional<TransactionsPaymentMethod> transactionPaymentMethod = repository.findById(id);

        return transactionPaymentMethod.map(transactionsPaymentMethod -> new FindTransactionPaymentMethodDTO(transactionsPaymentMethod.getId(),
                transactionsPaymentMethod.getTransaction().getId(),
                transactionsPaymentMethod.getPaymentMethod().getId()
        )).orElse(null);
    }
}
