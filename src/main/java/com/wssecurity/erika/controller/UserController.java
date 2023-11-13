package com.wssecurity.erika.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wssecurity.erika.dto.UserDTO;
import com.wssecurity.erika.jwt.JwtUtil;
import com.wssecurity.erika.service.UserService;
import com.wssecurity.erika.utility.ApiResponse;

@RestController
public class UserController {

    @Autowired
    JwtUtil jwtUtilService;
    @Autowired
    UserService userService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody UserDTO user) {
        //Hashmap ~ Tab. associatif<key,value>
        HashMap<String, Object> data = new HashMap<>();
        try {

            userService.register(user);
            String token = jwtUtilService.generateToken(user);
            data.put("user", user);
            data.put("token", token);
            return new ResponseEntity<>(new ApiResponse<>(data), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody UserDTO user) {
        HashMap<String, Object> data = new HashMap<>();
        try {
            userService.login(user);
            String token = jwtUtilService.generateToken(user);
            data.put("user", user);
            data.put("token", token);
            return new ResponseEntity<>(new ApiResponse<>(data), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/forAdminOnly")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> onlyAdminData() {
        HashMap<String, Object> data = new HashMap<>();
        try {
            data.put("message", "This is only for admin");
            return new ResponseEntity<>(new ApiResponse<>(data), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/forAdminAndUser")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> userAndAdminData() {
        HashMap<String, Object> data = new HashMap<>();
        try {
            data.put("message", "This is only for admin and user");
            return new ResponseEntity<>(new ApiResponse<>(data), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(e.getMessage()), HttpStatus.BAD_REQUEST); 
        }
    }

    @GetMapping("/forEveryone")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> worldData() {
        HashMap<String, Object> data = new HashMap<>();
        try {
            data.put("message", "Hello World");
            return new ResponseEntity<>(new ApiResponse<>(data), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
