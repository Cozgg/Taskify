package com.ccq.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.service.UserService;
import com.ccq.service.WorkspaceService;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/workspaces")
    public ResponseEntity<List<Workspace>> getWorkspaceCards(@RequestParam Map<String, String> params) {
        List<Workspace> workspaces = this.workspaceService.getWorkspaces(params);
        return new ResponseEntity<>(workspaces, HttpStatus.OK);
    }

    @GetMapping("/workspaces/{id}/users")
    public ResponseEntity<List<User>> getUsersByWorkspace(
            @PathVariable("id") int workspaceId,
            @RequestParam Map<String, String> params) {

        List<User> users = this.userService.getUsersByWorkspace(workspaceId, params);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = this.userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Void> addOrUpdateUser(@RequestBody User user) {
        this.userService.addOrUpdateUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
        User user = this.userService.getUserById(id);
        if (user != null) {
            this.userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<User> getUserByEmail(@RequestParam("email") String email) {
        User user = this.userService.getUserByEmail(email); //
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
