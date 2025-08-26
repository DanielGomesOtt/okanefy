package com.okanefy.okanefy.repositories;

import com.okanefy.okanefy.models.TransactionsPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsPaymentMethodRepository extends JpaRepository<TransactionsPaymentMethod, Long> {
}
