package org.example.rentapp.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.entities.User;
import org.example.rentapp.services.interfaces.UtilService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {

    private final UserDao userDao;

    @Override
    public Boolean checkAccountExistence(String email) {
        Optional<User> user = userDao.findByEmail(email);
        return user.isPresent() ? true : false;
    }
}
