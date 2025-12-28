package com.service.student.config;

import com.service.user.dto.user.UserMinimalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}/minimal")
    UserMinimalDTO getUserMinimalById(@PathVariable("id") UUID id);

    @GetMapping("/api/users/email/{email}/minimal")
    UserMinimalDTO getUserMinimalByEmail(@PathVariable("email") String email);
}
