/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "thamgia")
@NamedQueries({
    @NamedQuery(name = "Thamgia.findAll", query = "SELECT t FROM Thamgia t"),
    @NamedQuery(name = "Thamgia.findByUserId", query = "SELECT t FROM Thamgia t WHERE t.thamgiaPK.userId = :userId"),
    @NamedQuery(name = "Thamgia.findByWorkspaceId", query = "SELECT t FROM Thamgia t WHERE t.thamgiaPK.workspaceId = :workspaceId"),
    @NamedQuery(name = "Thamgia.findByVaiTro", query = "SELECT t FROM Thamgia t WHERE t.vaiTro = :vaiTro")})
public class Thamgia implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ThamgiaPK thamgiaPK;
    @Column(name = "vaiTro")
    private String vaiTro;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "workspace_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Workspace workspace;

    public Thamgia() {
    }

    public Thamgia(ThamgiaPK thamgiaPK) {
        this.thamgiaPK = thamgiaPK;
    }

    public Thamgia(int userId, int workspaceId) {
        this.thamgiaPK = new ThamgiaPK(userId, workspaceId);
    }

    public ThamgiaPK getThamgiaPK() {
        return thamgiaPK;
    }

    public void setThamgiaPK(ThamgiaPK thamgiaPK) {
        this.thamgiaPK = thamgiaPK;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (thamgiaPK != null ? thamgiaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Thamgia)) {
            return false;
        }
        Thamgia other = (Thamgia) object;
        if ((this.thamgiaPK == null && other.thamgiaPK != null) || (this.thamgiaPK != null && !this.thamgiaPK.equals(other.thamgiaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.Thamgia[ thamgiaPK=" + thamgiaPK + " ]";
    }
    
}
