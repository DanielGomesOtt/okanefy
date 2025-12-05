package com.okanefy.okanefy.repositories;

import com.okanefy.okanefy.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
       SELECT DISTINCT t FROM Transaction t
       JOIN FETCH t.transactionPaymentMethods tpm
       WHERE t.user.id = :userId
       AND t.status = 1
       AND tpm.status = 1
       AND (:initialDate IS NULL OR t.initial_date >= :initialDate)
       AND (:endDate IS NULL OR t.end_date <= :endDate)
       AND (:description IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%')))
       AND (:frequency IS NULL OR t.frequency = :frequency)
       AND (:category_id IS NULL OR t.category.id = :category_id)
       AND (:payment_method_id IS NULL OR tpm.paymentMethod.id = :payment_method_id)
       """)
    Page<Transaction> findAllWithFilters(
            @Param("userId") Long userId,
            @Param("initialDate") String initialDate,
            @Param("endDate") String endDate,
            @Param("description") String description,
            @Param("frequency") String frequency,
            @Param("category_id") Long categoryId,
            @Param("payment_method_id") Long paymentMethodId,
            Pageable pageable);

}
