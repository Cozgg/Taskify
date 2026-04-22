/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.Boardlist;
import com.ccq.repository.ListRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
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
public class ListRepositoryImpl implements ListRepository{

    @Autowired
    private Environment env;
    
    @Autowired
    private LocalSessionFactoryBean factory;
    @Override
    public Boardlist getById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Boardlist.class, id);
    }

    @Override
    public void addOrUpdate(Boardlist l) {
        Session s = this.factory.getObject().getCurrentSession();
        if (l.getId() != null) {
            s.merge(l);
        } else {
            s.persist(l);
        }
        
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Boardlist l = this.getById(id);
        if(s != null){
            s.remove(l);
        }
    }

    @Override
    public java.util.List<Boardlist> getList(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Boardlist> q = b.createQuery(Boardlist.class);
        Root<Boardlist> root = q.from(Boardlist.class);
        q.select(root);

        if (params != null) {
            java.util.List<Predicate> predicates = new ArrayList<>();

            String boardId = params.get("boardId");
            if (boardId != null && !boardId.isEmpty()) {
                predicates.add(b.equal(root.get("boardId").get("id"), Integer.parseInt(boardId)));
            }
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }
        }
        q.orderBy(b.asc(root.get("position")));

        Query<Boardlist> query = s.createQuery(q);

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
    
}
