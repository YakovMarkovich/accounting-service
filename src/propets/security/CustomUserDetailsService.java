package propets.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import propets.dao.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository dao;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        propets.entities.User userFounded= dao.findById(userName).orElse(null);
        System.out.println("userFound" + userFounded);
        if (userFounded == null) {
            throw new UsernameNotFoundException("Unknown user: "+userName);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        String[] authStrings = userFounded.getRoles();
        for(String authString : authStrings) {
            authorities.add(new SimpleGrantedAuthority(authString));
        }
        UserDetails user = User.builder()
                .username(userFounded.getUserLogin())
               // .roles(userFounded.getRoles())
                .authorities(authorities)
                .password(userFounded.getPassword())
                .build();
        System.out.println("returned user" + user);
        return user;
    }
}

//"jwtToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2YXN5YUBnbWFpbC5jb20iLCJleHAiOjE2MDMwMjI2ODQsImlhdCI6MTYwMTgyMjY4NH0.e6vAHfkOcXFLIrTRAEuV8H0UCwWBKLcPlTBHJsC4al8"
