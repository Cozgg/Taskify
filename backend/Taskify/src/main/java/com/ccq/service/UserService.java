package com.ccq.service;

import java.util.List;
import java.util.Map;

import com.ccq.pojo.User;

public interface UserService {
    
    List<User> getUsers(Map<String, String> params);

    void addOrUpdateUser(User u);

    User getUserById(int id);

    User getUserByEmail(String email);

    boolean checkExistEmail(String email);

    void deleteUser(int id);

    Long countUsers();

    Long countBoards();

    User getUserByUsername(String username);

    boolean existsByUsername(String username);

    Long countWorkspaces();
}
