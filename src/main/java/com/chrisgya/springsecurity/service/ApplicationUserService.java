//package com.chrisgya.springsecurity.service;
//
//import com.chrisgya.springsecurity.dao.ApplicationUserDao;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ApplicationUserService implements UserDetailsService {
//    private final ApplicationUserDao applicationUserDao;
//
//    public ApplicationUserService(@Qualifier("fake") ApplicationUserDao applicationUserDao) {
//        this.applicationUserDao = applicationUserDao;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return applicationUserDao.getApplicationUserByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
//    }
//
//}
