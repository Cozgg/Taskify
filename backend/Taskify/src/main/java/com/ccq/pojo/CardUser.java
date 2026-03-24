/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "card_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CardUser.findAll", query = "SELECT c FROM CardUser c"),
    @NamedQuery(name = "CardUser.findByCardId", query = "SELECT c FROM CardUser c WHERE c.cardUserPK.cardId = :cardId"),
    @NamedQuery(name = "CardUser.findByUserId", query = "SELECT c FROM CardUser c WHERE c.cardUserPK.userId = :userId"),
    @NamedQuery(name = "CardUser.findByAssignedDate", query = "SELECT c FROM CardUser c WHERE c.assignedDate = :assignedDate")})
public class CardUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CardUserPK cardUserPK;
    @Column(name = "assigned_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;
    @JoinColumn(name = "card_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Card card;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public CardUser() {
    }

    public CardUser(CardUserPK cardUserPK) {
        this.cardUserPK = cardUserPK;
    }

    public CardUser(int cardId, int userId) {
        this.cardUserPK = new CardUserPK(cardId, userId);
    }

    public CardUserPK getCardUserPK() {
        return cardUserPK;
    }

    public void setCardUserPK(CardUserPK cardUserPK) {
        this.cardUserPK = cardUserPK;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardUserPK != null ? cardUserPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CardUser)) {
            return false;
        }
        CardUser other = (CardUser) object;
        if ((this.cardUserPK == null && other.cardUserPK != null) || (this.cardUserPK != null && !this.cardUserPK.equals(other.cardUserPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ccq.pojo.CardUser[ cardUserPK=" + cardUserPK + " ]";
    }
    
}
