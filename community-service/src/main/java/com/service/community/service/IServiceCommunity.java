package com.service.community.service;


import com.service.community.dto.CommunityMinimalDTO;
import com.service.community.entity.Community;

import java.util.List;
import java.util.UUID;

public interface IServiceCommunity {

    // CRUD Operations
    Community createCommunity(Community community);
    Community updateCommunity(UUID id, Community community);
    void deleteCommunity(UUID id);
    Community getCommunityById(UUID id);
    Community getCommunityBySlug(String slug);
    List<Community> getAllCommunities();

    // Search and Filter Operations
    List<Community> searchCommunities(String keyword);
    List<Community> getActiveCommunities();
    List<Community> getCommunitiesByStatus(Boolean isActive);
    List<Community> getTopCommunitiesByMembers(Integer limit);

    // Status Management
    Community activateCommunity(UUID communityId);
    Community deactivateCommunity(UUID communityId);

    // Validation
    boolean communityExistsBySlug(String slug);
    boolean communityExistsByTitle(String title);

    // Statistics
    Long getTotalCommunities();
    Long getTotalActiveCommunities();
    Long getTotalMembersAcrossCommunities();

    // Minimal DTO for inter-service communication
    CommunityMinimalDTO getCommunityMinimalById(UUID id);
}