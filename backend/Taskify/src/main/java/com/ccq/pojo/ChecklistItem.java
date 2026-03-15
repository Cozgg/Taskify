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
@Table(name = "checklist_item")
@NamedQueries({
    @NamedQuery(name = "ChecklistItem.findAll", query = "SELECT c FROM ChecklistItem c"),
    @NamedQuery(name = "ChecklistItem.findById", query = "SELECT c FROM ChecklistItem c WHERE c.id = :id"),
    @NamedQuery(name = "ChecklistItem.findByName", query = "SELECT c FROM ChecklistItem c WHERE c.name = :name"),
    @NamedQuery(name = "ChecklistItem.findByIsChecked", query = "SELECT c FROM ChecklistItem c WHERE c.isChecked = :isChecked"),
    @NamedQuery(name = "ChecklistItem.findByPosition", query = "SELECT c FROM ChecklistItem c WHERE c.position = :position")})
public class ChecklistItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "is_checked")
    private Boolean isChecked;
    @Column(name = "position")
    private Integer position;
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    @ManyToOne
    private Card cardId;

    public ChecklistItem() {
    }

    public ChecklistItem(Integer id) {
        this.id = id;
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

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Card getCardId() {
        return cardId;
    }

    public void setCardId(Card cardId) {
        this.cardId = cardId;
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
        if (!(object instanceof ChecklistItem)) {
            return false;
        }
        ChecklistItem other = (ChecklistItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccq.pojo.ChecklistItem[ id=" + id + " ]";
    }
    
}
