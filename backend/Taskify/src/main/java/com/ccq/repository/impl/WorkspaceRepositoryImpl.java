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
import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
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
    public List<Workspace> getWorkspaceByOwnerId(int ownerId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Workspace> q = s.createQuery(
                "FROM Workspace w WHERE w.ownerId.id = :ownerId", Workspace.class);
        q.setParameter("ownerId", ownerId);
        return q.getResultList();
    }

    @Override
    public List<Workspace> getWorkspacesByOwnerId(int ownerId, Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Workspace> q = b.createQuery(Workspace.class);
        Root<Workspace> root = q.from(Workspace.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("ownerId").get("id"), ownerId));

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw.trim())));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(new Predicate[0]));
        }

        q.orderBy(b.desc(root.get("id")));
        Query<Workspace> query = s.createQuery(q);

        int defaultPageSize = Integer.parseInt(this.env.getProperty("workspace.page_size", "10"));
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

        int start = (page - 1) * pageSize;
        query.setFirstResult(start);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public Long countWorkspacesByOwnerId(int ownerId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Workspace> root = q.from(Workspace.class);

        q.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("ownerId").get("id"), ownerId));

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public List<Workspace> getWorkspaces(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Workspace> q = b.createQuery(Workspace.class);
        Root<Workspace> root = q.from(Workspace.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw.trim())));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(new Predicate[0]));
        }

        q.orderBy(b.desc(root.get("id")));
        Query<Workspace> query = s.createQuery(q);

        int defaultPageSize = Integer.parseInt(this.env.getProperty("workspace.page_size", "10"));
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

        int start = (page - 1) * pageSize;
        query.setMaxResults(pageSize);
        query.setFirstResult(start);

        return query.getResultList();
    }

    @Override
    public Long countWorkspaces(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Workspace> root = q.from(Workspace.class);

        q.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw.trim())));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(new Predicate[0]));
        }

        return s.createQuery(q).getSingleResult();
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

    // @Override
    // public boolean isAdminOfThisWorkspace(int workspaceId, String username) {
    //     Session s = this.factory.getObject().getCurrentSession();
    //     Query<Long> q = s.createQuery(
    //             "SELECT COUNT(uw.id) FROM UserWorkspace uw WHERE uw.workspaceId.id = :wsId AND uw.userId.username = :username", Long.class);
    //     q.setParameter("wsId", workspaceId);
    //     q.setParameter("username", username);
    //     return q.uniqueResult() > 0;
    // }
    @Override
    public Long countMembersByWorkspaceId(int workspaceId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Long> q = s.createQuery(
                "SELECT COUNT(uw.id) FROM UserWorkspace uw WHERE uw.workspaceId.id = :wsId", Long.class);
        q.setParameter("wsId", workspaceId);
        return q.uniqueResult();
    }

    @Override
    public void addUserIntoWorkspace(UserWorkspace uw) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(uw);
    }

}
