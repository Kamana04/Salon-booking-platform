package com.salon.userservice.service;

import com.salon.userservice.model.User;
import com.salon.userservice.payload.dto.SignupDTO;
import com.salon.userservice.payload.response.TokenResponse;
import com.salon.userservice.repository.UserRepository;
import com.salon.userservice.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final KeyCloakService keyCloakService;

    @Override
    public AuthResponse login(String username, String password) throws Exception {
        TokenResponse tokenResponse = keyCloakService.getAdminAccessToken(
                username,
                password,
                "password",
                null
        );
        AuthResponse response = new AuthResponse();
        response.setTitle("Welcome Back " + username);
        response.setMessage("login success");
        response.setJwt(tokenResponse.getAccessToken());
        response.setRefresh_token(tokenResponse.getRefreshToken());
        return response;
    }

    @Override
    public AuthResponse signup(SignupDTO req) throws Exception {
        keyCloakService.createUser(req);
        //if user is exists with same username then this keyCloak service will throw exception
        //if everything works fine then we need to create user in db

        User createdUser = new User();
        createdUser.setEmail(req.getEmail());
        createdUser.setPassword(req.getPassword());
        createdUser.setCreatedAt(LocalDateTime.now());
        createdUser.setRole(req.getRole());
        createdUser.setFullName(req.getFullName());
        createdUser.setUsername(req.getUsername());
        userRepository.save(createdUser);


        TokenResponse tokenResponse= keyCloakService.getAdminAccessToken(
                req.getUsername(),
                req.getPassword(),
                "password",
                null
        );

        AuthResponse response = new AuthResponse();
        response.setTitle("Welcome " + createdUser.getEmail());
        response.setMessage("Register success");
        response.setJwt(tokenResponse.getAccessToken());
        response.setRole(createdUser.getRole());
        response.setRefresh_token(tokenResponse.getRefreshToken());
        return response;

    }

    @Override
    public AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception {
        TokenResponse tokenResponse = keyCloakService.getAdminAccessToken(
                null,
                null,
                "refresh_token",
                refreshToken
        );
        AuthResponse response = new AuthResponse();

        response.setMessage("Access token received");
        response.setJwt(tokenResponse.getAccessToken());
        response.setRefresh_token(tokenResponse.getRefreshToken());
        return response;
    }
}
