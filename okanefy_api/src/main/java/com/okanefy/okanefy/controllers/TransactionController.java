package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.transactions.*;
import com.okanefy.okanefy.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public ResponseEntity<CreatedTransactionDTO>save (@RequestBody @Valid CreateTransactionDTO data) {
        return ResponseEntity.status(201).body(service.save(data));
    }


    @GetMapping
    public ResponseEntity<FindPageableTransactionsDTO> findAll(@RequestParam(required = true) Long userId,
                                                        @RequestParam(required = true) int page,
                                                        @RequestParam(required = true) int size,
                                                        @RequestParam(required = false) String initialDate,
                                                        @RequestParam(required = false) String endDate,
                                                        @RequestParam(required = false) String description,
                                                        @RequestParam(required = false) String frequency,
                                                        @RequestParam(required = false) Long categoryId,
                                                        @RequestParam(required = false) Long paymentMethodId) {
        return ResponseEntity.ok(service.findAll(userId, page, size, initialDate, endDate, description,
                frequency, categoryId, paymentMethodId));
    }


    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> findById(@PathVariable(required = true) Long transactionId) {
        return ResponseEntity.ok(service.findById(transactionId));
    }


    @DeleteMapping("/{transactionId}")
    public ResponseEntity<?> delete(@PathVariable(required = true) Long transactionId) {
        service.delete(transactionId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping
    public ResponseEntity<TransactionDTO> update(@RequestBody @Valid UpdateTransactionDTO data) {
        return ResponseEntity.ok(service.update(data));
    }
}
