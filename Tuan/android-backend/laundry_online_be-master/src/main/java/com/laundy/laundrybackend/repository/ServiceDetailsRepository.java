package com.laundy.laundrybackend.repository;

import com.laundy.laundrybackend.models.ServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceDetailsRepository extends JpaRepository<ServiceDetail, Long> {
    @Query(value = "select s from ServiceDetail s where s.service.id = :id")
    List<ServiceDetail> getAllByServiceId(Long id);

    @Query(value = "select s from ServiceDetail s where  s.id in :ids ")
    List<ServiceDetail> findAllById(List<Long> ids);
}
