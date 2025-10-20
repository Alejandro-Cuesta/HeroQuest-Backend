package dev.alejandro.heroquest_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDTORequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String username;

    @NotBlank(message = "Display name cannot be blank")
    @Size(min = 3, max = 20, message = "Display name must be between 3 and 20 characters")
    private String displayName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must have at least 6 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number")
    private String password;

    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;
}