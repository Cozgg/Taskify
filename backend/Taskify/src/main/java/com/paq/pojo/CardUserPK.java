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
public class CardUserPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "card_id")
    private int cardId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;

    public CardUserPK() {
    }

    public CardUserPK(int cardId, int userId) {
        this.cardId = cardId;
        this.userId = userId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cardId;
        hash += (int) userId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CardUserPK)) {
            return false;
        }
        CardUserPK other = (CardUserPK) object;
        if (this.cardId != other.cardId) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.CardUserPK[ cardId=" + cardId + ", userId=" + userId + " ]";
    }
    
}
