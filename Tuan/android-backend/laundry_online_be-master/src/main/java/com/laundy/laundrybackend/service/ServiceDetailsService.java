package com.laundy.laundrybackend.service;

import com.laundy.laundrybackend.models.ServiceDetail;

import java.util.List;

public interface ServiceDetailsService {
    List<ServiceDetail> getAllServiceDetailsByServiceId(Long id);
}
