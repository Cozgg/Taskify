/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccq.pojo.Card;
import com.ccq.pojo.CardUser;
import com.ccq.pojo.User;
import com.ccq.repository.CardRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author Admin
 */
@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class CardRepositoryImpl implements CardRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Card getById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Card.class, id);
    }

    @Override
    public void addOrUpdate(Card c) {
        Session s = this.factory.getObject().getCurrentSession();
        if (c.getId() != null) {
            s.merge(c);
        } else {
            s.persist(c);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Card c = this.getById(id);
        if (s != null) {
            s.remove(c);
        }
    }

    @Override
    public List<Card> getCard(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Card> q = b.createQuery(Card.class);
        Root<Card> root = q.from(Card.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String listId = params.get("listId");
            if (listId != null && !listId.isEmpty()) {
                predicates.add(b.equal(root.get("listId").get("id"), Integer.parseInt(listId)));
            }

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }
        }

        q.orderBy(b.desc(root.get("id")));
        Query<Card> query = s.createQuery(q);

        if (params != null) {
            String pageStr = params.get("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                int pageSize = this.env.getProperty("workspace.page_size", Integer.class);
                int page = Integer.parseInt(params.getOrDefault("page", "1"));
                int start = (page - 1) * pageSize;

                query.setMaxResults(pageSize);
                query.setFirstResult(start);
            }
        }

        return query.getResultList();
    }

    @Override
    public Card findCardById(int cardId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Card.class, cardId);
    }

    @Override
    public void assignUserForCard(CardUser ac) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(ac);
    }

    @Override
    public boolean isUserInCard(int userId, int cardId) {
        Session s = this.factory.getObject().getCurrentSession();
        String sql = "from CardUser cu " + "where cu.userId.id = :userId and cu.cardId.id = :cardId";
        Query q = s.createQuery(sql, CardUser.class);
        q.setParameter("userId", userId);
        q.setParameter("cardId", cardId);
        return q.uniqueResult() != null;
    }

    // @Override
    // public boolean isWorkspaceAdminOfThisCard(int cardId, String username) {
    //     Session s = this.factory.getObject().getCurrentSession();
    //     String sql = "select count(c.id) from Card c " + "where c.id = :cardId " + "and c.listId.boardId.workspaceId.ownerId.username = :username";
    //     Query<Long> q = s.createQuery(sql, Long.class);
    //     return q.getSingleResult() > 0;
    // }
    @Override
    public List<User> getMemberInCard(int cardId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root<CardUser> root = q.from(CardUser.class);
        Join<CardUser, User> join = root.join("userId", JoinType.INNER);
        q.select(join);

        q.where(b.equal(root.get("cardId").get("id"), cardId));
        Query<User> query = s.createQuery(q);
        return query.getResultList();
    }

    @Override
    public void removeUserInCard(int userId, int cardId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<CardUser> q = b.createQuery(CardUser.class);

        Root<CardUser> root = q.from(CardUser.class);

        q.select(root).where(
                b.and(
                        b.equal(root.get("userId").get("id"), userId),
                        b.equal(root.get("cardId").get("id"), cardId)
                )
        );

        CardUser cu = s.createQuery(q).uniqueResult();

        if (cu != null) {
            s.remove(cu);
        }
    }
}
