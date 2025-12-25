package com.service.community.controller;

import com.service.community.dto.CommunityDTO;
import com.service.community.entity.Community;
import com.service.community.service.ServiceCommunity;
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
@RequestMapping("/api/communities")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CommunityRestController {

    private final ServiceCommunity communityService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<CommunityDTO> createCommunity(@Valid @RequestBody CommunityDTO communityDTO) {
        Community community = modelMapper.map(communityDTO, Community.class);
        Community createdCommunity = communityService.createCommunity(community);
        CommunityDTO responseDTO = modelMapper.map(createdCommunity, CommunityDTO.class);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommunityDTO>> getAllCommunities() {
        List<Community> communities = communityService.getAllCommunities();
        List<CommunityDTO> communityDTOs = communities.stream()
                .map(community -> modelMapper.map(community, CommunityDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(communityDTOs);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CommunityDTO>> getActiveCommunities() {
        List<Community> communities = communityService.getActiveCommunities();
        List<CommunityDTO> communityDTOs = communities.stream()
                .map(community -> modelMapper.map(community, CommunityDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(communityDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityDTO> getCommunityById(@PathVariable UUID id) {
        Community community = communityService.getCommunityById(id);
        CommunityDTO communityDTO = modelMapper.map(community, CommunityDTO.class);
        return ResponseEntity.ok(communityDTO);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CommunityDTO> getCommunityBySlug(@PathVariable String slug) {
        Community community = communityService.getCommunityBySlug(slug);
        CommunityDTO communityDTO = modelMapper.map(community, CommunityDTO.class);
        return ResponseEntity.ok(communityDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommunityDTO>> searchCommunities(@RequestParam String keyword) {
        List<Community> communities = communityService.searchCommunities(keyword);
        List<CommunityDTO> communityDTOs = communities.stream()
                .map(community -> modelMapper.map(community, CommunityDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(communityDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunityDTO> updateCommunity(@PathVariable UUID id,
                                                        @Valid @RequestBody CommunityDTO communityDTO) {
        Community community = modelMapper.map(communityDTO, Community.class);
        Community updatedCommunity = communityService.updateCommunity(id, community);
        CommunityDTO responseDTO = modelMapper.map(updatedCommunity, CommunityDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable UUID id) {
        communityService.deleteCommunity(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CommunityDTO> activateCommunity(@PathVariable UUID id) {
        Community activatedCommunity = communityService.activateCommunity(id);
        CommunityDTO communityDTO = modelMapper.map(activatedCommunity, CommunityDTO.class);
        return ResponseEntity.ok(communityDTO);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CommunityDTO> deactivateCommunity(@PathVariable UUID id) {
        Community deactivatedCommunity = communityService.deactivateCommunity(id);
        CommunityDTO communityDTO = modelMapper.map(deactivatedCommunity, CommunityDTO.class);
        return ResponseEntity.ok(communityDTO);
    }

    @GetMapping("/top/{limit}")
    public ResponseEntity<List<CommunityDTO>> getTopCommunities(@PathVariable Integer limit) {
        List<Community> communities = communityService.getTopCommunitiesByMembers(limit);
        List<CommunityDTO> communityDTOs = communities.stream()
                .map(community -> modelMapper.map(community, CommunityDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(communityDTOs);
    }

    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalCommunities() {
        Long total = communityService.getTotalCommunities();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/stats/active")
    public ResponseEntity<Long> getTotalActiveCommunities() {
        Long totalActive = communityService.getTotalActiveCommunities();
        return ResponseEntity.ok(totalActive);
    }
}