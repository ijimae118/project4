package com.laundy.laundrybackend.config.security.jwt.service;

import com.laundy.laundrybackend.constant.UserRoleEnum;
import com.laundy.laundrybackend.models.ShipperUser;
import com.laundy.laundrybackend.models.StaffUser;
import com.laundy.laundrybackend.models.User;
import com.laundy.laundrybackend.repository.ShipperUserRepository;
import com.laundy.laundrybackend.repository.StaffUserRepository;
import com.laundy.laundrybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final StaffUserRepository staffUserRepository;
    private final ShipperUserRepository shipperUserRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, StaffUserRepository staffUserRepository, ShipperUserRepository shipperUserRepository) {
        this.userRepository = userRepository;
        this.staffUserRepository = staffUserRepository;
        this.shipperUserRepository = shipperUserRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request.getServletPath().contains("staff")){
            StaffUser staffUser = staffUserRepository.findByUsername(username).orElseThrow(
                    () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
            return UserPrinciple.build(staffUser);
        }
        if (request.getServletPath().contains("shipper")){
            ShipperUser shipperUser = shipperUserRepository.findByUsername(username).orElseThrow(
                    () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
            return UserPrinciple.build(shipperUser);
        }
            User user = userRepository.findByUsername(username).orElseThrow(
                    () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));

            return UserPrinciple.build(user);
    }

    @Transactional
    public UserDetails loadUserByUsernameAndRole(String username, UserRoleEnum role) throws UsernameNotFoundException {
        switch (role){
            case ROLE_USER:
                User user = userRepository.findByUsername(username).orElseThrow(
                        () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
                return UserPrinciple.build(user);
            case ROLE_SHIPPER:
                ShipperUser shipperUser = shipperUserRepository.findByUsername(username).orElseThrow(
                        () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
                return UserPrinciple.build(shipperUser);
            case ROLE_ADMIN:
            case ROLE_STAFF:
                StaffUser staffUser = staffUserRepository.findByUsername(username).orElseThrow(
                        () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
                return UserPrinciple.build(staffUser);
        }
        throw new UsernameNotFoundException("Can't match User to system Role");
    }
}
