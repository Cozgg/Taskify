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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "workspace")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Workspace.findAll", query = "SELECT w FROM Workspace w"),
    @NamedQuery(name = "Workspace.findById", query = "SELECT w FROM Workspace w WHERE w.id = :id"),
    @NamedQuery(name = "Workspace.findByName", query = "SELECT w FROM Workspace w WHERE w.name = :name")})
public class Workspace implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne
    private User ownerId;
    @JsonIgnore
    @OneToMany(mappedBy = "workspaceId")
    private Set<Board> boardSet;
    @JsonIgnore
    @OneToMany(mappedBy = "workspaceId")
    private Set<UserWorkspace> userWorkspaceSet;

    public Workspace() {
    }

    public Workspace(Integer id) {
        this.id = id;
    }

    public Workspace(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public User getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    @XmlTransient
    @JsonIgnore
    public Set<Board> getBoardSet() {
        return boardSet;
    }

    public void setBoardSet(Set<Board> boardSet) {
        this.boardSet = boardSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<UserWorkspace> getUserWorkspaceSet() {
        return userWorkspaceSet;
    }

    public void setUserWorkspaceSet(Set<UserWorkspace> userWorkspaceSet) {
        this.userWorkspaceSet = userWorkspaceSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Workspace)) {
            return false;
        }
        Workspace other = (Workspace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccq.pojo.Workspace[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
