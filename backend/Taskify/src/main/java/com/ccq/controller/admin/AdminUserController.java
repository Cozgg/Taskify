package com.ccq.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.ccq.pojo.request.ReqAdminUserDTO;
import com.ccq.pojo.response.ResUserDTO;
import com.ccq.pojo.response.ResUserPageDTO;
import com.ccq.pojo.response.RestResponse;
import com.ccq.service.UserService;
import com.ccq.utils.DTOMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @GetMapping
    public ResponseEntity<RestResponse<ResUserPageDTO>> getUsers(@RequestParam Map<String, String> params) {
        List<User> users = this.userService.getUsers(params);
        Long totalItems = this.userService.countUsers(params);

        int page = 1;
        int pageSize = Integer.parseInt(this.env.getProperty("user.page_size", "10"));
        if (params != null) {
            try {
                page = Integer.parseInt(params.getOrDefault("page", "1"));
            } catch (NumberFormatException ignored) {
                page = 1;
            }
            try {
                pageSize = Integer.parseInt(params.getOrDefault("size", String.valueOf(pageSize)));
            } catch (NumberFormatException ignored) {
                // keep default from configs
            }
        }
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = Integer.parseInt(this.env.getProperty("user.page_size", "10"));
        }

        ResUserPageDTO pageDto = DTOMapper.toUserPageDTO(users, totalItems, page, pageSize);

        RestResponse<ResUserPageDTO> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setData(pageDto);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") int id) {
        User user = this.userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(DTOMapper.toUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> addOrUpdateUser(@Valid @RequestBody ReqAdminUserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setAvatar(dto.getAvatar());
        user.setCreatedDate(new Date());
        user.setRole(dto.getRole() != null ? dto.getRole() : "USER");
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
}
