package com.service.community.service;

import com.service.community.entity.Community;
import com.service.community.repository.CommunityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ServiceCommunity implements IServiceCommunity {

    private final CommunityRepository communityRepository;

    @Override
    public Community createCommunity(Community community) {
        // Validate uniqueness
        if (communityRepository.existsByTitle(community.getTitle())) {
            throw new RuntimeException("Community with title '" + community.getTitle() + "' already exists");
        }

        if (community.getSlug() != null && communityRepository.existsBySlug(community.getSlug())) {
            throw new RuntimeException("Community with slug '" + community.getSlug() + "' already exists");
        }

        // Initialize default values
        if (community.getMemberCount() == null) {
            community.setMemberCount(0);
        }

        if (community.getIsActive() == null) {
            community.setIsActive(true);
        }

        // Initialize collections
//        community.setStudents(new java.util.HashSet<>());
//        community.setEvents(new java.util.HashSet<>());

        return communityRepository.save(community);
    }

    @Override
    public Community updateCommunity(Integer id, Community community) {
        Community existingCommunity = communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community not found with id: " + id));

        // Check if title changed and is still unique
        if (!existingCommunity.getTitle().equals(community.getTitle()) &&
                communityRepository.existsByTitle(community.getTitle())) {
            throw new RuntimeException("Community with title '" + community.getTitle() + "' already exists");
        }

        // Check if slug changed and is still unique
        if (community.getSlug() != null &&
                !community.getSlug().equals(existingCommunity.getSlug()) &&
                communityRepository.existsBySlug(community.getSlug())) {
            throw new RuntimeException("Community with slug '" + community.getSlug() + "' already exists");
        }

        existingCommunity.setTitle(community.getTitle());
        existingCommunity.setDescription(community.getDescription());
        existingCommunity.setSlug(community.getSlug());
        existingCommunity.setWebsite(community.getWebsite());
        existingCommunity.setContactEmail(community.getContactEmail());
        existingCommunity.setContactPhone(community.getContactPhone());
        existingCommunity.setFoundingYear(community.getFoundingYear());
        existingCommunity.setLogoUrl(community.getLogoUrl());
        existingCommunity.setIsActive(community.getIsActive());
        existingCommunity.setUpdatedAt(LocalDateTime.now());

        // Only update member count if explicitly provided
        if (community.getMemberCount() != null) {
            existingCommunity.setMemberCount(community.getMemberCount());
        }

        return communityRepository.save(existingCommunity);
    }

    @Override
    public void deleteCommunity(Integer id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community not found with id: " + id));

        // Check if community has members
        if (community.getMemberCount() > 0) {
            throw new RuntimeException("Cannot delete community that has members. Deactivate it instead.");
        }

        communityRepository.delete(community);
    }

    @Override
    public Community getCommunityById(Integer id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community not found with id: " + id));
    }

    @Override
    public Community getCommunityBySlug(String slug) {
        return communityRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Community not found with slug: " + slug));
    }

    @Override
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    @Override
    public List<Community> searchCommunities(String keyword) {
        return communityRepository.searchCommunities(keyword);
    }

    @Override
    public List<Community> getActiveCommunities() {
        return communityRepository.findByIsActiveTrue();
    }

    @Override
    public List<Community> getCommunitiesByStatus(Boolean isActive) {
        return communityRepository.findByIsActive(isActive);
    }

    @Override
    public List<Community> getTopCommunitiesByMembers(Integer limit) {
        return communityRepository.findAllOrderByMemberCountDesc()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Community incrementMemberCount(Integer communityId) {
        Community community = getCommunityById(communityId);
        community.setMemberCount(community.getMemberCount() + 1);
        community.setUpdatedAt(LocalDateTime.now());
        return communityRepository.save(community);
    }

    @Override
    public Community decrementMemberCount(Integer communityId) {
        Community community = getCommunityById(communityId);
        int newCount = community.getMemberCount() - 1;
        if (newCount < 0) newCount = 0;
        community.setMemberCount(newCount);
        community.setUpdatedAt(LocalDateTime.now());
        return communityRepository.save(community);
    }

    @Override
    public Community updateMemberCount(Integer communityId, Integer memberCount) {
        Community community = getCommunityById(communityId);
        community.setMemberCount(memberCount);
        community.setUpdatedAt(LocalDateTime.now());
        return communityRepository.save(community);
    }

    @Override
    public Community activateCommunity(Integer communityId) {
        Community community = getCommunityById(communityId);
        community.setIsActive(true);
        community.setUpdatedAt(LocalDateTime.now());
        return communityRepository.save(community);
    }

    @Override
    public Community deactivateCommunity(Integer communityId) {
        Community community = getCommunityById(communityId);
        community.setIsActive(false);
        community.setUpdatedAt(LocalDateTime.now());
        return communityRepository.save(community);
    }

    @Override
    public boolean communityExistsBySlug(String slug) {
        return communityRepository.existsBySlug(slug);
    }

    @Override
    public boolean communityExistsByTitle(String title) {
        return communityRepository.existsByTitle(title);
    }

    @Override
    public Long getTotalCommunities() {
        return communityRepository.count();
    }

    @Override
    public Long getTotalActiveCommunities() {
        return communityRepository.findByIsActiveTrue().stream().count();
    }

    @Override
    public Integer getTotalMembersAcrossCommunities() {
        return communityRepository.findAll().stream()
                .mapToInt(Community::getMemberCount)
                .sum();
    }
}