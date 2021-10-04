package com.laundy.laundrybackend.service.impl;

import com.laundy.laundrybackend.models.ServiceDetail;
import com.laundy.laundrybackend.repository.ServiceDetailsRepository;
import com.laundy.laundrybackend.service.ServiceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceDetailsServiceImpl implements ServiceDetailsService {
    private final ServiceDetailsRepository serviceDetailsRepository;

    @Autowired
    public ServiceDetailsServiceImpl(ServiceDetailsRepository serviceDetailsRepository) {
        this.serviceDetailsRepository = serviceDetailsRepository;
    }

    @Override
    public List<ServiceDetail> getAllServiceDetailsByServiceId(Long id) {
        return  serviceDetailsRepository.getAllByServiceId(id);
    }
}
