package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.paymentMethod.CreatePaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodDTO;
import com.okanefy.okanefy.exceptions.PaymentMethodNotFoundException;
import com.okanefy.okanefy.exceptions.UserNotFoundException;
import com.okanefy.okanefy.models.PaymentMethod;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.PaymentMethodRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository repository;

    @Autowired
    private UsersRepository usersRepository;

    public List<PaymentMethodDTO> findAll(Long userId) {
        Optional<List<PaymentMethod>> paymentMethods = repository.findAllByUserIdAndStatus(userId, 1);
        return paymentMethods.map(paymentMethodsList -> paymentMethodsList
                .stream()
                .map(PaymentMethodDTO::new).toList()).orElseGet(List::of);
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
        Optional<PaymentMethod> paymentMethod = repository.findById(data.id());

        if(paymentMethod.isPresent()) {
            paymentMethod.get().setName(data.name());
            paymentMethod.get().set_installment(data.is_installment());
            return new PaymentMethodDTO(paymentMethod.get());
        }

        throw new PaymentMethodNotFoundException("Forma de pagamento não encontrada");
    }
}
