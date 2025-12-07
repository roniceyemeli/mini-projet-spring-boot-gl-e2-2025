package com.service.community.service;


import com.service.community.entity.Community;

import java.util.List;

public interface IServiceCommunity {

    // CRUD Operations
    Community createCommunity(Community community);
    Community updateCommunity(Integer id, Community community);
    void deleteCommunity(Integer id);
    Community getCommunityById(Integer id);
    Community getCommunityBySlug(String slug);
    List<Community> getAllCommunities();

    // Search and Filter Operations
    List<Community> searchCommunities(String keyword);
    List<Community> getActiveCommunities();
    List<Community> getCommunitiesByStatus(Boolean isActive);
    List<Community> getTopCommunitiesByMembers(Integer limit);

    // Status Management
    Community activateCommunity(Integer communityId);
    Community deactivateCommunity(Integer communityId);

    // Validation
    boolean communityExistsBySlug(String slug);
    boolean communityExistsByTitle(String title);

    // Statistics
    Long getTotalCommunities();
    Long getTotalActiveCommunities();
    Integer getTotalMembersAcrossCommunities();
}