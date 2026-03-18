/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.configs.HibernateConfigs;
import com.ccq.pojo.Workspace;
import com.ccq.repository.WorkspaceRepository;
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
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguye
 */
@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Workspace getById(int id) {
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
        Workspace w = this.getById(id);
        if (w != null) {
            s.remove(w);
        }
    }

    @Override
    public List<Workspace> getWorkSpace(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Workspace> q = b.createQuery(Workspace.class);
        Root<Workspace> root = q.from(Workspace.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));

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

}
