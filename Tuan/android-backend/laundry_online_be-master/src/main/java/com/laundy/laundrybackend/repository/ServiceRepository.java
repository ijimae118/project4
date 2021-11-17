package com.laundy.laundrybackend.repository;

import com.laundy.laundrybackend.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceRepository extends JpaRepository<Service,Long> {

    @Query(value = "select *\n" +
            "            from services s join (select service_id\n" +
            "                           from order_service_details osd\n" +
            "                                  join service_details sd on osd.service_detail_id = sd.id\n" +
            "                          where osd.order_id = :orderId limit 1) jt on s.id = jt.service_id", nativeQuery = true)
    Service getServiceByOrderId(Long orderId);
}
