package com.okanefy.okanefy.repositories;

import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.models.Category;
import com.okanefy.okanefy.models.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findByIdAndStatus(Long id, int status);

    Page<PaymentMethod> findAllByUserIdAndStatus(Long userId, int status, Pageable pageable);

    Page<PaymentMethod> findAllByNameContainingIgnoreCaseAndIsInstallmentAndUserIdAndStatus(String name, Boolean isInstallment, Long userId, int status, Pageable pageable);

    Page<PaymentMethod> findAllByNameContainingIgnoreCaseAndUserIdAndStatus(String name, Long userId, int status, Pageable pageable);

    Page<PaymentMethod> findAllByIsInstallmentAndUserIdAndStatus(Boolean isInstallment, Long userId, int status, Pageable pageable);

    Optional<List<PaymentMethod>> findAllByUserIdAndStatus(Long userId, int status);
}
