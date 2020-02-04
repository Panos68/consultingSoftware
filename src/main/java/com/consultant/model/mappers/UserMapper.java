package com.consultant.model.mappers;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}