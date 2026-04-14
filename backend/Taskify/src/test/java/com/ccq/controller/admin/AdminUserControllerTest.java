package com.ccq.controller.admin;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ccq.pojo.User;
import com.ccq.pojo.request.ReqAdminUserDTO;
import com.ccq.pojo.response.ResUserPageDTO;
import com.ccq.pojo.response.RestResponse;
import com.ccq.service.UserService;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Environment env;

    @InjectMocks
    private AdminUserController controller;

    @Test
    void getUsers_mapsEntitiesToDtos() {
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setEmail("admin@test.com");
        user.setRole("ADMIN");

        when(userService.getUsers(any())).thenReturn(List.of(user));
        when(userService.countUsers(any())).thenReturn(1L);
        when(env.getProperty("user.page_size", "10")).thenReturn("10");

        ResponseEntity<RestResponse<ResUserPageDTO>> response = controller.getUsers(Map.of());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        RestResponse<ResUserPageDTO> body = response.getBody();
        assertNotNull(body);
        ResUserPageDTO page = body.getData();
        assertNotNull(page);
        assertNotNull(page.getItems());
        assertEquals(1, page.getItems().size());
        assertEquals("admin", page.getItems().get(0).getUsername());
    }

    @Test
    void addOrUpdateUser_withoutRole_defaultsToUser() {
        ReqAdminUserDTO dto = new ReqAdminUserDTO();
        dto.setUsername("quyendz");
        dto.setPassword("123456");
        dto.setEmail("quyendz@test.com");
        dto.setAvatar("avatar.png");

        ResponseEntity<?> response = controller.addOrUpdateUser(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).addOrUpdateUser(captor.capture());
        User saved = captor.getValue();
        assertEquals("quyendz", saved.getUsername());
        assertEquals("USER", saved.getRole());
    }

    @Test
    void deleteUser_whenUserMissing_returnsNotFound() {
        when(userService.getUserById(44)).thenReturn(null);

        ResponseEntity<Void> response = controller.deleteUser(44);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
