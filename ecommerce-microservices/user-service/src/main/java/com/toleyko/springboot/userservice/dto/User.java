package com.toleyko.springboot.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
