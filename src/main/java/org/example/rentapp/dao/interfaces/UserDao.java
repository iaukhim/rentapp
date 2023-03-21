package org.example.rentapp.dao.interfaces;

import org.example.rentapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserDao extends AbstractDao<User, Long> {
    void deleteNonActiveUsers();

    Optional<User> loadByIdEager(Long id);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

}
