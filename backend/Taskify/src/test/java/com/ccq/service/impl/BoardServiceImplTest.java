package com.ccq.service.impl;

import com.ccq.pojo.Board;
import com.ccq.pojo.Workspace;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @Mock
    private BoardRepository boardRepo;

    @Mock
    private WorkspaceRepository wsRepo;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private BoardServiceImpl boardService;

    @Test
    void getById_existingBoard_requiresAccessAndReturnsBoard() {
        Board board = new Board();
        board.setId(1);

        when(boardRepo.getById(1)).thenReturn(board);

        Board result = boardService.getById(1);

        assertSame(board, result);
        verify(permissionService).requireBoardAccess(1);
    }

    @Test
    void addOrUpdate_nullBoard_throwsException() {
        assertThrows(ResponseStatusException.class, () -> boardService.addOrUpdate(null));
    }

    @Test
    void addOrUpdate_newBoardInWorkspace_requiresWorkspaceAccessAndPersists() {
        Workspace ws = new Workspace();
        ws.setId(10);

        Board board = new Board();
        board.setWorkspaceId(ws);

        boardService.addOrUpdate(board);

        verify(permissionService).requireWorkspaceAccess(10);
        verify(boardRepo).addOrUpdate(board);
    }

    @Test
    void delete_nonExistingBoard_throwsNotFound() {
        when(boardRepo.getById(99)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> boardService.delete(99));
    }

    @Test
    void createBoardInWorkspace_validInput_setsWorkspaceAndPersists() {
        Workspace ws = new Workspace();
        ws.setId(1);
        
        Board board = new Board();
        board.setName("New Board");

        when(wsRepo.getWorkspaceById(1)).thenReturn(ws);

        Board result = boardService.createBoardInWorkspace(1, board);

        assertSame(ws, result.getWorkspaceId());
        verify(permissionService).requireWorkspaceAccess(1);
        verify(boardRepo).addOrUpdate(board);
    }
    @Test
    void delete_existingBoard_requiresPermissionAndDeletes() {
        Board board = new Board();
        board.setId(1);
        Workspace ws = new Workspace();
        board.setWorkspaceId(ws);

        when(boardRepo.getById(1)).thenReturn(board);

        boardService.delete(1);

        verify(permissionService).requireBoardDeletePermission(1);
        verify(boardRepo).delete(1);
    }

    @Test
    void getBoards_withWorkspaceId_requiresAccessAndReturnsList() {
        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("workspaceId", "15");
        
        java.util.List<Board> mockList = java.util.Collections.singletonList(new Board());
        when(boardRepo.getBoards(params)).thenReturn(mockList);

        java.util.List<Board> result = boardService.getBoards(params);

        assertSame(mockList, result);
        verify(permissionService).requireWorkspaceAccess(15);
    }

    @Test
    void countBoards_withWorkspaceId_requiresAccessAndReturnsCount() {
        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("workspaceId", "15");
        
        when(boardRepo.countBoards(params)).thenReturn(5L);

        Long result = boardService.countBoards(params);

        assertEquals(5L, result);
        verify(permissionService).requireWorkspaceAccess(15);
    }

    @Test
    void getBoardDTOById_existingBoard_returnsDTO() {
        Board board = new Board();
        board.setId(1);
        board.setName("Test");

        when(boardRepo.getById(1)).thenReturn(board);

        com.ccq.pojo.response.ResBoardDTO result = boardService.getBoardDTOById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
    }
}
