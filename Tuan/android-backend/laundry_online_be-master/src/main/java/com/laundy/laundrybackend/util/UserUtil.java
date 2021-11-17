package com.laundy.laundrybackend.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class UserUtil {
    public static boolean checkIfCurrentUserMatchUsername(String username){
        return SecurityContextHolder.getContext().getAuthentication().getName().equals(username);
    }

    public static String getCurrentUserForAuditor(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        List<SimpleGrantedAuthority> roles = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if ((username.isEmpty() || username == null) && roles.isEmpty()) return null;
        return username+"-"+roles.get(0).toString();
    }
}
