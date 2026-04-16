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

import com.ccq.pojo.Activity;
import com.ccq.pojo.Board;
import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
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

        Root root = q.from(Board.class);
        Join<Board, Boardlist> listJoin = root.join("boardlistSet", JoinType.INNER);
        Join<Boardlist, Card> cardJoin = root.join("cardSet", JoinType.LEFT);
        q.multiselect(listJoin.get("status"),
                b.count(cardJoin.get("id")));

        q.where(b.equal(root.get("id"), id));
        q.groupBy(listJoin.get("status"));

        Query query = session.createQuery(q);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getMemberProgress(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root root = q.from(User.class);
        Join<User, Activity> activityJoin = root.join("activitySet", JoinType.INNER);
        Join<Activity, Card> cardJoin = root.join("cardId", JoinType.INNER);
        Join<Card, Boardlist> listJoin = root.join("listId", JoinType.INNER);

        q.multiselect(
                root.get("id"),
                root.get("username"),
                listJoin.get("status"),
                b.count(cardJoin.get("id"))
        );

        q.where(b.equal(listJoin.get("boardId").get("id"), id));

        q.groupBy(root.get("id"), root.get("username"), listJoin.get("status"));

        Query query = session.createQuery(q);
        return query.getResultList();
    }

}
