package com.okanefy.okanefy.controllers;


import com.okanefy.okanefy.dto.transactions_payment_method.CreateTransactionPaymentMethodDTO;
import com.okanefy.okanefy.dto.transactions_payment_method.FindTransactionPaymentMethodDTO;
import com.okanefy.okanefy.services.TransactionsPaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactionsPaymentMethod")
public class TransactionsPaymentMethodController {

    @Autowired
    private TransactionsPaymentMethodService service;

    @PostMapping
    public ResponseEntity<CreateTransactionPaymentMethodDTO> save(@RequestBody @Valid CreateTransactionPaymentMethodDTO data) {
        return ResponseEntity.status(201).body(service.save(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(required = true) Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindTransactionPaymentMethodDTO> findById(@PathVariable(required = true) Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
