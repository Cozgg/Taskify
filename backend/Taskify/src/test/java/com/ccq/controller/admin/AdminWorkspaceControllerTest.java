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

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.pojo.request.ReqAdminWorkspaceDTO;
import com.ccq.pojo.response.ResWorkspacePageDTO;
import com.ccq.pojo.response.RestResponse;
import com.ccq.service.WorkspaceService;

@ExtendWith(MockitoExtension.class)
class AdminWorkspaceControllerTest {

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private Environment env;

    @InjectMocks
    private AdminWorkspaceController controller;

    @Test
    void getWorkspaceDetail_whenWorkspaceMissing_returnsNotFound() {
        when(workspaceService.getWorkspaceById(99)).thenReturn(null);

        ResponseEntity<?> response = controller.getWorkspaceDetail(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getWorkspaceDetail_whenWorkspaceExists_returnsDetailPayload() {
        User owner = new User();
        owner.setId(1);
        owner.setUsername("quyendz");
        owner.setEmail("quyendz@test.com");
        owner.setRole("USER");

        Workspace workspace = new Workspace();
        workspace.setId(8);
        workspace.setName("Main");
        workspace.setOwnerId(owner);

        User member = new User();
        member.setId(2);
        member.setUsername("member");
        member.setEmail("member@test.com");
        member.setRole("USER");

        Board board = new Board();
        board.setId(5);
        board.setName("Board A");

        when(workspaceService.getWorkspaceById(8)).thenReturn(workspace);
        when(workspaceService.getMembersByWorkspaceId(8)).thenReturn(List.of(member));
        when(workspaceService.getBoardsByWorkspaceId(8)).thenReturn(List.of(board));

        ResponseEntity<?> response = controller.getWorkspaceDetail(8);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<?, ?> payload = (Map<?, ?>) response.getBody();
        assertNotNull(payload);
        assertEquals(1, payload.get("totalMembers"));
        assertEquals(1, payload.get("totalBoards"));
    }

    @Test
    void addWorkspace_buildsWorkspaceAndDelegatesToService() {
        ReqAdminWorkspaceDTO dto = new ReqAdminWorkspaceDTO();
        dto.setName("Admin Workspace");
        dto.setOwnerId(11);

        ResponseEntity<?> response = controller.addWorkspace(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ArgumentCaptor<Workspace> captor = ArgumentCaptor.forClass(Workspace.class);
        verify(workspaceService).addOrUpdate(captor.capture());
        Workspace saved = captor.getValue();
        assertEquals("Admin Workspace", saved.getName());
        assertNotNull(saved.getOwnerId());
        assertEquals(11, saved.getOwnerId().getId());
    }

    @Test
    void getAllWorkspaces_mapsResultToDtoList() {
        Workspace workspace = new Workspace();
        workspace.setId(4);
        workspace.setName("WS");

        when(workspaceService.getWorkspaces(any())).thenReturn(List.of(workspace));

        when(workspaceService.countWorkspaces(any())).thenReturn(1L);
        when(env.getProperty("workspace.page_size", "10")).thenReturn("10");

        ResponseEntity<RestResponse<ResWorkspacePageDTO>> response = controller.getAllWorkspaces(Map.of());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        RestResponse<ResWorkspacePageDTO> body = response.getBody();
        assertNotNull(body);

        ResWorkspacePageDTO page = body.getData();
        assertNotNull(page);
        assertNotNull(page.getItems());
        assertEquals(1, page.getItems().size());
        assertEquals("WS", page.getItems().get(0).getName());
    }
}
