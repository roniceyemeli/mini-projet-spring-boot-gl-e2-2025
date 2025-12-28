package com.service.student.config;

import com.service.community.dto.CommunityMinimalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "community-service")
public interface CommunityServiceClient {

    @GetMapping("/api/communities/{id}/minimal")
    CommunityMinimalDTO getCommunityMinimalById(@PathVariable("id") UUID id);
}

