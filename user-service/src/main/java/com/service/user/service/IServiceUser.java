package com.service.user.service;


import com.service.user.entity.User;

import java.util.List;

public interface IServiceUser {
    public User addUser(User user);
    public List<User> allUsers();
}
