package com.service.user.config;

import com.service.user.dto.role.RoleDTO;
import com.service.user.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom configuration for Role mapping
        modelMapper.createTypeMap(Role.class, RoleDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Role::getId, RoleDTO::setId);
                    mapper.map(Role::getName, RoleDTO::setName);
                    mapper.map(Role::getDescription, RoleDTO::setDescription);
                    mapper.map(Role::getPermissions, RoleDTO::setPermissions);
                    mapper.map(Role::getIsDefault, RoleDTO::setIsDefault);
                });


        return modelMapper;
    }
}