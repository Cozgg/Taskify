/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccq.pojo.Board;
import com.ccq.pojo.Workspace;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.BoardService;

/**
 *
 * @author nguye
 */
@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private WorkspaceRepository wsRepo;

    @Override
    public Board getById(int id) {
        return this.boardRepo.getById(id);
    }

    @Override
    public void addOrUpdate(Board b) {
        this.boardRepo.addOrUpdate(b);
    }

    @Override
    public void delete(int id) {
        this.boardRepo.delete(id);
    }

    @Override
    public List<Board> getBoards(Map<String, String> params) {
        return this.boardRepo.getBoards(params);
    }

    @Override
    public void createBoardInWorkspace(int workspaceId, Board board) {
        Workspace ws = this.wsRepo.getWorkspaceById(workspaceId);
        if (ws == null) {
            throw new RuntimeException("Không tìm thấy Workspace" + workspaceId);
        }

        board.setWorkspaceId(ws);

        this.boardRepo.addOrUpdate(board);
    }

}
