package com.ccq.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccq.pojo.User;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WorkspaceRepository workspaceRepo;

    @Autowired
    private BoardRepository boardRepo;

    @Override
    public List<User> getUsers(Map<String, String> params) {
        return this.userRepo.getUsers(params);
    }

    @Override
    public void addOrUpdateUser(User u) {
        this.userRepo.addOrUpdateUser(u);
    }

    @Override
    public void deleteUser(int id) {
        this.userRepo.deleteUser(id);
    }

    @Override
    public User getUserById(int id) {
        return this.userRepo.findUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepo.findUserByEmail(email);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return this.userRepo.existEmail(email);
    }

    @Override
    public Long countUsers() {
        return this.userRepo.count();
    }

    @Override
    public Long countBoards() {
        return this.boardRepo.count();
    }

    @Override
    public Long countWorkspaces() {
        return this.workspaceRepo.count();
    }

}
