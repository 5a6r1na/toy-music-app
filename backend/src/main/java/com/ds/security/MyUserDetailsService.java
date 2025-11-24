package com.ds.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // For simplicity, we're using an in-memory user with hardcoded credentials.
        // In a real application, you would retrieve user details from a database.
        return new User("foo", "$2a$10$DowJones1KSGRR/DnMUNqS.Ob.3BqMyz2yIkO8E8JwU/P8LOI.nqRS", new ArrayList<>());
    }
}
