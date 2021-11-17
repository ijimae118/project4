package com.laundy.laundrybackend.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laundy.laundrybackend.constant.ServiceDetailIconEnum;
import com.laundy.laundrybackend.models.ServiceDetail;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDetailDTO {
    @JsonProperty("serviceDetailId")
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private ServiceDetailIconEnum serviceDetailIcon;

    public static ServiceDetailDTO serviceDetailDTOFromServiceDetail(ServiceDetail serviceDetail){
        return ServiceDetailDTO.builder()
                .id(serviceDetail.getId())
                .name(serviceDetail.getName())
                .description(serviceDetail.getDescription())
                .price((serviceDetail.getPrice()))
                .serviceDetailIcon(serviceDetail.getServiceDetailIcon())
                .build();
    }

    public static List<ServiceDetailDTO> serviceDetailDTOSFromServiceDetails(List<ServiceDetail> serviceDetails){
        List<ServiceDetailDTO> dtos = new ArrayList<>();
        for (ServiceDetail serviceDetail:
             serviceDetails) {
            dtos.add(ServiceDetailDTO.serviceDetailDTOFromServiceDetail(serviceDetail));
        }
        return dtos;
    }
}
