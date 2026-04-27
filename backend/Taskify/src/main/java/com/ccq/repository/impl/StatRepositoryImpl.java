/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccq.pojo.Board;
import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
import com.ccq.pojo.CardUser;
import com.ccq.pojo.User;
import com.ccq.repository.StatRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author Admin
 */
@Repository
@Transactional
public class StatRepositoryImpl implements StatRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> getBoardProgressStats(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Boardlist> root = q.from(Boardlist.class);
        Join<Boardlist, Card> cardJoin = root.join("cardSet", JoinType.LEFT);
        q.multiselect(
                root.get("name"),
                b.count(cardJoin.get("id"))
        );
        q.where(
                b.equal(root.get("boardId").get("id"), id)
        );
        q.groupBy(
                root.get("id"),
                root.get("name")
        );
        q.orderBy(b.asc(root.get("position")));
        return session.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> getMemberProgress(int boardId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<CardUser> root = q.from(CardUser.class);

        Join<CardUser, User> userJoin = root.join("userId", JoinType.INNER);
        Join<CardUser, Card> cardJoin = root.join("cardId", JoinType.INNER);

        Join<Card, Boardlist> listJoin = cardJoin.join("listId", JoinType.INNER);

        q.multiselect(
                userJoin.get("username"),
                listJoin.get("name"),
                b.count(cardJoin.get("id"))
        );

        q.where(b.equal(listJoin.get("boardId").get("id"), boardId));

        q.groupBy(
                userJoin.get("id"),
                userJoin.get("username"),
                listJoin.get("name")
        );

        Query<Object[]> query = session.createQuery(q);
        return query.getResultList();
    }

}
