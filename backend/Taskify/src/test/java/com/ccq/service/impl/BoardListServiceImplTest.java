package com.ccq.service.impl;

import com.ccq.pojo.Board;
import com.ccq.pojo.Boardlist;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardListServiceImplTest {

    @Mock
    private ListRepository listRepo;

    @Mock
    private BoardRepository boardRepo;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private ListServiceImpl listService;

    @Test
    void getById_existingList_requiresAccessAndReturnsList() {
        Boardlist list = new Boardlist();
        list.setId(1);

        when(listRepo.getById(1)).thenReturn(list);

        Boardlist result = listService.getById(1);

        assertSame(list, result);
        verify(permissionService).requireListAccess(1);
    }

    @Test
    void addOrUpdate_existingList_requiresListWritePermissionAndPersists() {
        Boardlist list = new Boardlist();
        list.setId(5);

        listService.addOrUpdate(list);

        verify(permissionService).requireListWritePermission(5);
        verify(listRepo).addOrUpdate(list);
    }

    @Test
    void addOrUpdate_newListInBoard_requiresBoardWritePermissionAndPersists() {
        Board board = new Board();
        board.setId(10);
        
        Boardlist list = new Boardlist();
        list.setBoardId(board);

        listService.addOrUpdate(list);

        verify(permissionService).requireBoardWritePermission(10);
        verify(listRepo).addOrUpdate(list);
    }

    @Test
    void delete_existingList_requiresWritePermissionAndDeletes() {
        listService.delete(1);

        verify(permissionService).requireListWritePermission(1);
        verify(listRepo).delete(1);
    }

    @Test
    void getList_withBoardId_requiresBoardAccessAndReturnsList() {
        Map<String, String> params = new HashMap<>();
        params.put("boardId", "15");
        
        List<Boardlist> mockList = java.util.Collections.singletonList(new Boardlist());
        when(listRepo.getList(params)).thenReturn(mockList);

        List<Boardlist> result = listService.getList(params);

        assertSame(mockList, result);
        verify(permissionService).requireBoardAccess(15);
    }

    @Test
    void createListInBoard_validInput_setsBoardAndPersists() {
        Board board = new Board();
        board.setId(10);

        Boardlist list = new Boardlist();
        list.setName("New List");

        when(boardRepo.getById(10)).thenReturn(board);

        listService.createListInBoard(10, list);

        assertEquals(board, list.getBoardId());
        verify(permissionService).requireBoardWritePermission(10);
        verify(listRepo).addOrUpdate(list);
    }

    @Test
    void createListInBoard_boardNotFound_throwsException() {
        Boardlist list = new Boardlist();
        
        when(boardRepo.getById(99)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> listService.createListInBoard(99, list));
        
        verify(permissionService).requireBoardWritePermission(99);
        verify(listRepo, never()).addOrUpdate(any());
    }
}
