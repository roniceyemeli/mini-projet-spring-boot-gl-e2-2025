package com.service.user.utils;
import com.service.user.dto.role.RoleDTO;
import com.service.user.dto.user.CreateUserDTO;
import com.service.user.dto.user.UpdateUserDTO;
import com.service.user.dto.user.UserDTO;
import com.service.user.entity.Role;
import com.service.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;



@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapperConfig {

    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    User toEntity(CreateUserDTO createUserDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UpdateUserDTO updateDTO, @MappingTarget User user);

    RoleDTO toDTO(Role role);
    Role toEntity(RoleDTO roleDTO);
    Role toEntity(CreateRoleDTO createRoleDTO);

    SchoolDTO toDTO(School school);
    School toEntity(SchoolDTO schoolDTO);
    School toEntity(CreateSchoolDTO createSchoolDTO);
}