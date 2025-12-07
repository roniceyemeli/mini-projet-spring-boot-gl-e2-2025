package com.service.event.config;

import com.service.event.dto.event.EventDTO;
import com.service.event.dto.event.EventResponseDTO;
import com.service.event.entity.Event;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuration pour une correspondance stricte
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        // Configuration spécifique pour Event
        configureEventMappings(modelMapper);

        return modelMapper;
    }

    private void configureEventMappings(ModelMapper modelMapper) {
        // Configuration pour Event -> EventResponseDTO
        modelMapper.createTypeMap(com.service.event.entity.Event.class,
                        com.service.event.dto.event.EventResponseDTO.class)
                .addMapping(Event::getId, EventResponseDTO::setId)
                .addMapping(Event::getTitle, EventResponseDTO::setTitle)
                .addMapping(Event::getDescription, EventResponseDTO::setDescription)
                .addMapping(Event::getSlug, EventResponseDTO::setSlug)
                .addMapping(Event::getLocation, EventResponseDTO::setLocation)
                .addMapping(Event::getVenueDetails, EventResponseDTO::setVenueDetails)
                .addMapping(Event::getStartDate, EventResponseDTO::setStartDate)
                .addMapping(Event::getStartTime, EventResponseDTO::setStartTime)
                .addMapping(Event::getEndDate, EventResponseDTO::setEndDate)
                .addMapping(Event::getEndTime, EventResponseDTO::setEndTime)
                .addMapping(Event::getStatus, EventResponseDTO::setStatus)
                .addMapping(Event::getMaxParticipants, EventResponseDTO::setMaxParticipants)
                .addMapping(Event::getCurrentParticipants, EventResponseDTO::setCurrentParticipants)
                .addMapping(Event::getRegistrationFee, EventResponseDTO::setRegistrationFee)
                .addMapping(Event::getOrganizerId, EventResponseDTO::setOrganizerId)
                .addMapping(Event::getOrganizerType, EventResponseDTO::setOrganizerType)
                .addMapping(Event::getContactEmail, EventResponseDTO::setContactEmail)
                .addMapping(Event::getContactPhone, EventResponseDTO::setContactPhone)
                .addMapping(Event::getImageUrl, EventResponseDTO::setImageUrl)
                .addMapping(Event::getIsOnline, EventResponseDTO::setIsOnline)
                .addMapping(Event::getOnlineLink, EventResponseDTO::setOnlineLink)
                .addMapping(Event::getRequiresApproval, EventResponseDTO::setRequiresApproval)
                .addMapping(Event::getRegistrationDeadline, EventResponseDTO::setRegistrationDeadline)
                .addMapping(Event::getCategory, EventResponseDTO::setCategory)
                .addMapping(Event::getTags, EventResponseDTO::setTags)
                .addMapping(Event::getIsFeatured, EventResponseDTO::setIsFeatured)
                .addMapping(Event::getVisibility, EventResponseDTO::setVisibility)
                .addMapping(Event::getCreatedAt, EventResponseDTO::setCreatedAt)
                .addMapping(Event::getUpdatedAt, EventResponseDTO::setUpdatedAt)
                .addMapping(Event::getCreatedBy, EventResponseDTO::setCreatedBy)
                .addMapping(Event::getUpdatedBy, EventResponseDTO::setUpdatedBy);

        // Configuration pour EventDTO -> Event
        modelMapper.createTypeMap(com.service.event.dto.event.EventDTO.class,
                        com.service.event.entity.Event.class)
                .addMapping(EventDTO::getTitle, Event::setTitle)
                .addMapping(EventDTO::getDescription, Event::setDescription)
                .addMapping(EventDTO::getSlug, Event::setSlug)
                .addMapping(EventDTO::getLocation, Event::setLocation)
                .addMapping(EventDTO::getVenueDetails, Event::setVenueDetails)
                .addMapping(EventDTO::getStartDate, Event::setStartDate)
                .addMapping(EventDTO::getStartTime, Event::setStartTime)
                .addMapping(EventDTO::getEndDate, Event::setEndDate)
                .addMapping(EventDTO::getEndTime, Event::setEndTime)
                .addMapping(EventDTO::getStatus, Event::setStatus)
                .addMapping(EventDTO::getMaxParticipants, Event::setMaxParticipants)
                .addMapping(EventDTO::getRegistrationFee, Event::setRegistrationFee)
                .addMapping(EventDTO::getOrganizerId, Event::setOrganizerId)
                .addMapping(EventDTO::getOrganizerType, Event::setOrganizerType)
                .addMapping(EventDTO::getContactEmail, Event::setContactEmail)
                .addMapping(EventDTO::getContactPhone, Event::setContactPhone)
                .addMapping(EventDTO::getImageUrl, Event::setImageUrl)
                .addMapping(EventDTO::getIsOnline, Event::setIsOnline)
                .addMapping(EventDTO::getOnlineLink, Event::setOnlineLink)
                .addMapping(EventDTO::getRequiresApproval, Event::setRequiresApproval)
                .addMapping(EventDTO::getRegistrationDeadline, Event::setRegistrationDeadline)
                .addMapping(EventDTO::getCategory, Event::setCategory)
                .addMapping(EventDTO::getTags, Event::setTags)
                .addMapping(EventDTO::getIsFeatured, Event::setIsFeatured)
                .addMapping(EventDTO::getVisibility, Event::setVisibility)
                .addMapping(EventDTO::getCreatedBy, Event::setCreatedBy)
                .addMapping(EventDTO::getUpdatedBy, Event::setUpdatedBy);
    }
}