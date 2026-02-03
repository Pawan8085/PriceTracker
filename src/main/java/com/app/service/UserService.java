package com.app.service;

import com.app.dto.request.UserRequest;
import com.app.dto.response.UserResponse;
import com.app.exception.UserException;
import org.springframework.security.core.Authentication;

public interface UserService {

    UserResponse registerUser(UserRequest userRequest)throws UserException;

    UserResponse userProfile(Authentication authentication);

}
