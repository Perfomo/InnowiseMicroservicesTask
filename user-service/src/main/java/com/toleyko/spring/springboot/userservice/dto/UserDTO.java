package com.toleyko.spring.springboot.userservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userName;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
