package com.ccq.controller.client;

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

import com.ccq.pojo.response.ResUserDTO;
import com.ccq.pojo.response.ResWorkspaceDTO;
import com.ccq.pojo.response.ResBoardDTO;
import com.ccq.pojo.response.RestResponse;
import com.ccq.utils.DTOMapper;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@RestController
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/workspace/owner/{ownerId}")
    public ResponseEntity<RestResponse<ResWorkspaceDTO>> getWorkspaceByOwner(@PathVariable("ownerId") int ownerId) {
        Workspace workspace = this.workspaceService.getWorkspaceByOwnerId(ownerId);
        if (workspace == null) {
            RestResponse<ResWorkspaceDTO> err = new RestResponse<>();
            err.setStatusCode(HttpStatus.NOT_FOUND.value());
            err.setError("Người dùng chưa có workspace");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
        ResWorkspaceDTO dto = DTOMapper.toWorkspaceDTO(workspace);
        RestResponse<ResWorkspaceDTO> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setData(dto);
        return ResponseEntity.ok(res);
    }

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
        // Cập nhật trường name từ request sang bản ghi hiện tại trong DB
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
}
