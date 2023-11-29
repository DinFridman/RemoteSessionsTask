package com.example.remotesessionstask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return new ResponseEntity<>("Good!", HttpStatus.OK);
    }
}
