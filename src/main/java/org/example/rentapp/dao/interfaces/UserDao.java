package org.example.rentapp.dao.interfaces;

import org.example.rentapp.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface UserDao extends AbstractDao<User, Long>, PagingAndSortingRepository<User, Long> {
    void deleteNonActiveUsers();

    Optional<User> loadByIdEager(Long id);

    Optional<User> findByEmail(String email);

}
