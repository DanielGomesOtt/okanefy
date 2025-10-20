package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.users.UsersDTO;
import com.okanefy.okanefy.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService service;

    @PutMapping
    public ResponseEntity<UsersDTO> updateUser(@RequestBody @Valid UsersDTO data) {
        return ResponseEntity.ok(service.updateUser(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
