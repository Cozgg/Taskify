/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccq.pojo.User;
import com.ccq.repository.UserRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author Admin
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session currentSession() {
        return Objects.requireNonNull(this.factory.getObject(), "SessionFactory is null").getCurrentSession();
    }

    @Override
    public User findUserById(int id) {
        Session s = currentSession();
        return s.get(User.class, id);
    }

    @Override
    public User getUserByUsername(String username) {
        Session s = currentSession();
        Query<User> q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return q.uniqueResult();
    }

    @Override
    public void addOrUpdateUser(User u) {
        Session s = currentSession();
        if (u.getId() != null) {
            s.merge(u);
        } else {
            s.persist(u);
        }
    }

    @Override
    public void deleteUser(int id) {
        Session s = currentSession();
        User u = s.get(User.class, id);
        if (u != null) {
            s.remove(u);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        Session s = currentSession();
        Query<User> q = s.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);
        return q.uniqueResult();
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = currentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                String t = kw.trim();
                Predicate byUsername = b.like(root.get("username"), "%" + t + "%");
                Predicate byEmail = b.like(root.get("email"), "%" + t + "%");
                predicates.add(b.or(byUsername, byEmail));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }

        q.orderBy(b.desc(root.get("id")));
        Query<User> query = s.createQuery(q);

        int defaultPageSize = Integer.parseInt(this.env.getProperty("user.page_size", "10"));
        int pageSize = defaultPageSize;
        int page = 1;
        if (params != null) {
            try {
                page = Integer.parseInt(params.getOrDefault("page", "1"));
            } catch (NumberFormatException ignored) {
                page = 1;
            }
            try {
                pageSize = Integer.parseInt(params.getOrDefault("size", String.valueOf(defaultPageSize)));
            } catch (NumberFormatException ignored) {
                pageSize = defaultPageSize;
            }
        }
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = defaultPageSize;
        }

        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public Long countUsers(Map<String, String> params) {
        Session s = currentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<User> root = q.from(User.class);

        q.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                String t = kw.trim();
                Predicate byUsername = b.like(root.get("username"), "%" + t + "%");
                Predicate byEmail = b.like(root.get("email"), "%" + t + "%");
                predicates.add(b.or(byUsername, byEmail));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public boolean existEmail(String email) {
        Session s = currentSession();
        Query<User> q = s.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);
        return q.uniqueResult() != null;
    }

    @Override
    public Long count() {
        Session s = currentSession();
        Query<Long> q = s.createQuery("select count(*) from User", Long.class);
        return q.uniqueResult();
    }
}
