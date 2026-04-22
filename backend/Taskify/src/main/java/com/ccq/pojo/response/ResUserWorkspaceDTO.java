/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.pojo.response;

import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;

/**
 *
 * @author Admin
 */
public class ResUserWorkspaceDTO {
    private ResUserDTO user;
    private ResWorkspaceDTO workspace;

    public ResUserWorkspaceDTO(ResUserDTO user, ResWorkspaceDTO workspace) {
        this.user = user;
        this.workspace = workspace;
    }

    /**
     * @return the user
     */
    public ResUserDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(ResUserDTO user) {
        this.user = user;
    }

    /**
     * @return the workspace
     */
    public ResWorkspaceDTO getWorkspace() {
        return workspace;
    }

    /**
     * @param workspace the workspace to set
     */
    public void setWorkspace(ResWorkspaceDTO workspace) {
        this.workspace = workspace;
    }
    
    
}
