package com.salon.userservice.service;

import com.salon.userservice.payload.dto.SignupDTO;
import com.salon.userservice.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String username, String password) throws Exception;
    AuthResponse signup(SignupDTO req) throws Exception;
    AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception;

}
