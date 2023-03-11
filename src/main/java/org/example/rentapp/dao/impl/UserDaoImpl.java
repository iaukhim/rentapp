package org.example.rentapp.dao.impl;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.entities.User;
import org.example.rentapp.entities.User_;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<User, Long> implements UserDao {

    private final String DELETE_NON_ACTIVE_USERS_QUERY = "DELETE FROM " + User.class.getName() + " WHERE " + User_.STATUS + " = false";

    private final String LOAD_BY_ID_FETCH = "SELECT u FROM " + User.class.getName() + " u  LEFT OUTER JOIN FETCH u." + User_.ROLES + " LEFT OUTER JOIN FETCH u." + User_.ADDRESS + "  WHERE u." + User_.ID + "= :id";

    private final String FIND_BY_EMAIL = "SELECT u FROM " + User.class.getName() + " u LEFT OUTER JOIN FETCH u." + User_.ROLES + " WHERE u." + User_.EMAIL + " = :email";

    @Override
    public void deleteNonActiveUsers() {
        Query query = entityManager.createQuery(DELETE_NON_ACTIVE_USERS_QUERY);
        query.executeUpdate();
    }

    @Override
    public Optional<User> loadByIdEager(Long id) {
        TypedQuery<User> query = entityManager.createQuery(LOAD_BY_ID_FETCH, User.class);
        query.setParameter("id", id);
        List<User> userList = query.getResultList();
        return userList.isEmpty() ? Optional.empty() : Optional.of(userList.get(0));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery(FIND_BY_EMAIL, User.class);
        query.setParameter("email", email);
        List<User> userList = query.getResultList();
        return userList.isEmpty() ? Optional.empty() : Optional.of(userList.get(0));
    }
}
