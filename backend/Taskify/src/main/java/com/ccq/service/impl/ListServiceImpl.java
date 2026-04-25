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
import com.ccq.pojo.Boardlist;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.service.ListService;
import com.ccq.service.PermissionService;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguye
 */
@Service
@Transactional
public class ListServiceImpl implements ListService{

    @Autowired
    private ListRepository listRepo;
    
    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private PermissionService permissionService;
    
    @Override
    public Boardlist getById(int id) {
        Boardlist list = this.listRepo.getById(id);
        if (list != null) {
            permissionService.requireListAccess(id);
        }
        return list;
    }

    @Override
    public void addOrUpdate(Boardlist l) {
        if (l.getId() != null) {
            permissionService.requireListWritePermission(l.getId());
        } else if (l.getBoardId() != null && l.getBoardId().getId() != null) {
            permissionService.requireBoardWritePermission(l.getBoardId().getId());
        }
        this.listRepo.addOrUpdate(l);
    }

    @Override
    public void delete(int id) {
        permissionService.requireListWritePermission(id);
        this.listRepo.delete(id);
    }

    @Override
    public List<Boardlist> getList(Map<String, String> params) {
        if (params != null && params.containsKey("boardId")) {
            permissionService.requireBoardAccess(Integer.parseInt(params.get("boardId")));
        }
        return this.listRepo.getList(params);
    }

    @Override
    public void createListInBoard(int boardId, Boardlist list) {
        permissionService.requireBoardWritePermission(boardId);
        Board board = this.boardRepo.getById(boardId);

        if(board == null){
            throw new RuntimeException("Không tìm thấy bảng với id: " + boardId);
        }

        list.setBoardId(board);
        this.listRepo.addOrUpdate(list);
    }

}
