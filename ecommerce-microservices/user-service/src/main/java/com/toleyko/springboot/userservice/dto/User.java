package com.toleyko.springboot.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "Invalid username")
    private String username;
    @Email(message = "Invalid email")
    private String email;
    @Size(min = 4, max = 15, message = "Invalid password, length must be more that 3 and less than 16")
    private String password;
    @NotEmpty(message = "Invalid firstname")
    private String firstName;
    @NotEmpty(message = "Invalid lastname")
    private String lastName;
}
