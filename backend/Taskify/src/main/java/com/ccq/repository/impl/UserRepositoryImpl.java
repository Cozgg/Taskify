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
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccq.pojo.User;
import com.ccq.pojo.UserWorkspace;
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

    @Override
    public User findUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(User.class, id);
    }

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<User> q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return q.uniqueResult();
    }

    @Override
    public void addOrUpdateUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        if (u.getId() != null) {
            s.merge(u);
        } else {
            s.persist(u);
        }
    }

    @Override
    public void deleteUser(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        User u = s.get(User.class, id);
        if (u != null) {
            s.remove(u);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<User> q = s.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);
        return q.uniqueResult();
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate byUsername = b.like(root.get("username"), "%" + kw + "%");
                Predicate byEmail = b.like(root.get("email"), "%" + kw + "%");
                predicates.add(b.or(byUsername, byEmail));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }
        }

        q.orderBy(b.desc(root.get("id")));
        Query<User> query = s.createQuery(q);

        int pageSize = Integer.parseInt(this.env.getProperty("user.page_size", "10"));
        int page = params != null ? Integer.parseInt(params.getOrDefault("page", "1")) : 1;
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public boolean existEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<User> q = s.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);
        return q.uniqueResult() != null;
    }

    @Override
    public Long count() {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Long> q = s.createQuery("select count(*) from User", Long.class);
        return q.uniqueResult();
    }
}
