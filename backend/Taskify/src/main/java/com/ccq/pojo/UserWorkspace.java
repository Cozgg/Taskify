/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "user_workspace")
@NamedQueries({
    @NamedQuery(name = "UserWorkspace.findAll", query = "SELECT u FROM UserWorkspace u"),
    @NamedQuery(name = "UserWorkspace.findById", query = "SELECT u FROM UserWorkspace u WHERE u.id = :id"),
    })
public class UserWorkspace implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;
    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    @ManyToOne
    private Workspace workspaceId;

    @Size(max = 20)
    @Column(name = "role_workspace")
    private String roleWorkspace;

    public UserWorkspace() {
    }

    public UserWorkspace(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Workspace getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Workspace workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getRoleWorkspace() {
        return roleWorkspace;
    }

    public void setRoleWorkspace(String roleWorkspace) {
        this.roleWorkspace = roleWorkspace;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserWorkspace)) {
            return false;
        }
        UserWorkspace other = (UserWorkspace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccq.pojo.UserWorkspace[ id=" + id + " ]";
    }
    
}
