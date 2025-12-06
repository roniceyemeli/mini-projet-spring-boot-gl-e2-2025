package com.service.community.utils;

import com.service.community.dto.CommunityDTO;
import com.service.community.entity.Community;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommunityModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom configuration for Community mapping
        modelMapper.createTypeMap(Community.class, CommunityDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Community::getId, CommunityDTO::setId);
                    mapper.map(Community::getTitle, CommunityDTO::setTitle);
                    mapper.map(Community::getDescription, CommunityDTO::setDescription);
                    mapper.map(Community::getSlug, CommunityDTO::setSlug);
                    mapper.map(Community::getWebsite, CommunityDTO::setWebsite);
                    mapper.map(Community::getContactEmail, CommunityDTO::setContactEmail);
                    mapper.map(Community::getContactPhone, CommunityDTO::setContactPhone);
                    mapper.map(Community::getFoundingYear, CommunityDTO::setFoundingYear);
                    mapper.map(Community::getMemberCount, CommunityDTO::setMemberCount);
                    mapper.map(Community::getIsActive, CommunityDTO::setIsActive);
                    mapper.map(Community::getLogoUrl, CommunityDTO::setLogoUrl);
                    mapper.map(Community::getCreatedBy, CommunityDTO::setCreatedBy);
                });

        return modelMapper;
    }
}