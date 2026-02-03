package com.app.service;

import com.app.dto.request.UserRequest;
import com.app.dto.response.UserResponse;
import com.app.exception.UserException;
import com.app.model.User;
import com.app.repository.UserRepo;
import com.app.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Value("${app.mail}")
    private String mail;

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        // send registration mail
        String subject = "PriceTracker Registration";
        String text = "You have successfully registered to PriceTracker";
        boolean success = emailService.send(userRequest.getEmail(), mail, subject, text);

        if(true){
            User user = UserTransformer.userRequestToUser(userRequest);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepo.save(user);
            return UserTransformer.userToUserResponse(savedUser);
        }

        throw new UserException("Invalid email id!");

    }

    @Override
    public UserResponse userProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return UserTransformer.userToUserResponse(user);
    }

}
