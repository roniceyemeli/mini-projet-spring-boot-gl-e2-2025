package com.service.student.config;

import com.service.school.dto.SchoolMinimalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "school-service")
public interface SchoolServiceClient {

    @GetMapping("/api/schools/{id}/minimal")
    SchoolMinimalDTO getSchoolMinimalById(@PathVariable("id") UUID id);
}

