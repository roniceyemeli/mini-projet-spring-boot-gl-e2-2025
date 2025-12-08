package com.service.school.config;

import com.service.school.dto.SchoolDTO;
import com.service.school.dto.SchoolResponseDTO;
import com.service.school.entity.School;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchoolMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuration pour une correspondance stricte
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        // Configuration spécifique pour School
        configureSchoolMappings(modelMapper);

        return modelMapper;
    }

    private void configureSchoolMappings(ModelMapper modelMapper) {
        // Configuration pour School -> SchoolResponseDTO
        modelMapper.createTypeMap(School.class, SchoolResponseDTO.class)
                .addMapping(School::getId, SchoolResponseDTO::setId)
                .addMapping(School::getName, SchoolResponseDTO::setName)
                .addMapping(School::getTitle, SchoolResponseDTO::setTitle)
                .addMapping(School::getDescription, SchoolResponseDTO::setDescription)
                .addMapping(School::getSlug, SchoolResponseDTO::setSlug)
                .addMapping(School::getAddress, SchoolResponseDTO::setAddress)
                .addMapping(School::getFullAddress, SchoolResponseDTO::setFullAddress)
                .addMapping(School::getEmail, SchoolResponseDTO::setEmail)
                .addMapping(School::getWebsite, SchoolResponseDTO::setWebsite)
                .addMapping(School::getPhoneNumber, SchoolResponseDTO::setPhoneNumber)
                .addMapping(School::getFaxNumber, SchoolResponseDTO::setFaxNumber)
                .addMapping(School::getFoundingYear, SchoolResponseDTO::setFoundingYear)
                .addMapping(School::getType, SchoolResponseDTO::setType)
                .addMapping(School::getStatus, SchoolResponseDTO::setStatus)
                .addMapping(School::getAccreditationNumber, SchoolResponseDTO::setAccreditationNumber)
                .addMapping(School::getTaxId, SchoolResponseDTO::setTaxId)
                .addMapping(School::getRegistrationNumber, SchoolResponseDTO::setRegistrationNumber)
                .addMapping(School::getLogoUrl, SchoolResponseDTO::setLogoUrl)
                .addMapping(School::getBannerUrl, SchoolResponseDTO::setBannerUrl)
                .addMapping(School::getCountry, SchoolResponseDTO::setCountry)
                .addMapping(School::getCity, SchoolResponseDTO::setCity)
                .addMapping(School::getPostalCode, SchoolResponseDTO::setPostalCode)
                .addMapping(School::getLatitude, SchoolResponseDTO::setLatitude)
                .addMapping(School::getLongitude, SchoolResponseDTO::setLongitude)
                .addMapping(School::getTotalStudents, SchoolResponseDTO::setTotalStudents)
                .addMapping(School::getTotalTeachers, SchoolResponseDTO::setTotalTeachers)
                .addMapping(School::getTotalStaff, SchoolResponseDTO::setTotalStaff)
                .addMapping(School::getIsPublic, SchoolResponseDTO::setIsPublic)
                .addMapping(School::getTuitionRange, SchoolResponseDTO::setTuitionRange)
                .addMapping(School::getAdmissionsEmail, SchoolResponseDTO::setAdmissionsEmail)
                .addMapping(School::getAdmissionsPhone, SchoolResponseDTO::setAdmissionsPhone)
                .addMapping(School::getContactPerson, SchoolResponseDTO::setContactPerson)
                .addMapping(School::getContactPosition, SchoolResponseDTO::setContactPosition)
                .addMapping(School::getFacebookUrl, SchoolResponseDTO::setFacebookUrl)
                .addMapping(School::getTwitterUrl, SchoolResponseDTO::setTwitterUrl)
                .addMapping(School::getLinkedinUrl, SchoolResponseDTO::setLinkedinUrl)
                .addMapping(School::getInstagramUrl, SchoolResponseDTO::setInstagramUrl)
                .addMapping(School::getIsFeatured, SchoolResponseDTO::setIsFeatured)
                .addMapping(School::getRanking, SchoolResponseDTO::setRanking)
                .addMapping(School::getAccreditationStatus, SchoolResponseDTO::setAccreditationStatus)
                .addMapping(School::getAccreditationExpiryDate, SchoolResponseDTO::setAccreditationExpiryDate)
                .addMapping(School::getMotto, SchoolResponseDTO::setMotto)
                .addMapping(School::getVision, SchoolResponseDTO::setVision)
                .addMapping(School::getMission, SchoolResponseDTO::setMission)
                .addMapping(School::getIsActive, SchoolResponseDTO::setIsActive)
                .addMapping(School::getVerified, SchoolResponseDTO::setVerified)
                .addMapping(School::getVerificationDate, SchoolResponseDTO::setVerificationDate)
                .addMapping(School::getCreatedAt, SchoolResponseDTO::setCreatedAt)
                .addMapping(School::getUpdatedAt, SchoolResponseDTO::setUpdatedAt)
                .addMapping(School::getCreatedBy, SchoolResponseDTO::setCreatedBy)
                .addMapping(School::getUpdatedBy, SchoolResponseDTO::setUpdatedBy);

        // Configuration pour SchoolDTO -> School
        modelMapper.createTypeMap(SchoolDTO.class, School.class)
                .addMapping(SchoolDTO::getName, School::setName)
                .addMapping(SchoolDTO::getTitle, School::setTitle)
                .addMapping(SchoolDTO::getDescription, School::setDescription)
                .addMapping(SchoolDTO::getSlug, School::setSlug)
                .addMapping(SchoolDTO::getAddress, School::setAddress)
                .addMapping(SchoolDTO::getFullAddress, School::setFullAddress)
                .addMapping(SchoolDTO::getEmail, School::setEmail)
                .addMapping(SchoolDTO::getWebsite, School::setWebsite)
                .addMapping(SchoolDTO::getPhoneNumber, School::setPhoneNumber)
                .addMapping(SchoolDTO::getFaxNumber, School::setFaxNumber)
                .addMapping(SchoolDTO::getFoundingYear, School::setFoundingYear)
                .addMapping(SchoolDTO::getType, School::setType)
                .addMapping(SchoolDTO::getAccreditationNumber, School::setAccreditationNumber)
                .addMapping(SchoolDTO::getTaxId, School::setTaxId)
                .addMapping(SchoolDTO::getRegistrationNumber, School::setRegistrationNumber)
                .addMapping(SchoolDTO::getLogoUrl, School::setLogoUrl)
                .addMapping(SchoolDTO::getBannerUrl, School::setBannerUrl)
                .addMapping(SchoolDTO::getCountry, School::setCountry)
                .addMapping(SchoolDTO::getCity, School::setCity)
                .addMapping(SchoolDTO::getPostalCode, School::setPostalCode)
                .addMapping(SchoolDTO::getLatitude, School::setLatitude)
                .addMapping(SchoolDTO::getLongitude, School::setLongitude)
                .addMapping(SchoolDTO::getIsPublic, School::setIsPublic)
                .addMapping(SchoolDTO::getTuitionRange, School::setTuitionRange)
                .addMapping(SchoolDTO::getAdmissionsEmail, School::setAdmissionsEmail)
                .addMapping(SchoolDTO::getAdmissionsPhone, School::setAdmissionsPhone)
                .addMapping(SchoolDTO::getContactPerson, School::setContactPerson)
                .addMapping(SchoolDTO::getContactPosition, School::setContactPosition)
                .addMapping(SchoolDTO::getFacebookUrl, School::setFacebookUrl)
                .addMapping(SchoolDTO::getTwitterUrl, School::setTwitterUrl)
                .addMapping(SchoolDTO::getLinkedinUrl, School::setLinkedinUrl)
                .addMapping(SchoolDTO::getInstagramUrl, School::setInstagramUrl)
                .addMapping(SchoolDTO::getIsFeatured, School::setIsFeatured)
                .addMapping(SchoolDTO::getRanking, School::setRanking)
                .addMapping(SchoolDTO::getAccreditationStatus, School::setAccreditationStatus)
                .addMapping(SchoolDTO::getAccreditationExpiryDate, School::setAccreditationExpiryDate)
                .addMapping(SchoolDTO::getMotto, School::setMotto)
                .addMapping(SchoolDTO::getVision, School::setVision)
                .addMapping(SchoolDTO::getMission, School::setMission)
                .addMapping(SchoolDTO::getIsActive, School::setIsActive)
                .addMapping(SchoolDTO::getVerified, School::setVerified)
                .addMapping(SchoolDTO::getCreatedBy, School::setCreatedBy)
                .addMapping(SchoolDTO::getUpdatedBy, School::setUpdatedBy);
    }
}