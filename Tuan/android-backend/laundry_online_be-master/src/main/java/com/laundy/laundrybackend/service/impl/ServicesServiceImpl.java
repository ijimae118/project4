package com.laundy.laundrybackend.service.impl;

import com.laundy.laundrybackend.models.Service;
import com.laundy.laundrybackend.repository.ServiceRepository;
import com.laundy.laundrybackend.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@org.springframework.stereotype.Service
public class ServicesServiceImpl implements ServicesService {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServicesServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    @Transactional
    public List<Service> getAllService() {
        return serviceRepository.findAll();
    }
}
