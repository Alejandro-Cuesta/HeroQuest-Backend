package dev.alejandro.heroquest_backend.inventory.mapper;

import dev.alejandro.heroquest_backend.inventory.dto.ItemDTORequest;
import dev.alejandro.heroquest_backend.inventory.dto.ItemDTOResponse;
import dev.alejandro.heroquest_backend.inventory.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper con MapStruct para convertir entre entidad Item y DTOs.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    Item toItem(ItemDTORequest dto);

    ItemDTOResponse toItemDTOResponse(Item item);
}
