package com.okanefy.okanefy.repositories;

import com.okanefy.okanefy.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<List<PaymentMethod>> findAllByUserIdAndStatus(Long userId, int status);

    Optional<PaymentMethod> findByIdAndStatus(Long id, int status);
}
