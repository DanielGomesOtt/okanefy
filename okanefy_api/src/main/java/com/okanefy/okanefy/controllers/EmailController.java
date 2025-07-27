package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.email.EmailDTO;
import com.okanefy.okanefy.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    private EmailService service;

    @PostMapping
    public void sendEmail(@RequestBody EmailDTO email) {
        service.sendEmail(email);
    }
}
