package com.ccq.controller.client;

import com.ccq.dto.BoardDTO;
import com.ccq.dto.UserDTO;
import com.ccq.dto.WorkspaceDTO;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.service.WorkspaceService;

import jakarta.validation.Valid;
import java.util.ArrayList;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;
    
    @GetMapping("/workspace/owner/{ownerId}")
    public ResponseEntity getWorkspaceByOwner(@PathVariable("ownerId") int ownerId) {
        try {
            Workspace workspace = this.workspaceService.getWorkspaceByOwnerId(ownerId);
            if (workspace == null) {
                return new ResponseEntity<>("Người dùng chưa có workspace", HttpStatus.NOT_FOUND);
            }
            WorkspaceDTO dto = new WorkspaceDTO(workspace.getId(), workspace.getName(), workspace.getOwnerId().getId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //da test, chua check quyen
    @GetMapping("/workspace/{id}")
    public ResponseEntity<?> getWorkspaceById(@PathVariable("id") int id) {
        try {
            Workspace workspace = this.workspaceService.getWorkspaceById(id);
            if (workspace == null) {
                return new ResponseEntity<>("Không tìm thấy workspace", HttpStatus.NOT_FOUND);
            }
            WorkspaceDTO dto = new WorkspaceDTO(workspace.getId(), workspace.getName(), workspace.getOwnerId().getId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    //da test, chua check quyen
    @GetMapping("/workspace/{id}/members")
    public ResponseEntity<?> getMembers(@PathVariable("id") int id) {
        try {
            Workspace workspace = this.workspaceService.getWorkspaceById(id);
            if (workspace == null) {
                return new ResponseEntity<>("Không tìm thấy workspace", HttpStatus.NOT_FOUND);
            }
            List<User> members = this.workspaceService.getMembersByWorkspaceId(id);
            List<UserDTO> dtoMembers = new ArrayList<>();
            for(var m : members){
                UserDTO udto = new UserDTO(m.getId(), m.getUsername(), m.getEmail());
                dtoMembers.add(udto);
            }
            WorkspaceDTO workspaceDto = WorkspaceDTO.withMembers(workspace.getId(), workspace.getName(), workspace.getOwnerId().getId(), dtoMembers);
            return new ResponseEntity<>(workspaceDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/workspace/{id}/boards")
    public ResponseEntity<?> getBoardsInWorkspace(@PathVariable("id") int id) {
        try {
            Workspace workspace = this.workspaceService.getWorkspaceById(id);
            if (workspace == null) {
                return new ResponseEntity<>("Không tìm thấy workspace", HttpStatus.NOT_FOUND);
            }
            List<Board> boards = this.workspaceService.getBoardsByWorkspaceId(id);
            List<BoardDTO> dtoBoards = new ArrayList<>();
            for(var b : boards){
                BoardDTO bdto = new BoardDTO(b.getId(), b.getName(), b.getCreatedDate(), b.getIsPublic());
                dtoBoards.add(bdto);
            }
            WorkspaceDTO workspaceDto = WorkspaceDTO.withBoards(workspace.getId(), workspace.getName(), workspace.getOwnerId().getId(), dtoBoards);
            return new ResponseEntity<>(workspaceDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/workspace/{id}")
    public ResponseEntity<?> updateWorkspace(
            @PathVariable("id") int id,
            @Valid @RequestBody Workspace workspace) {
        try {
            Workspace existing = this.workspaceService.getWorkspaceById(id);
            if (existing == null) {
                return new ResponseEntity<>("Không tìm thấy workspace", HttpStatus.NOT_FOUND);
            }
            workspace.setId(id);
            this.workspaceService.addOrUpdate(workspace);
            return new ResponseEntity<>(workspace, HttpStatus.OK);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); // 403
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/workspace/{id}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable("id") int id) {
        try {
            this.workspaceService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); // 403
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
