package com.laundy.laundrybackend.repository;

import com.laundy.laundrybackend.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    @Query(value = "select a from Address a where a.user.username =:username order by a.isDefaultAddress desc")
    List<Address> getAddressByUsername(String username);
    @Query(value = "delete from Address where id= :id ")
    @Modifying
    void deleteAddressById(Long id);

    @Query(value = "select a from Address a where a.user.id = :userId and a.isDefaultAddress = true ")
    Address getUserDefaultAddress(Long userId);
}
