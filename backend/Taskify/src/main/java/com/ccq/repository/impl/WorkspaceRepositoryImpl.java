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
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Workspace getWorkspaceById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Workspace.class, id);
    }

    @Override
    public void addOrUpdate(Workspace w) {
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
        Workspace w = this.getWorkspaceById(id);
        if (w != null) {
            s.remove(w);
        }
    }

    @Override
    public Workspace getWorkspaceByOwnerId(int ownerId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Workspace> q = s.createQuery(
                "FROM Workspace w WHERE w.ownerId.id = :ownerId", Workspace.class);
        q.setParameter("ownerId", ownerId);
        return q.uniqueResult();
    }

    @Override
    public List<Workspace> getWorkspaces(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Workspace> q = b.createQuery(Workspace.class);
        Root<Workspace> root = q.from(Workspace.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }
        }

        q.orderBy(b.desc(root.get("id")));
        Query<Workspace> query = s.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("workspace.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);

        }

        return query.getResultList();
    }

    @Override
    public List<User> getMembersByWorkspaceId(int workspaceId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<User> q = s.createQuery(
                "SELECT uw.userId FROM UserWorkspace uw WHERE uw.workspaceId.id = :wsId", User.class);
        q.setParameter("wsId", workspaceId);
        return q.getResultList();
    }

    @Override
    public List<Board> getBoardsByWorkspaceId(int wsId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Board> q = s.createQuery("FROM Board b WHERE b.workspaceId.id = :wsId", Board.class);
        q.setParameter("wsId", wsId);
        return q.getResultList();
    }

    @Override
    public Long count() {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Long> q = s.createQuery("SELECT COUNT(*) FROM Workspace", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsByUsernameAndWorkspaceIdAndRole(String username, int workspaceId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Long> q = s.createQuery(
                "SELECT COUNT(w.id) FROM Workspace w WHERE w.id = :wsId AND w.ownerId.username = :username", Long.class);
        q.setParameter("wsId", workspaceId);
        q.setParameter("username", username);

        return q.uniqueResult() > 0;
    }

    @Override
    public boolean existsByUsernameAndWorkspaceId(String username, int workspaceId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Long> q = s.createQuery(
                "SELECT COUNT(uw.id) FROM UserWorkspace uw WHERE uw.workspaceId.id = :wsId AND uw.userId.username = :username", Long.class);
        q.setParameter("wsId", workspaceId);
        q.setParameter("username", username);

        return q.uniqueResult() > 0;
    }

}
