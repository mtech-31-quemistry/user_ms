package com.quemistry.user_ms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.status(HttpStatus.OK).body("Greeting from Hello World!");
    }
}
