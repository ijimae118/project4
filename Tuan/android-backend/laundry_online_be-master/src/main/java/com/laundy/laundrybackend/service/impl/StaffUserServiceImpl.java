package com.laundy.laundrybackend.service.impl;

import com.laundy.laundrybackend.config.security.jwt.JwtUserProvider;
import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.constant.UserRoleEnum;
import com.laundy.laundrybackend.models.ShipperUser;
import com.laundy.laundrybackend.models.StaffUser;
import com.laundy.laundrybackend.models.dtos.JwtResponseDTO;
import com.laundy.laundrybackend.models.request.RegisterNewShipperUserForm;
import com.laundy.laundrybackend.models.request.RegisterNewStaffUserForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.repository.ShipperUserRepository;
import com.laundy.laundrybackend.repository.StaffUserRepository;
import com.laundy.laundrybackend.service.StaffUserService;
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
public class StaffUserServiceImpl implements StaffUserService {
    private final StaffUserRepository staffUserRepository;
    private final ShipperUserRepository shipperUserRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserProvider jwtProvider;

    @Autowired
    public StaffUserServiceImpl(StaffUserRepository staffUserRepository, ShipperUserRepository shipperUserRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUserProvider jwtProvider) {
        this.staffUserRepository = staffUserRepository;
        this.shipperUserRepository = shipperUserRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void registerNewUser(RegisterNewStaffUserForm registerNewStaffUserForm, String role) {
        StaffUser staffUser = StaffUser.staffUserFromRegisterForm(registerNewStaffUserForm);
        if (!staffUserRepository.existsByUsernameOrPhoneNumberOrEmail(staffUser.getUsername(), staffUser.getPhoneNumber(), staffUser.getEmail())) {
            UserRoleEnum userRole = UserRoleEnum.valueOf(role);
            staffUser.setRole(userRole);
            staffUser.setPassword(passwordEncoder.encode(registerNewStaffUserForm.getPassword()));
            staffUserRepository.save(staffUser);
        } else {
            throw new ValidationException(Constants.USER_EXIST_ERROR_MESS);
        }
    }

    @Override
    public void registerNewShipper(RegisterNewShipperUserForm form) {
        ShipperUser shipperUser = ShipperUser.shipperUserFromRegisterForm(form);
        if (!shipperUserRepository.existsByUsernameOrPhoneNumberOrEmail(shipperUser.getUsername(), shipperUser.getPhoneNumber(), shipperUser.getEmail())){
            shipperUser.setPassword(passwordEncoder.encode(form.getPassword()));
            shipperUserRepository.save(shipperUser);
        }else {
            throw new ValidationException(Constants.USER_EXIST_ERROR_MESS);
        }
    }

    @Override
    public JwtResponseDTO loginUser(UserLoginForm userLoginForm) {
        StaffUser staffUser = getUserByUsernameOrPhoneNumber(userLoginForm.getLoginId());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(staffUser.getUsername(), userLoginForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new JwtResponseDTO(jwt,userDetails.getUsername());
    }

    private StaffUser getUserByUsernameOrPhoneNumber(String loginId) {
        if (staffUserRepository.findByUsername(loginId).isPresent()) {
            return staffUserRepository.findByUsername(loginId).get();
        } else if (staffUserRepository.findUserByPhoneNumber(loginId).isPresent()) {
            return staffUserRepository.findUserByPhoneNumber(loginId).get();
        }
        throw new ValidationException(Constants.USER_NOT_EXISTED_ERROR);
    }
}
