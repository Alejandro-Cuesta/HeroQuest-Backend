package dev.alejandro.heroquest_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDTORequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
