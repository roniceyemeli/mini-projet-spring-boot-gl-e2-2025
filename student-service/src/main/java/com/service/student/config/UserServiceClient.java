package com.service.student.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}/minimal")
    UserMinimalDTO getUserMinimalById(@PathVariable("id") Long id);

    @GetMapping("/api/users/email/{email}/minimal")
    UserMinimalDTO getUserMinimalByEmail(@PathVariable("email") String email);
}