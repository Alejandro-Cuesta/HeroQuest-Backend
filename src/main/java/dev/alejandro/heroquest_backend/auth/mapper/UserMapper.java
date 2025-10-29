package dev.alejandro.heroquest_backend.auth.mapper;

import dev.alejandro.heroquest_backend.auth.dto.UserDTOResponse;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;
import dev.alejandro.heroquest_backend.auth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper con MapStruct para convertir entre User y sus DTOs.
 * Evita exponer entidades directamente en la API y protege la contraseña.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Instancia automática generada por MapStruct.
     * Se puede inyectar como bean de Spring.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Convierte un DTO de registro en entidad User.
     * Ignora el ID y el héroe porque se asignarán automáticamente.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hero", ignore = true) // ignoramos hero al crear User
    @Mapping(target = "password", source = "password")
    @Mapping(target = "displayName", source = "displayName")
    @Mapping(target = "username", source = "username")
    User toUser(UserRegisterDTORequest dto);

    /**
     * Convierte un User a DTO de salida.
     * Devuelve los datos de usuario sin incluir la contraseña.
     * Ya no incluimos hero porque el DTO actual no tiene esa propiedad.
     */
    UserDTOResponse toUserDTOResponse(User user);
}