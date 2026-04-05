package com.ccq.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.ccq.pojo.request.ReqAdminWorkspaceDTO;
import com.ccq.pojo.response.ResBoardDTO;
import com.ccq.pojo.response.ResUserDTO;
import com.ccq.pojo.response.ResWorkspaceDTO;
import com.ccq.service.WorkspaceService;
import com.ccq.utils.DTOMapper;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/admin/workspaces")
public class AdminWorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping
    public ResponseEntity<List<ResWorkspaceDTO>> getAllWorkspaces(@RequestParam Map<String, String> params) {
        List<ResWorkspaceDTO> workspaces = this.workspaceService.getWorkspaces(params)
                .stream()
                .map(DTOMapper::toWorkspaceDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(workspaces, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkspaceDetail(@PathVariable("id") int id) {
        try {
            Workspace workspace = this.workspaceService.getWorkspaceById(id);
            if (workspace == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<ResUserDTO> members = this.workspaceService.getMembersByWorkspaceId(id)
                    .stream()
                    .map(DTOMapper::toUserDTO)
                    .collect(Collectors.toList());

            List<ResBoardDTO> boards = this.workspaceService.getBoardsByWorkspaceId(id)
                    .stream()
                    .map(DTOMapper::toBoardDTO)
                    .collect(Collectors.toList());

            Map<String, Object> detail = new HashMap<>();
            detail.put("workspace", DTOMapper.toWorkspaceDTO(workspace));
            detail.put("totalMembers", members.size());
            detail.put("members", members);
            detail.put("totalBoards", boards.size());
            detail.put("boards", boards);

            return new ResponseEntity<>(detail, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getMembersByWorkspace(@PathVariable("id") int id) {
        try {
            Workspace workspace = this.workspaceService.getWorkspaceById(id);
            if (workspace == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<ResUserDTO> members = this.workspaceService.getMembersByWorkspaceId(id)
                    .stream()
                    .map(DTOMapper::toUserDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(members, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> addWorkspace(@Valid @RequestBody ReqAdminWorkspaceDTO dto) {
        try {
            Workspace workspace = new Workspace();
            workspace.setName(dto.getName());
            if (dto.getOwnerId() != null) {
                User owner = new User();
                owner.setId(dto.getOwnerId());
                workspace.setOwnerId(owner);
            }
            this.workspaceService.addOrUpdate(workspace);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable("id") int id) {
        try {
            this.workspaceService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
