package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.paymentMethod.CreatePaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodDTO;
import com.okanefy.okanefy.services.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paymentMethod")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService service;

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> findAll(@RequestParam Long userId) {
        List<PaymentMethodDTO> paymentMethods = service.findAll(userId);
        return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> findById(@PathVariable Long id) {
        PaymentMethodDTO paymentMethod = service.findById(id);
        return ResponseEntity.ok(paymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> save(@RequestBody @Valid CreatePaymentMethodDTO data) {
        PaymentMethodDTO createdPaymentMethod = service.save(data);
        return ResponseEntity.status(201).body(createdPaymentMethod);
    }

    @PutMapping
    public ResponseEntity<PaymentMethodDTO> update(@RequestBody PaymentMethodDTO data) {
        PaymentMethodDTO updatedPaymentMethod = service.update(data);
        return ResponseEntity.ok(updatedPaymentMethod);
    }
}
