package com.service.school.controller;

import com.service.school.dto.SchoolDTO;
import com.service.school.dto.SchoolResponseDTO;
import com.service.school.entity.School;
import com.service.school.enums.SchoolStatus;
import com.service.school.enums.SchoolType;
import com.service.school.service.IServiceSchool;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/schools")
@CrossOrigin(origins = "*")
public class SchoolRestController {

    private final IServiceSchool schoolService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<SchoolResponseDTO> createSchool(@Valid @RequestBody SchoolDTO schoolDTO) {
        School school = modelMapper.map(schoolDTO, School.class);
        School createdSchool = schoolService.createSchool(school);
        SchoolResponseDTO responseDTO = modelMapper.map(createdSchool, SchoolResponseDTO.class);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponseDTO> getSchoolById(@PathVariable UUID id) {
        School school = schoolService.getSchoolById(id);
        SchoolResponseDTO responseDTO = modelMapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<SchoolResponseDTO> getSchoolBySlug(@PathVariable String slug) {
        School school = schoolService.getSchoolBySlug(slug)
                .orElseThrow(() -> new RuntimeException("School not found with slug: " + slug));
        SchoolResponseDTO responseDTO = modelMapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<SchoolResponseDTO>> getAllSchools(
            @RequestParam(required = false) SchoolType type,
            @RequestParam(required = false) SchoolStatus status,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Boolean verified) {

        List<School> schools;

        if (type != null) {
            schools = schoolService.getSchoolsByType(type);
        } else if (status != null) {
            schools = schoolService.getSchoolsByStatus(status);
        } else if (country != null) {
            schools = schoolService.getSchoolsByCountry(country);
        } else if (city != null) {
            schools = schoolService.getSchoolsByCity(city);
        } else if (featured != null && featured) {
            schools = schoolService.getFeaturedSchools();
        } else if (verified != null && verified) {
            schools = schoolService.getVerifiedSchools();
        } else {
            schools = schoolService.getAllSchools();
        }

        List<SchoolResponseDTO> responseDTOs = schools.stream()
                .map(school -> modelMapper.map(school, SchoolResponseDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolResponseDTO> updateSchool(
            @PathVariable UUID id,
            @Valid @RequestBody SchoolDTO schoolDTO) {
        School school = modelMapper.map(schoolDTO, School.class);
        School updatedSchool = schoolService.updateSchool(id, school);
        SchoolResponseDTO responseDTO = modelMapper.map(updatedSchool, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable UUID id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<SchoolResponseDTO>> searchSchools(@RequestParam String keyword) {
        List<School> schools = schoolService.searchSchools(keyword);
        List<SchoolResponseDTO> responseDTOs = schools.stream()
                .map(school -> modelMapper.map(school, SchoolResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<SchoolResponseDTO>> getFeaturedSchools(
            @RequestParam(defaultValue = "10") int limit) {
        List<School> schools = schoolService.getTopFeaturedSchools(limit);
        List<SchoolResponseDTO> responseDTOs = schools.stream()
                .map(school -> modelMapper.map(school, SchoolResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PatchMapping("/{id}/feature")
    public ResponseEntity<SchoolResponseDTO> setFeaturedStatus(
            @PathVariable UUID id,
            @RequestParam boolean featured) {
        School school = schoolService.setFeaturedStatus(id, featured);
        SchoolResponseDTO responseDTO = modelMapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SchoolResponseDTO> changeSchoolStatus(
            @PathVariable UUID id,
            @RequestParam SchoolStatus status) {
        School school = schoolService.changeSchoolStatus(id, status);
        SchoolResponseDTO responseDTO = modelMapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SchoolResponseDTO> activateSchool(@PathVariable UUID id) {
        School school = schoolService.activateSchool(id);
        SchoolResponseDTO responseDTO = modelMapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SchoolResponseDTO> deactivateSchool(@PathVariable UUID id) {
        School school = schoolService.deactivateSchool(id);
        SchoolResponseDTO responseDTO = modelMapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<SchoolResponseDTO> verifySchool(@PathVariable UUID id) {
        School school = schoolService.verifySchool(id);
        SchoolResponseDTO responseDTO = modelMapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<SchoolResponseDTO> suspendSchool(@PathVariable UUID id) {
        School school = schoolService.suspendSchool(id);
        SchoolResponseDTO responseDTO = modelMapper.map(school, SchoolResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/public")
    public ResponseEntity<List<SchoolResponseDTO>> getPublicSchools() {
        List<School> schools = schoolService.getPublicSchools();
        List<SchoolResponseDTO> responseDTOs = schools.stream()
                .map(school -> modelMapper.map(school, SchoolResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/private")
    public ResponseEntity<List<SchoolResponseDTO>> getPrivateSchools() {
        List<School> schools = schoolService.getPrivateSchools();
        List<SchoolResponseDTO> responseDTOs = schools.stream()
                .map(school -> modelMapper.map(school, SchoolResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalSchoolsCount() {
        Long count = schoolService.getTotalSchoolsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/active")
    public ResponseEntity<Long> getActiveSchoolsCount() {
        Long count = schoolService.getActiveSchoolsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/featured")
    public ResponseEntity<Long> getFeaturedSchoolsCount() {
        Long count = schoolService.getFeaturedSchoolsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/verified")
    public ResponseEntity<Long> getVerifiedSchoolsCount() {
        Long count = schoolService.getVerifiedSchoolsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/categories")
    public ResponseEntity<List<Object[]>> getSchoolsCountByType() {
        List<Object[]> stats = schoolService.getSchoolsCountByType();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/statuses")
    public ResponseEntity<List<Object[]>> getSchoolsCountByStatus() {
        List<Object[]> stats = schoolService.getSchoolsCountByStatus();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/countries")
    public ResponseEntity<List<Object[]>> getSchoolsCountByCountry() {
        List<Object[]> stats = schoolService.getSchoolsCountByCountry();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/students-total")
    public ResponseEntity<Integer> getTotalStudentsAcrossSchools() {
        Integer total = schoolService.getTotalStudentsAcrossSchools();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/stats/teachers-total")
    public ResponseEntity<Integer> getTotalTeachersAcrossSchools() {
        Integer total = schoolService.getTotalTeachersAcrossSchools();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/{id}/can-admit")
    public ResponseEntity<Boolean> canSchoolAdmitStudents(@PathVariable UUID id) {
        boolean canAdmit = schoolService.canSchoolAdmitStudents(id);
        return ResponseEntity.ok(canAdmit);
    }

    @GetMapping("/{id}/is-accredited")
    public ResponseEntity<Boolean> isSchoolAccredited(@PathVariable UUID id) {
        boolean isAccredited = schoolService.isSchoolAccredited(id);
        return ResponseEntity.ok(isAccredited);
    }

    @GetMapping("/{id}/is-operational")
    public ResponseEntity<Boolean> isSchoolOperational(@PathVariable UUID id) {
        boolean isOperational = schoolService.isSchoolOperational(id);
        return ResponseEntity.ok(isOperational);
    }

    @GetMapping("/near-location")
    public ResponseEntity<List<SchoolResponseDTO>> getSchoolsNearLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10") Double radiusKm) {
        List<School> schools = schoolService.getSchoolsNearLocation(latitude, longitude, radiusKm);
        List<SchoolResponseDTO> responseDTOs = schools.stream()
                .map(school -> modelMapper.map(school, SchoolResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
}