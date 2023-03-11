package org.example.rentapp.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.rentapp.dao.interfaces.AbstractDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

@Repository
public abstract class AbstractDaoImpl<T, PK> implements AbstractDao<T, PK> {
    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> entityClass;

    @Autowired
    public AbstractDaoImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public Optional<T> loadById(PK id) {
        T foundEntity = entityManager.find(entityClass, id);
        return Optional.ofNullable(foundEntity);
    }

    @Override
    public T save(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        if (!entityManager.contains(entity)) {
            entity = entityManager.merge(entity);
        }
        entityManager.remove(entity);
    }

    @Override
    public List<T> loadAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root);
        List<T> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }

}
