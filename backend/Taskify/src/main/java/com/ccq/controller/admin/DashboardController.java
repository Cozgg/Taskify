package com.ccq.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.service.UserService;

@RestController
public class DashboardController {

    @Autowired
    private UserService userSer;

    @GetMapping("/admin/dashboard")
    public ResponseEntity<Map<String, Object>> getDashBoard() {
        Map<String, Object> data = new HashMap<>();
        long countUsers = this.userSer.countUsers();
        data.put("countUsers", countUsers);
        data.put("countWorkspaces", this.userSer.countWorkspaces());
        data.put("countBoards", this.userSer.countBoards());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
