package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.paymentMethod.CreatePaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodDTO;
import com.okanefy.okanefy.dto.paymentMethod.PaymentMethodListPaginationDTO;
import com.okanefy.okanefy.services.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentMethod")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService service;

    @GetMapping
    public ResponseEntity<PaymentMethodListPaginationDTO> findAll(@RequestParam(required = true) Long userId,
                                                                  @RequestParam(required = false) String name,
                                                                  @RequestParam(required = false) String isInstallment,
                                                                  @RequestParam(required = true) int page,
                                                                  @RequestParam(required = true) int size) {

        return ResponseEntity.ok(service.findAll(userId, name, isInstallment, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> save(@RequestBody @Valid CreatePaymentMethodDTO data) {
        return ResponseEntity.status(201).body(service.save(data));
    }

    @PutMapping
    public ResponseEntity<PaymentMethodDTO> update(@RequestBody PaymentMethodDTO data) {
        return ResponseEntity.ok(service.update(data));
    }
}
