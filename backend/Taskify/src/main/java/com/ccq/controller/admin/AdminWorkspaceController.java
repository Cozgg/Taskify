package com.ccq.controller.admin;

import java.util.HashMap;
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

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.service.WorkspaceService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/admin/workspaces")
public class AdminWorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping
    public ResponseEntity<List<Workspace>> getAllWorkspaces(@RequestParam Map<String, String> params) {
        List<Workspace> workspaces = this.workspaceService.getWorkspaces(params);
        return new ResponseEntity<>(workspaces, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getWorkspaceDetail(@PathVariable("id") int id) {
        try {
            Workspace workspace = this.workspaceService.getWorkspaceById(id);
            if (workspace == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<User> members = this.workspaceService.getMembersByWorkspaceId(id);
            List<Board> boards = this.workspaceService.getBoardsByWorkspaceId(id);

            Map<String, Object> detail = new HashMap<>();
            detail.put("workspace", workspace);
            detail.put("totalMembers", members.size());
            detail.put("members", members);
            detail.put("totalBoards", boards.size());
            detail.put("boards", boards);

            return new ResponseEntity<>(detail, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = Map.of("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getMembersByWorkspace(@PathVariable("id") int id) {
        try {
            Workspace workspace = this.workspaceService.getWorkspaceById(id);
            if (workspace == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<User> members = this.workspaceService.getMembersByWorkspaceId(id);
            return new ResponseEntity<>(members, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> addWorkspace(@Valid @RequestBody Workspace workspace) {
        try {
            workspace.setId(null);
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
