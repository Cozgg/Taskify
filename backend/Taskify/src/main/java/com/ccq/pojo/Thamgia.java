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
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "thamgia")
@NamedQueries({
    @NamedQuery(name = "Thamgia.findAll", query = "SELECT t FROM Thamgia t"),
    @NamedQuery(name = "Thamgia.findById", query = "SELECT t FROM Thamgia t WHERE t.id = :id"),
    @NamedQuery(name = "Thamgia.findByVaiTro", query = "SELECT t FROM Thamgia t WHERE t.vaiTro = :vaiTro"),
    @NamedQuery(name = "Thamgia.findByWorkspaceIdFk", query = "SELECT t FROM Thamgia t WHERE t.workspaceIdFk = :workspaceIdFk")})
public class Thamgia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "vaiTro")
    private String vaiTro;
    @Column(name = "workspace_id_fk")
    private Integer workspaceIdFk;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;
    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    @ManyToOne
    private Workspace workspaceId;

    public Thamgia() {
    }

    public Thamgia(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public Integer getWorkspaceIdFk() {
        return workspaceIdFk;
    }

    public void setWorkspaceIdFk(Integer workspaceIdFk) {
        this.workspaceIdFk = workspaceIdFk;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Thamgia)) {
            return false;
        }
        Thamgia other = (Thamgia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccq.pojo.Thamgia[ id=" + id + " ]";
    }
    
}
