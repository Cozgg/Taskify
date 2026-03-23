/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import java.util.List;
import java.util.Map;

import com.ccq.pojo.User;

/**
 *
 * @author Admin
 */
public interface UserRepository {

    List<User> getUsers(Map<String, String> params);

    User findUserById(int id);

    User findUserByEmail(String email);

    boolean existEmail(String email);

    void addOrUpdateUser(User u);

    void deleteUser(int id);

    User getUserByUsername(String username);

    Long count();
}
