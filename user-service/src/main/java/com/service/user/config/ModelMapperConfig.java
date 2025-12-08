package com.service.user.config;

import com.service.user.dto.role.RoleDTO;
import com.service.user.dto.user.UserDTO;
import com.service.user.entity.Role;
import com.service.user.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // User to UserDTO mapping
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    // Map the role object directly - ModelMapper will automatically
                    // convert Role to RoleDTO using the mapping below
                    mapper.map(User::getRole, UserDTO::setRole);
                });

        // Role to RoleDTO mapping
        modelMapper.createTypeMap(Role.class, RoleDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Role::getId, RoleDTO::setId);
                    mapper.map(Role::getName, RoleDTO::setName);
                    mapper.map(Role::getDescription, RoleDTO::setDescription);
                    mapper.map(Role::getPermissions, RoleDTO::setPermissions);
                    mapper.map(Role::getIsDefault, RoleDTO::setIsDefault);
                    mapper.map(Role::getIsSystem, RoleDTO::setIsSystem);
                    mapper.map(Role::getCreatedAt, RoleDTO::setCreatedAt);
                    mapper.map(Role::getUpdatedAt, RoleDTO::setUpdatedAt);
                });

        return modelMapper;
    }
}