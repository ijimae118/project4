package com.laundy.laundrybackend.repository;

import com.laundy.laundrybackend.models.ShipperUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShipperUserRepository extends JpaRepository<ShipperUser, Long> {
    Optional<ShipperUser> findByUsername(String username);

    Optional<ShipperUser> findUserByPhoneNumber(String phoneNumber);

    Optional<ShipperUser> findUserByEmail(String email);

    @Query(value = "select s from ShipperUser s where s.username =: loginId or s.email =:loginId or s.email =:loginId")
    Optional<ShipperUser> findUserByUsernameOrEmailOrPhone(String loginId);

    Boolean existsByUsernameOrPhoneNumberOrEmail(String username, String phoneNumber, String email);
}
