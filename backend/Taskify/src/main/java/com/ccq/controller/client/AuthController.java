package com.ccq.controller.client;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.User;
import com.ccq.pojo.UserRole;
import com.ccq.pojo.request.ReqLoginDTO;
import com.ccq.pojo.request.ReqRegisterDTO;
import com.ccq.pojo.response.ResLoginDTO;
import com.ccq.pojo.response.RestResponse;
import com.ccq.service.UserService;
import com.ccq.utils.JwtUtil;
import com.ccq.utils.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<RestResponse<ResLoginDTO>> login(@Valid @RequestBody ReqLoginDTO request) {
        User user = userService.getUserByUsername(request.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("Tên đăng nhập không tồn tại");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Mật khẩu không đúng");
        }

        String accessToken = jwtUtil.generateToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        ResLoginDTO resLogin = new ResLoginDTO(accessToken, user.getId(), user.getUsername(), user.getRole());
        resLogin.setRefreshToken(refreshToken);

        RestResponse<ResLoginDTO> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Login thành công");
        res.setData(resLogin);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/register")
    public ResponseEntity<RestResponse<ResLoginDTO>> register(@Valid @RequestBody ReqRegisterDTO request)
            throws IdInvalidException {

        if (userService.existsByUsername(request.getUsername())) {
            throw new IdInvalidException("Tên đăng nhập đã được sử dụng");
        }

        if (userService.checkExistEmail(request.getEmail())) {
            throw new IdInvalidException("Email đã được sử dụng");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setCreatedDate(new Date());
        user.setRole(String.valueOf(UserRole.USER));
        userService.addOrUpdateUser(user);

        User saved = userService.getUserByUsername(request.getUsername());

        String accessToken = jwtUtil.generateToken(saved.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(saved.getUsername());

        ResLoginDTO resLogin = new ResLoginDTO(accessToken, saved.getId(), saved.getUsername(), saved.getRole());
        resLogin.setRefreshToken(refreshToken);

        RestResponse<ResLoginDTO> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Register thành công");
        res.setData(resLogin);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RestResponse<Map<String, String>>> refreshToken(
            @RequestBody Map<String, String> body) {

        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            RestResponse<Map<String, String>> res = new RestResponse<>();
            res.setStatusCode(HttpStatus.BAD_REQUEST.value());
            res.setMessage("refreshToken không được để trống");
            return ResponseEntity.badRequest().body(res);
        }

        if (!jwtUtil.validateToken(refreshToken)) {
            RestResponse<Map<String, String>> res = new RestResponse<>();
            res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            res.setMessage("Refresh Token không hợp lệ hoặc đã hết hạn");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateToken(username);

        RestResponse<Map<String, String>> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Refresh Token thành công");
        res.setData(Map.of("accessToken", newAccessToken));

        return ResponseEntity.ok(res);
    }
}
