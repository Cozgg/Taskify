/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author paqvi
 */
@Embeddable
public class ThamgiaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "workspace_id")
    private int workspaceId;

    public ThamgiaPK() {
    }

    public ThamgiaPK(int userId, int workspaceId) {
        this.userId = userId;
        this.workspaceId = workspaceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(int workspaceId) {
        this.workspaceId = workspaceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) workspaceId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThamgiaPK)) {
            return false;
        }
        ThamgiaPK other = (ThamgiaPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.workspaceId != other.workspaceId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.ThamgiaPK[ userId=" + userId + ", workspaceId=" + workspaceId + " ]";
    }
    
}
