package com.service.user.controller;


import com.service.user.entity.User;
import com.service.user.service.IServiceUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/")
public class UserRestController {
    private IServiceUser serviceUser;
    @PostMapping("add")
    public User add(@RequestBody User user) {
        return serviceUser.addUser(user);
    }
    @GetMapping("all")
    public List<User> all(){
        return serviceUser.allUsers();
    }
}
