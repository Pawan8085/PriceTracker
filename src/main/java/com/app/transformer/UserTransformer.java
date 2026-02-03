package com.app.transformer;

import com.app.dto.request.UserRequest;
import com.app.dto.response.UserResponse;
import com.app.model.User;

public class UserTransformer {


    public static User userRequestToUser(UserRequest userRequest){
        // UserRequest to User
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

    }


    public static UserResponse userToUserResponse(User user){
        // User to UserResponse
        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
