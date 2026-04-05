/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.Activity;
import com.ccq.pojo.Card;
import com.ccq.pojo.CardUser;
import com.ccq.pojo.Workspace;
import com.ccq.repository.CardRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import com.ccq.repository.CardRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class CardRepositoryImpl implements CardRepository{
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
        if(s != null){
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

            // Filter theo listId
            String listId = params.get("listId");
            if (listId != null && !listId.isEmpty()) {
                predicates.add(b.equal(root.get("boardList").get("id"), Integer.parseInt(listId)));
            }

            // Filter theo keyword tên card
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
}
