package com.toleyko.spring.springboot.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
