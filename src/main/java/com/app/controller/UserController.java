package com.app.controller;

import com.app.dto.request.UserRequest;
import com.app.dto.response.UserResponse;
import com.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUserHandler(@RequestBody @Valid UserRequest userRequest){
        UserResponse userResponse = userService.registerUser(userRequest);
        System.out.print("inside user register controller");
        return new ResponseEntity<UserResponse>(UserResponse.builder().build(), HttpStatus.CREATED);
    }


    @GetMapping("/profile")
    public ResponseEntity<UserResponse> profile(Authentication authentication){

        UserResponse userResponse =  userService.userProfile(authentication);
        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
    }

}
