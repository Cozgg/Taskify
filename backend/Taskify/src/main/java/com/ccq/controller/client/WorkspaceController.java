package com.ccq.controller.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
import com.ccq.pojo.response.ResBoardDTO;
import com.ccq.pojo.response.ResUserDTO;
import com.ccq.pojo.response.ResUserWorkspaceDTO;
import com.ccq.pojo.response.ResWorkspaceDTO;
import com.ccq.pojo.response.ResWorkspacePageDTO;
import com.ccq.pojo.response.RestResponse;
import com.ccq.service.UserService;
import com.ccq.service.WorkspaceService;
import com.ccq.utils.DTOMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @GetMapping("/workspace/owner/{ownerId}")
    public ResponseEntity<RestResponse<ResWorkspacePageDTO>> getWorkspaceByOwner(
            @PathVariable("ownerId") int ownerId,
            @RequestParam(required = false) Map<String, String> params) {

        List<Workspace> workspaces = this.workspaceService.getWorkspacesByOwnerId(ownerId, params);

        Long totalItems = this.workspaceService.countWorkspacesByOwnerId(ownerId);

        int page = 1;
        int pageSize = Integer.parseInt(this.env.getProperty("workspace.page_size", "10"));
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
            pageSize = Integer.parseInt(this.env.getProperty("workspace.page_size", "10"));
        }

        ResWorkspacePageDTO pageDto = DTOMapper.toWorkspacePageDTO(workspaces, totalItems, page, pageSize);

        RestResponse<ResWorkspacePageDTO> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setData(pageDto);
        return ResponseEntity.ok(res);
    }

    //da test, chua check quyen
    @GetMapping("/workspace/{id}")
    public ResponseEntity<RestResponse<ResWorkspaceDTO>> getWorkspaceById(@PathVariable("id") int id) {
        Workspace workspace = this.workspaceService.getWorkspaceById(id);
        if (workspace == null) {
            RestResponse<ResWorkspaceDTO> err = new RestResponse<>();
            err.setStatusCode(HttpStatus.NOT_FOUND.value());
            err.setError("Không tìm thấy workspace");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
        ResWorkspaceDTO dto = DTOMapper.toWorkspaceDTO(workspace);
        RestResponse<ResWorkspaceDTO> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setData(dto);
        return ResponseEntity.ok(res);
    }

    //da test, chua check quyen
    @GetMapping("/workspace/{id}/members")
    public ResponseEntity<?> getMembers(@PathVariable("id") int id) {
        Workspace workspace = this.workspaceService.getWorkspaceById(id);
        if (workspace == null) {
            return new ResponseEntity<>("Không tìm thấy workspace", HttpStatus.NOT_FOUND);
        }
        List<User> members = this.workspaceService.getMembersByWorkspaceId(id);
        List<ResUserDTO> response = members.stream().map(DTOMapper::toUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/workspace/{id}/boards")
    public ResponseEntity<?> getBoardsInWorkspace(@PathVariable("id") int id) {
        Workspace workspace = this.workspaceService.getWorkspaceById(id);
        if (workspace == null) {
            return new ResponseEntity<>("Không tìm thấy workspace", HttpStatus.NOT_FOUND);
        }
        List<Board> boards = this.workspaceService.getBoardsByWorkspaceId(id);
        List<ResBoardDTO> response = boards.stream().map(DTOMapper::toBoardDTO).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/workspace/{id}")
    public ResponseEntity<RestResponse<ResWorkspaceDTO>> updateWorkspace(
            @PathVariable("id") int id,
            @Valid @RequestBody Workspace workspaceReq) {
        Workspace existing = this.workspaceService.getWorkspaceById(id);
        if (existing == null) {
            RestResponse<ResWorkspaceDTO> err = new RestResponse<>();
            err.setStatusCode(HttpStatus.NOT_FOUND.value());
            err.setError("Không tìm thấy workspace");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
        if (workspaceReq.getName() != null) {
            existing.setName(workspaceReq.getName());
        }

        this.workspaceService.addOrUpdate(existing);
        ResWorkspaceDTO dto = DTOMapper.toWorkspaceDTO(existing);
        RestResponse<ResWorkspaceDTO> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setData(dto);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/workspace")
    public ResponseEntity<RestResponse<ResWorkspaceDTO>> createWorkspace(
            @Valid @RequestBody Workspace workspaceReq) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            com.ccq.pojo.User owner = userService.getUserByUsername(currentUsername);
            if (owner == null) {
                RestResponse<ResWorkspaceDTO> err = new RestResponse<>();
                err.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                err.setError("Không xác định được người dùng");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
            }
            workspaceReq.setOwnerId(owner);
            workspaceReq.setId(null);
            this.workspaceService.addOrUpdate(workspaceReq);
            ResWorkspaceDTO dto = DTOMapper.toWorkspaceDTO(workspaceReq);
            RestResponse<ResWorkspaceDTO> res = new RestResponse<>();
            res.setStatusCode(HttpStatus.CREATED.value());
            res.setData(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            RestResponse<ResWorkspaceDTO> err = new RestResponse<>();
            err.setStatusCode(HttpStatus.BAD_REQUEST.value());
            err.setError("Lỗi tạo workspace: " + e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // Xóa workspace theo id
    @DeleteMapping("/workspace/{id}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable("id") int id) {
        try {
            this.workspaceService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            RestResponse<ResWorkspaceDTO> err = new RestResponse<>();
            err.setStatusCode(HttpStatus.BAD_REQUEST.value());
            err.setError("Lỗi xóa workspace: " + e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @PostMapping("/workspaces/{workspaceId}/users")
    public ResponseEntity<ResUserWorkspaceDTO> inviteUser(@PathVariable("workspaceId") int workspaceId, @RequestBody Map<String, String> params) {
        User u = this.userService.getUserById(Integer.parseInt(params.get("userId")));
        UserWorkspace uw = this.workspaceService.addUserIntoWorkspace(workspaceId, u.getId());
        ResUserWorkspaceDTO dto = DTOMapper.toUserWorkspaceDTO(uw);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
