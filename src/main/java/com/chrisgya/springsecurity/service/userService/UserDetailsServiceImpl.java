package com.chrisgya.springsecurity.service.userService;

import com.chrisgya.springsecurity.dao.UserDao;
import com.chrisgya.springsecurity.model.UserDetailsImpl;
import com.chrisgya.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("no user found"));

        List<String> permissions = userDao.findUserPermissions(user.getId())
                .stream().map(permission -> permission.getName()).collect(Collectors.toList());

        return UserDetailsImpl.build(user, permissions);
    }
}
