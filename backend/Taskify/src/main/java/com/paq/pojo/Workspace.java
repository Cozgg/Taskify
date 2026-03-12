/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "workspace")
@NamedQueries({
    @NamedQuery(name = "Workspace.findAll", query = "SELECT w FROM Workspace w"),
    @NamedQuery(name = "Workspace.findById", query = "SELECT w FROM Workspace w WHERE w.id = :id"),
    @NamedQuery(name = "Workspace.findByName", query = "SELECT w FROM Workspace w WHERE w.name = :name")})
public class Workspace implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne
    private User ownerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workspace")
    private Set<Thamgia> thamgiaSet;
    @OneToMany(mappedBy = "workspaceId")
    private Set<Board> boardSet;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Thamgia> getThamgiaSet() {
        return thamgiaSet;
    }

    public void setThamgiaSet(Set<Thamgia> thamgiaSet) {
        this.thamgiaSet = thamgiaSet;
    }

    public Set<Board> getBoardSet() {
        return boardSet;
    }

    public void setBoardSet(Set<Board> boardSet) {
        this.boardSet = boardSet;
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
        return "com.paq.pojo.Workspace[ id=" + id + " ]";
    }
    
}
