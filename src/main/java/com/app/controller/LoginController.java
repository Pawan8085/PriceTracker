package com.app.controller;

import com.app.dto.response.UserResponse;
import com.app.model.User;
import com.app.repository.UserRepo;
import com.app.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    private UserRepo userRepo;

    @Autowired
    public LoginController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/signIn")
    public ResponseEntity<UserResponse> userSignInHandler(Authentication auth){

        Optional<User> optUser = userRepo.findByEmail(auth.getName());
        if(optUser.isEmpty()){
            throw new BadCredentialsException("Invalid Username or Password!");
        }

        User user = optUser.get();
        UserResponse userResponse = UserTransformer.userToUserResponse(user);
        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
    }
}
