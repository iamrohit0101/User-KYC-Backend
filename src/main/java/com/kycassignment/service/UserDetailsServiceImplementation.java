package com.kycassignment.service;

import com.kycassignment.dao.UserDao;
import com.kycassignment.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  {

        UserEntity user = userDao.findByUserName(username);
//        System.out.println(user+" ----> "+user.getPassword());
        if(user!=null) {
            return new User(user.getUserName(),user.getPassword(), new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("User not found..");
        }
    }
}
