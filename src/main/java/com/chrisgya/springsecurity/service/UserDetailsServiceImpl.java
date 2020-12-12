package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.dao.UserDao;
import com.chrisgya.springsecurity.model.UserDetailsImpl;
import com.chrisgya.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("no user found"));

        var permissions = userDao.findUserPermissions(user.getId());
        return UserDetailsImpl.build(user, permissions);
    }
}
