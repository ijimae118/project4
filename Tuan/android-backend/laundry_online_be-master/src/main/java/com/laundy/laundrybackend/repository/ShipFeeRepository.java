package com.laundy.laundrybackend.repository;

import com.laundy.laundrybackend.models.ShipFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShipFeeRepository extends JpaRepository<ShipFee,Long> {
    @Query(value = "select * from ship_fees where :distance <= max_distance and :distance > min_distance limit 1", nativeQuery = true)
    ShipFee getShipFeeByDistance(double distance);
}
