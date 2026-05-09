package com.salon.userservice.payload.dto;

import com.salon.userservice.domain.UserRole;
import lombok.Data;

@Data
public class SignupDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private UserRole role;
}
