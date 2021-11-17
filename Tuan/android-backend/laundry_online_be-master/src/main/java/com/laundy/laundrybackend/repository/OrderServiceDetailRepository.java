package com.laundy.laundrybackend.repository;

import com.laundy.laundrybackend.models.OrderServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderServiceDetailRepository extends JpaRepository<OrderServiceDetail,Long> {
    @Query(value = "select o from OrderServiceDetail o where o.order.id = :orderId")
    List<OrderServiceDetail> findAllByOrderId(Long orderId);
}
