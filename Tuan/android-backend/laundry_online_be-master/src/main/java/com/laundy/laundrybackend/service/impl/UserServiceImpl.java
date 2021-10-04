package com.laundy.laundrybackend.service.impl;

import com.laundy.laundrybackend.config.security.jwt.JwtUserProvider;
import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.constant.ResponseStatusCodeEnum;
import com.laundy.laundrybackend.models.Address;
import com.laundy.laundrybackend.models.User;
import com.laundy.laundrybackend.models.dtos.JwtResponseDTO;
import com.laundy.laundrybackend.models.dtos.UserInfoDTO;
import com.laundy.laundrybackend.models.request.RegisterUserForm;
import com.laundy.laundrybackend.models.request.SocialUserFirstLoginForm;
import com.laundy.laundrybackend.models.request.SocialUserLoginForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.repository.AddressRepository;
import com.laundy.laundrybackend.repository.UserRepository;
import com.laundy.laundrybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserProvider jwtProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUserProvider jwtProvider) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void registerNewUser(RegisterUserForm registerUserForm) {
        User user = User.getUserFromRegisterForm(registerUserForm);
        Address address = Address.addressFromRegisterUserForm(registerUserForm);
        address.setUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (!userRepository.existsByUsernameOrPhoneNumberOrEmail(user.getUsername(), user.getPhoneNumber(), user.getEmail())) {
            addressRepository.save(address);
        } else {
            throw new ValidationException(Constants.USER_EXIST_ERROR_MESS);
        }
    }

    @Override
    public JwtResponseDTO loginUser(UserLoginForm userLoginForm) {
        User user = getUserByUsernameOrPhoneNumber(userLoginForm.getLoginId());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), userLoginForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new JwtResponseDTO(jwt, userDetails.getUsername());
    }

    @Override
    public Object loginSocialUser(SocialUserLoginForm socialUserLoginForm) {
        Optional<User> optionalUser = userRepository.findUserByEmail(socialUserLoginForm.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getIsSocialUser()) return ResponseStatusCodeEnum.SOCIAL_USER_EMAIL_LINK_TO_EXISTED_USER;
            return authenSocialUser(user);
        }
        return ResponseStatusCodeEnum.SOCIAL_USER_NOT_EXIST;
    }

    @Override
    public Object loginSocialUserFirstTime(SocialUserFirstLoginForm socialUserFirstLoginForm) {
        Optional<User> optionalUser = userRepository.findUserByEmail(socialUserFirstLoginForm.getEmail());
        User user;
        if (optionalUser.isPresent()){
            if (!optionalUser.get().getIsSocialUser()) return ResponseStatusCodeEnum.SOCIAL_USER_EMAIL_LINK_TO_EXISTED_USER;
            user = optionalUser.get();
        }else{
            Address address = Address.builder()
                    .address(socialUserFirstLoginForm.getAddress())
                    .city(socialUserFirstLoginForm.getCity())
                    .ward(socialUserFirstLoginForm.getWard())
                    .district((socialUserFirstLoginForm.getDistrict()))
                    .isDefaultAddress(Boolean.TRUE)
                    .receiverName(socialUserFirstLoginForm.getName())
                    .receiverPhoneNumber(socialUserFirstLoginForm.getPhoneNumber())
                    .build();

            user = User.builder()
                    .isSocialUser(Boolean.TRUE)
                    .email(socialUserFirstLoginForm.getEmail())
                    .phoneNumber(socialUserFirstLoginForm.getPhoneNumber())
                    .name(socialUserFirstLoginForm.getName())
                    .username(socialUserFirstLoginForm.getEmail())
                    .password(passwordEncoder.encode(socialUserFirstLoginForm.getEmail()))
                    .build();
            address.setUser(user);
            addressRepository.save(address);
        }
        return authenSocialUser(user);
    }

    @Override
    public UserInfoDTO getCurrentUserInfo() {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Address address = addressRepository.getUserDefaultAddress(user.getId());
        return UserInfoDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .address(address.getAddress())
                .city(address.getCity())
                .district(address.getDistrict())
                .ward(address.getWard())
                .isSocialUser(user.getIsSocialUser())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    private User getUserByUsernameOrPhoneNumber(String loginId) {
        if (userRepository.findByUsername(loginId).isPresent()) {
            return userRepository.findByUsername(loginId).get();
        } else if (userRepository.findUserByPhoneNumber(loginId).isPresent()) {
            return userRepository.findUserByPhoneNumber(loginId).get();
        }
        throw new ValidationException(Constants.USER_NOT_EXISTED_ERROR);
    }

    private JwtResponseDTO authenSocialUser(User user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getUsername()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new JwtResponseDTO(jwt, userDetails.getUsername());
    }
}
