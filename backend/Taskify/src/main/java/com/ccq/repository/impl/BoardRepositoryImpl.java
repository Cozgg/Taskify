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

import com.ccq.pojo.Board;
import com.ccq.repository.BoardRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author nguye
 */
@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class BoardRepositoryImpl implements BoardRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Board getById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Board.class, id);
    }

    @Override
    public void addOrUpdate(Board w) {
        Session s = this.factory.getObject().getCurrentSession();
        if (w.getId() != null) {
            s.merge(w);
        } else {
            s.persist(w);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Board w = this.getById(id);
        if (w != null) {
            s.remove(w);
        }
    }

    @Override
    public List<Board> getBoards(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Board> q = b.createQuery(Board.class);
        Root<Board> root = q.from(Board.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String workspaceId = params.get("workspaceId");
            if (workspaceId != null && !workspaceId.isEmpty()) {
                predicates.add(b.equal(root.get("workspaceId").get("id"), Integer.parseInt(workspaceId)));
            }

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }

        }

        q.orderBy(b.asc(root.get("id")));

        Query<Board> query = s.createQuery(q);

        if (params != null) {
            String pageStr = params.get("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                int pageSize = this.env.getProperty("workspace.page_size", Integer.class);
                int page = Integer.parseInt(pageStr);
                int start = (page - 1) * pageSize;

                query.setMaxResults(pageSize);
                query.setFirstResult(start);
            }
        }

        return query.getResultList();
    }

    @Override
    public Long count() {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Long> q = s.createQuery("select count(*) from Board", Long.class);
        return q.uniqueResult();
    }

}
