/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "card")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Card.findAll", query = "SELECT c FROM Card c"),
    @NamedQuery(name = "Card.findById", query = "SELECT c FROM Card c WHERE c.id = :id"),
    @NamedQuery(name = "Card.findByName", query = "SELECT c FROM Card c WHERE c.name = :name"),
    @NamedQuery(name = "Card.findByDescription", query = "SELECT c FROM Card c WHERE c.description = :description"),
    @NamedQuery(name = "Card.findByCreatedDate", query = "SELECT c FROM Card c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "Card.findByIsActive", query = "SELECT c FROM Card c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "Card.findByDueDate", query = "SELECT c FROM Card c WHERE c.dueDate = :dueDate"),
    @NamedQuery(name = "Card.findByReminderDate", query = "SELECT c FROM Card c WHERE c.reminderDate = :reminderDate"),
    @NamedQuery(name = "Card.findByPosition", query = "SELECT c FROM Card c WHERE c.position = :position")})
@JsonIgnoreProperties({"checklistItemSet", "activitySet", "attachmentSet", "commentSet", "cardUserSet"})
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "reminder_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reminderDate;
    @Column(name = "position")
    private Integer position;
    @OneToMany(mappedBy = "cardId")
    private Set<ChecklistItem> checklistItemSet;
    @OneToMany(mappedBy = "cardId")
    private Set<Activity> activitySet;
    @OneToMany(mappedBy = "cardId")
    private Set<Attachment> attachmentSet;
    @OneToMany(mappedBy = "cardId")
    private Set<Comment> commentSet;
    @JoinColumn(name = "list_id", referencedColumnName = "id")
    @ManyToOne
    private Boardlist listId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardId")
    private Set<CardUser> cardUserSet;

    public Card() {
    }

    public Card(Integer id) {
        this.id = id;
    }

    public Card(Integer id, String name) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @XmlTransient
    public Set<ChecklistItem> getChecklistItemSet() {
        return checklistItemSet;
    }

    public void setChecklistItemSet(Set<ChecklistItem> checklistItemSet) {
        this.checklistItemSet = checklistItemSet;
    }

    @XmlTransient
    public Set<Activity> getActivitySet() {
        return activitySet;
    }

    public void setActivitySet(Set<Activity> activitySet) {
        this.activitySet = activitySet;
    }

    @XmlTransient
    public Set<Attachment> getAttachmentSet() {
        return attachmentSet;
    }

    public void setAttachmentSet(Set<Attachment> attachmentSet) {
        this.attachmentSet = attachmentSet;
    }

    @XmlTransient
    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    public Boardlist getListId() {
        return listId;
    }

    public void setListId(Boardlist listId) {
        this.listId = listId;
    }

    @XmlTransient
    public Set<CardUser> getCardUserSet() {
        return cardUserSet;
    }

    public void setCardUserSet(Set<CardUser> cardUserSet) {
        this.cardUserSet = cardUserSet;
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
        if (!(object instanceof Card)) {
            return false;
        }
        Card other = (Card) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccq.pojo.Card[ id=" + id + " ]";
    }
    
}
