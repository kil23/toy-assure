package com.channel.dao;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class AbstractDao<T> {

    protected Class<T> clazz;

    public AbstractDao(Class<T> clazz){
        this.clazz = clazz;
    }

    @PersistenceContext
    protected EntityManager em;

    protected T getSingle(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    protected  TypedQuery<T> getQuery(String jpql) {
        return em.createQuery(jpql, clazz);
    }

    @Transactional(readOnly = true)
    public T findOne(Long id){
        return em.find( clazz, id);
    }

    @Transactional(readOnly = true)
    public List<T> findAll(){
        return em.createQuery("from " + clazz.getName()).getResultList();
    }

    @Transactional
    public T insert(T t){
        em.persist(t);
        em.flush();
        return t;
    }

    @Transactional
    public void update(T t){
        em.merge(t);
        em.flush();
    }

    @Transactional
    public void delete(T t){
        em.remove(em.contains(t) ? t : em.merge(t));
    }
}
