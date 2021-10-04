package com.laundy.laundrybackend.service;

import com.laundy.laundrybackend.models.Address;
import com.laundy.laundrybackend.models.request.AddressForm;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddressByCurrentUser();
    void createNewAddressForCurrentUser(AddressForm addressForm);
    void updateAddressInfo(Long id, AddressForm addressForm);
    Address deleteAddressById(Long id);
    Address getAddressById(Long id);
}
