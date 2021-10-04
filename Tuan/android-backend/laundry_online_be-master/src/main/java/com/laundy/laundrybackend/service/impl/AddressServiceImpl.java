package com.laundy.laundrybackend.service.impl;

import com.laundy.laundrybackend.exception.AddressCannotBeDeleteException;
import com.laundy.laundrybackend.exception.UnauthorizedException;
import com.laundy.laundrybackend.models.Address;
import com.laundy.laundrybackend.models.User;
import com.laundy.laundrybackend.models.request.AddressForm;
import com.laundy.laundrybackend.repository.AddressRepository;
import com.laundy.laundrybackend.repository.UserRepository;
import com.laundy.laundrybackend.service.AddressService;
import com.laundy.laundrybackend.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Address> getAllAddressByCurrentUser() {
        return addressRepository.getAddressByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public void createNewAddressForCurrentUser(AddressForm addressForm) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Address address = Address.addressFromAddressForm(addressForm);
        address.setIsDefaultAddress(Boolean.FALSE);
        address.setUser(user);
        addressRepository.save(address);
    }

    @Override
    public void updateAddressInfo(Long id, AddressForm addressForm) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()){
            Address address = optionalAddress.get();
            if (UserUtil.checkIfCurrentUserMatchUsername(address.getUser().getUsername())){
                address.setAddress(addressForm.getAddress());
                address.setCity(addressForm.getCity());
                address.setDistrict(addressForm.getDistrict());
                address.setWard(addressForm.getWard());
                address.setReceiverName(addressForm.getReceiverName());
                address.setReceiverPhoneNumber(address.getReceiverPhoneNumber());

                addressRepository.save(address);
            }else {
                throw new UnauthorizedException("User not match address owner");
            }
        }else {
            throw new NoResultException("NO ADDRESS match ID");
        }
    }

    @Override
    @Transactional
    public Address deleteAddressById(Long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()){
            Address address = optionalAddress.get();
            if (UserUtil.checkIfCurrentUserMatchUsername(address.getUser().getUsername())){
                if (!address.getIsDefaultAddress()){
                    addressRepository.deleteAddressById(id);
                    return address;
                }else{
                    throw new AddressCannotBeDeleteException("Can't delete default address");
                }
            }else {
                throw new UnauthorizedException("User not match address owner");
            }
        }else {
            throw new NoResultException("NO ADDRESS match ID");
        }
    }

    @Override
    public Address getAddressById(Long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()){
            Address address = optionalAddress.get();
            if (UserUtil.checkIfCurrentUserMatchUsername(address.getUser().getUsername())){
                return address;
            }else {
                throw new UnauthorizedException("User not match address owner");
            }
        }else {
            throw new NoResultException("NO ADDRESS match ID");
        }
    }
}
