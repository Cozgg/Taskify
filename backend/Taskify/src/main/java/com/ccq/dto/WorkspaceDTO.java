/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 *
 * @author Admin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkspaceDTO {
    private int id;
    private String name;
    private int ownerId;
    
    private List<UserDTO> members;
    private List<BoardDTO> boards;

    public WorkspaceDTO(int id, String name, int ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }
    
    public static WorkspaceDTO withMembers(int id, String name, int ownerId, List<UserDTO> members) {
        WorkspaceDTO dto = new WorkspaceDTO(id, name, ownerId);
        dto.members = members;
        return dto;
    }
    
    public static WorkspaceDTO withBoards(int id, String name, int ownerId, List<BoardDTO> boards) {
        WorkspaceDTO dto = new WorkspaceDTO(id, name, ownerId);
        dto.setBoards(boards);
        return dto;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the ownerId
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    
    
    /**
     * @return the members
     */
    public List<UserDTO> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<UserDTO> members) {
        this.members = members;
    }

    /**
     * @return the boards
     */
    public List<BoardDTO> getBoards() {
        return boards;
    }

    /**
     * @param boards the boards to set
     */
    public void setBoards(List<BoardDTO> boards) {
        this.boards = boards;
    }
    
}
