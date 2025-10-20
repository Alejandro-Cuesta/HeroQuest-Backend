package dev.alejandro.heroquest_backend.auth.service;

import dev.alejandro.heroquest_backend.auth.dto.UserDTOResponse;
import dev.alejandro.heroquest_backend.auth.dto.UserLoginDTORequest;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;

public interface AuthService {

    UserDTOResponse register(UserRegisterDTORequest dto);

    UserDTOResponse login(UserLoginDTORequest dto);
}