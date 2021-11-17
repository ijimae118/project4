package com.laundy.laundrybackend.controller.impl;

import com.laundy.laundrybackend.controller.api.ServiceInterface;
import com.laundy.laundrybackend.models.Service;
import com.laundy.laundrybackend.models.ServiceDetail;
import com.laundy.laundrybackend.models.dtos.ServiceDetailDTO;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import com.laundy.laundrybackend.models.response.ResponseFactory;
import com.laundy.laundrybackend.service.ServiceDetailsService;
import com.laundy.laundrybackend.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
public class ServiceController implements ServiceInterface {
    private final ServicesService servicesService;
    private final ServiceDetailsService serviceDetailsService;

    @Autowired
    public ServiceController(ServicesService servicesService, ServiceDetailsService serviceDetailsService) {
        this.servicesService = servicesService;
        this.serviceDetailsService = serviceDetailsService;
    }

    @Override
    public GeneralResponse<?> getAllService() {
        List<Service> services = servicesService.getAllService();;
        return ResponseFactory.successResponse(services);
    }

    @Override
    public GeneralResponse<?> getServiceDetails(Long serviceId) {
        List<ServiceDetail> serviceDetails = serviceDetailsService.getAllServiceDetailsByServiceId(serviceId);
        if (serviceDetails.isEmpty()) return ResponseFactory.successResponse(null);
        return ResponseFactory.successResponse(ServiceDetailDTO.serviceDetailDTOSFromServiceDetails(serviceDetails));
    }
}
