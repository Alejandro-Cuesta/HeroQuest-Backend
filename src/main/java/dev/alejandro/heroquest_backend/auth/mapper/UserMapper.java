package dev.alejandro.heroquest_backend.auth.mapper;

import dev.alejandro.heroquest_backend.auth.dto.UserDTOResponse;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;
import dev.alejandro.heroquest_backend.auth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Usamos MapStruct, que genera el código de conversión
 * durante la compilación (no en tiempo de ejecución).
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Instancia del mapper generada automáticamente por MapStruct.
     * Se inyectará como un bean de Spring.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

     // Convierte un objeto UserRegisterDTORequest → User (para guardar en la base de datos)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "displayName", source = "displayName")
    @Mapping(target = "username", source = "username")
    User toUser(UserRegisterDTORequest dto);

    /**
     * Convierte un objeto User → UserDTOResponse (para devolver al frontend)
     * Aquí nunca incluimos la contraseña por seguridad.
     */
    UserDTOResponse toUserDTOResponse(User user);
}
