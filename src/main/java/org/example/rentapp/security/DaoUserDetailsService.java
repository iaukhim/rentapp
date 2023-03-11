package org.example.rentapp.security;

import lombok.RequiredArgsConstructor;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DaoUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userDao.findByEmail(username);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User with " + username + " was not found"));
        return modelMapper.map(user, SecurityUser.class);
    }
}
