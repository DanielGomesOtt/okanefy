package com.okanefy.okanefy.services;


import com.okanefy.okanefy.dto.paymentMethod.CreatePaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodListPaginationDTO;
import com.okanefy.okanefy.exceptions.PaymentMethodNotFoundException;
import com.okanefy.okanefy.exceptions.UserNotFoundException;
import com.okanefy.okanefy.models.PaymentMethod;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.PaymentMethodRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository repository;

    @Autowired
    private UsersRepository usersRepository;

    public PaymentMethodListPaginationDTO findAll(Long userId, String name, String isInstallment, int page, int size) {
        Page<PaymentMethod> paymentMethods;
        Pageable pageable = PageRequest.of(page, size);
        if(name != null && !Objects.equals(isInstallment, "all")) {
            paymentMethods = repository.findAllByNameContainingIgnoreCaseAndIsInstallmentAndUserIdAndStatus(name, Boolean.parseBoolean(isInstallment), userId, 1, pageable);
        } else if(name != null) {
            paymentMethods = repository.findAllByNameContainingIgnoreCaseAndUserIdAndStatus(name, userId, 1, pageable);
        } else if(!Objects.equals(isInstallment, "all")) {
            paymentMethods = repository.findAllByIsInstallmentAndUserIdAndStatus(Boolean.parseBoolean(isInstallment), userId, 1, pageable);
        } else {
            paymentMethods = repository.findAllByUserIdAndStatus(userId, 1, pageable);
        }
        List<PaymentMethodDTO> formattedPaymentMethods = paymentMethods.map(PaymentMethodDTO::new).getContent();
        return new PaymentMethodListPaginationDTO(formattedPaymentMethods, paymentMethods.getTotalElements(),
                paymentMethods.getTotalPages(), paymentMethods.getNumber(), paymentMethods.getNumberOfElements(),
                paymentMethods.getSize(), paymentMethods.isFirst(), paymentMethods.isLast(), paymentMethods.hasNext(),
                paymentMethods.nextOrLastPageable());
    }

    public PaymentMethodDTO findById(Long id) {
        Optional<PaymentMethod> paymentMethod = repository.findByIdAndStatus(id, 1);
        return paymentMethod.map(PaymentMethodDTO::new).orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        Optional<PaymentMethod> paymentMethod = repository.findById(id);
        paymentMethod.ifPresent(method -> method.setStatus(0));
    }

    @Transactional
    public PaymentMethodDTO save(CreatePaymentMethodDTO data) {
        Optional<Users> user = usersRepository.findById(data.userId());
        if(user.isPresent()) {

            PaymentMethod paymentMethod = new PaymentMethod(data, user.get());
            PaymentMethod createdPaymentMethod = repository.save(paymentMethod);
            return new PaymentMethodDTO(createdPaymentMethod);
        }

        throw new UserNotFoundException("Id informado não pertence a nenhum usuário");
    }

    @Transactional
    public PaymentMethodDTO update(PaymentMethodDTO data) {
        if (Objects.equals(data.isInstallment(), "all")) {
            throw new RuntimeException("Informe se a forma de pagamento é a prazo ou à vista.");
        }
        Optional<PaymentMethod> paymentMethod = repository.findById(data.id());

        if(paymentMethod.isPresent()) {
            paymentMethod.get().setName(data.name());
            paymentMethod.get().setInstallment(Boolean.parseBoolean(data.isInstallment()));
            return new PaymentMethodDTO(paymentMethod.get());
        }

        throw new PaymentMethodNotFoundException("Forma de pagamento não encontrada");
    }
}
