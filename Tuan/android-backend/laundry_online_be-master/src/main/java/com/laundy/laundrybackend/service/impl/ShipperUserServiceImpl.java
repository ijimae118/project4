package com.laundy.laundrybackend.service.impl;

import com.laundy.laundrybackend.config.security.jwt.JwtUserProvider;
import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.models.ShipperUser;
import com.laundy.laundrybackend.models.StaffUser;
import com.laundy.laundrybackend.models.dtos.JwtResponseDTO;
import com.laundy.laundrybackend.models.request.RegisterNewShipperUserForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.repository.ShipperUserRepository;
import com.laundy.laundrybackend.service.ShipperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
public class ShipperUserServiceImpl implements ShipperUserService {
    private final ShipperUserRepository shipperUserRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserProvider jwtProvider;

    @Autowired
    public ShipperUserServiceImpl(ShipperUserRepository shipperUserRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUserProvider jwtProvider) {
        this.shipperUserRepository = shipperUserRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public JwtResponseDTO loginUser(UserLoginForm userLoginForm) {
        ShipperUser shipperUser = getUserByUsernameOrPhoneNumber(userLoginForm.getLoginId());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(shipperUser.getUsername(), userLoginForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new JwtResponseDTO(jwt,userDetails.getUsername());
    }


    private ShipperUser getUserByUsernameOrPhoneNumber(String loginId) {
        if (shipperUserRepository.findByUsername(loginId).isPresent()) {
            return shipperUserRepository.findByUsername(loginId).get();
        } else if (shipperUserRepository.findUserByPhoneNumber(loginId).isPresent()) {
            return shipperUserRepository.findUserByPhoneNumber(loginId).get();
        }
        throw new ValidationException(Constants.USER_NOT_EXISTED_ERROR);
    }
}
