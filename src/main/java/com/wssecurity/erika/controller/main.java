package com.wssecurity.erika.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class main {
    @GetMapping("/")
    public ResponseEntity<?> index() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("message", "salut");
        return new ResponseEntity<>(data, null, 200);
    }
}
