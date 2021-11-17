package com.laundy.laundrybackend.service.impl;

import com.laundy.laundrybackend.constant.AcceptedShipperOrderStatusEnum;
import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.constant.OrderStatusEnum;
import com.laundy.laundrybackend.exception.OrderCannotBeCancelException;
import com.laundy.laundrybackend.exception.UnauthorizedException;
import com.laundy.laundrybackend.models.*;
import com.laundy.laundrybackend.models.dtos.OrderDetailResponseDTO;
import com.laundy.laundrybackend.models.dtos.OrderResponseDTO;
import com.laundy.laundrybackend.models.dtos.OrderResponseForShipperDTO;
import com.laundy.laundrybackend.models.dtos.OrderServiceDetailDTO;
import com.laundy.laundrybackend.models.request.NewOrderForm;
import com.laundy.laundrybackend.models.request.OrderPaymentForm;
import com.laundy.laundrybackend.models.request.OrderServiceDetailForm;
import com.laundy.laundrybackend.models.response.ServicesFeeResponse;
import com.laundy.laundrybackend.repository.*;
import com.laundy.laundrybackend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final ServiceDetailsRepository serviceDetailsRepository;
    private final OrderServiceDetailRepository orderServiceDetailRepository;
    private final OrderRepository orderRepository;
    private final ShipFeeRepository shipFeeRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final AddressRepository addressRepository;
    private final ShipperUserRepository shipperUserRepository;

    @Autowired
    public OrderServiceImpl(ServiceDetailsRepository serviceDetailsRepository, OrderServiceDetailRepository orderServiceDetailRepository, OrderRepository orderRepository, ShipFeeRepository shipFeeRepository, UserRepository userRepository, ServiceRepository serviceRepository, AddressRepository addressRepository, ShipperUserRepository shipperUserRepository) {
        this.serviceDetailsRepository = serviceDetailsRepository;
        this.orderServiceDetailRepository = orderServiceDetailRepository;
        this.orderRepository = orderRepository;
        this.shipFeeRepository = shipFeeRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.addressRepository = addressRepository;
        this.shipperUserRepository = shipperUserRepository;
    }

    @Override
    @Transactional
    public OrderDetailResponseDTO createNewOrder(NewOrderForm orderForm) {
        ShipFee shipFee = shipFeeRepository.getShipFeeByDistance(orderForm.getDistance());
        Optional<User> optionalUser = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!optionalUser.isPresent()) throw new NoResultException(Constants.USER_NOT_EXISTED_ERROR);
        User currentUser = optionalUser.get();
        Address userDefaultAddress = addressRepository.getUserDefaultAddress(currentUser.getId());
        Order order = Order.builder()
                .distance(orderForm.getDistance())
                .status(OrderStatusEnum.NEW)
                .shipFee(shipFee)
                .shippingAddress(orderForm.getShippingAddress())
                .totalShipFee(orderForm.getTotalShipFee())
                .totalServiceFee(orderForm.getTotalServiceFee())
                .totalBill((orderForm.getTotalServiceFee().add(orderForm.getTotalShipFee())).multiply(Constants.VAT_VALUES))
                .user(currentUser)
                .isPaid(Boolean.FALSE)
                .pickUpPersonName(userDefaultAddress.getReceiverName())
                .pickUpAddress(userDefaultAddress.getAddress())
                .pickUpCity(userDefaultAddress.getCity())
                .pickUpDistrict(userDefaultAddress.getDistrict())
                .pickUpWard(userDefaultAddress.getWard())
                .pickUpPersonPhoneNumber(userDefaultAddress.getReceiverPhoneNumber())
                .shippingPersonPhoneNumber(orderForm.getShippingPersonPhoneNumber())
                .shippingPersonName(orderForm.getShippingPersonName())
                .longShipping(orderForm.getLongShipping())
                .latShipping((orderForm.getLatShipping()))
                .isCashPay(orderForm.getIsCashPay())
                .build();
        com.laundy.laundrybackend.models.Service service = serviceRepository.getById(orderForm.getServiceId());
        List<OrderServiceDetail> serviceDetails = orderServiceDetailsFromNewOrderForm(orderForm, order);
        orderServiceDetailRepository.saveAllAndFlush(serviceDetails);
        return OrderDetailResponseDTO.OrderDetailResponseDTOFromOrderAndService(order, service, serviceDetails);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatusEnum status, int page, int size) {
        Pageable pageReq
                = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDate"));
        List<Order> orders = orderRepository.getUserOrderByStatusAndUsername(status, SecurityContextHolder.getContext().getAuthentication().getName(), pageReq);
        return getOrderResponseDTOS(orders);
    }

    @Override
    public List<OrderResponseDTO> getIncompleteOrders(int page, int size) {
        Pageable pageReq
                = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDate"));
        List<OrderStatusEnum> statuses = new ArrayList<>(Arrays.asList(OrderStatusEnum.CANCEL, OrderStatusEnum.COMPLETE_ORDER));
        List<Order> orders = orderRepository.getUserOrderByUsernameAndOtherThanStatuses(statuses, SecurityContextHolder.getContext().getAuthentication().getName(), pageReq);
        return getOrderResponseDTOS(orders);
    }

    @Override
    public OrderDetailResponseDTO getOrderDetailForUser(Long orderId) {
        Order order = getOrderById(orderId);
        User user = getUserBySecurityContext();
        if (!order.getUser().equals(user)) throw new UnauthorizedException("USER NOT OWN THE ORDER");
        com.laundy.laundrybackend.models.Service service = serviceRepository.getServiceByOrderId(orderId);
        List<OrderServiceDetail> serviceDetails = orderServiceDetailRepository.findAllByOrderId(orderId);
        return OrderDetailResponseDTO.OrderDetailResponseDTOFromOrderAndService(order, service, serviceDetails);
    }

    @Override
    @Transactional
    public NewOrderForm cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (!order.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                throw new UnauthorizedException("USER NOT OWN THE ORDER");
            }
            if (!checkIfOrderIsCancelable(order)) {
                throw new OrderCannotBeCancelException("ORDER CAN'T BE CANCEL");
            } else {
                order.setIsPaid(false);
                order.setStatus(OrderStatusEnum.CANCEL);
                orderRepository.save(order);
                return getReOrderInfo(order);
            }
        } else {
            throw new NoResultException("NO ORDER MATCH ID");
        }
    }

    @Override
    @Transactional
    public void updateOrderPayment(OrderPaymentForm orderPaymentForm) {
        PaymentInfo paymentInfo = PaymentInfo.paymentInfoFromOrderPaymentForm(orderPaymentForm);
        try {
            Order order = orderRepository.getById(orderPaymentForm.getOrderId());
            order.setIsPaid(Boolean.TRUE);
            order.setPaymentInfo(paymentInfo);
            orderRepository.save(order);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public BigDecimal getServicesFee(Double distance) {
        ShipFee shipFee = shipFeeRepository.getShipFeeByDistance(distance);
        if (shipFee == null) throw new NoResultException("No ship fee found match distance");
        return shipFee.getFee();
    }

    @Override
    public ServicesFeeResponse getServicesFee(List<OrderServiceDetailForm> detailFormList) {
        BigDecimal totalServicesFee = BigDecimal.ZERO;
        List<OrderServiceDetailDTO> serviceDetailDTOS = new ArrayList<>();
        List<Long> ids = detailFormList.stream().map(OrderServiceDetailForm::getServiceDetailId).collect(Collectors.toList());
        List<ServiceDetail> serviceDetails = serviceDetailsRepository.findAllById(ids);
        if (serviceDetails.isEmpty()) throw new NoResultException("NO SERVICE DETAIL");
        for (OrderServiceDetailForm serviceDetailForm : detailFormList) {
            ServiceDetail serviceDetail = serviceDetails.stream()
                    .filter(detail -> serviceDetailForm.getServiceDetailId().equals(detail.getId()))
                    .findAny()
                    .orElse(null);
            if (serviceDetail != null) {
                totalServicesFee = totalServicesFee.add(BigDecimal.valueOf(serviceDetailForm.getQuantity()).multiply((serviceDetail.getPrice())));
                serviceDetailDTOS.add(OrderServiceDetailDTO.orderServiceDetailDTOFromServiceDetail(serviceDetail, serviceDetailForm.getQuantity()));
            }
        }
        return new ServicesFeeResponse(totalServicesFee, serviceDetailDTOS);
    }

    @Override
    public OrderDetailResponseDTO acceptOrderByShipper(Long orderId) {
        Order order = getOrderById(orderId);
        ShipperUser shipperUser = getShipperBySecurityContext();
        if (orderRepository.countAllByShipperUserAndStatusNot(shipperUser,OrderStatusEnum.COMPLETE_ORDER) >= Constants.MAX_SHIPPER_CONCURRENT_ACCEPT_ORDERS) throw new ValidationException("Can't have more than "+Constants.MAX_SHIPPER_CONCURRENT_ACCEPT_ORDERS+" order at a time");
        if (!order.getStatus().equals(OrderStatusEnum.NEW))
            throw new UnauthorizedException("Order has already been accepted by another shipper");
        order.setShipperUser(shipperUser);
        order.setStatus(OrderStatusEnum.SHIPPER_ACCEPTED_ORDER);
        orderRepository.save(order);
        com.laundy.laundrybackend.models.Service service = serviceRepository.getServiceByOrderId(orderId);
        return OrderDetailResponseDTO.OrderDetailResponseDTOFromOrderAndService(order, service, order.getOrderServiceDetails());
    }

    @Override
    public void cancelOrderByShipper(Long orderId) {
        Order order = getOrderById(orderId);
        ShipperUser shipperUser = getShipperBySecurityContext();
        if (!order.getShipperUser().equals(shipperUser))
            throw new UnauthorizedException("Order doesn't link to current shipper");
        if (!checkIfOrderIsCancelableByShipper(order, shipperUser))
            throw new OrderCannotBeCancelException("Order can't be cancel");
        order.setShipperUser(null);
        order.setStatus(OrderStatusEnum.NEW);
        orderRepository.save(order);
    }

    @Override
    public void receivedOrderByShipper(Long orderId) {
        Order order = getOrderById(orderId);
        ShipperUser shipperUser = getShipperBySecurityContext();
        if (!order.getShipperUser().equals(shipperUser))
            throw new UnauthorizedException("Order doesn't link to current shipper");
        if (!order.getStatus().equals(OrderStatusEnum.SHIPPER_ACCEPTED_ORDER))
            throw new UnauthorizedException("Order can't be pick up");
        if (order.getStatus().equals(OrderStatusEnum.SHIPPER_RECEIVED_ORDER))
            throw new UnauthorizedException("Order has already been pick up");
        order.setStatus(OrderStatusEnum.SHIPPER_RECEIVED_ORDER);
        order.setPickUpDateTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public void deliverOrderByShipper(Long orderId) {
        Order order = getOrderById(orderId);
        ShipperUser shipperUser = getShipperBySecurityContext();
        if (!order.getShipperUser().equals(shipperUser))
            throw new UnauthorizedException("Order doesn't link to current shipper");
        if (!order.getStatus().equals(OrderStatusEnum.STORE_DONE_ORDER))
            throw new UnauthorizedException("Order isn't ready to be deliver yet");
        if (order.getStatus().equals(OrderStatusEnum.SHIPPER_DELIVER_ORDER))
            throw new UnauthorizedException("Order has already on the way");
        order.setStatus(OrderStatusEnum.SHIPPER_DELIVER_ORDER);
        orderRepository.save(order);
    }

    @Override
    public void completeOrderByShipper(Long orderId) {
        Order order = getOrderById(orderId);
        ShipperUser shipperUser = getShipperBySecurityContext();
        if (!order.getShipperUser().equals(shipperUser))
            throw new UnauthorizedException("Order doesn't link to current shipper");
        if (!order.getStatus().equals(OrderStatusEnum.SHIPPER_DELIVER_ORDER))
            throw new UnauthorizedException("Order can't be completed");
        if (order.getStatus().equals(OrderStatusEnum.COMPLETE_ORDER))
            throw new UnauthorizedException("Order has already completed");
        order.setStatus(OrderStatusEnum.COMPLETE_ORDER);
        order.setIsPaid(Boolean.TRUE);
        order.setDeliveryDateTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public OrderDetailResponseDTO getOrderDetailForShipper(Long orderId) {
        Order order = getOrderById(orderId);
        ShipperUser shipperUser = getShipperBySecurityContext();
        if (!order.getStatus().equals(OrderStatusEnum.NEW) && !order.getShipperUser().equals(shipperUser))throw new UnauthorizedException("Shipper not linked to this ORDER");
        com.laundy.laundrybackend.models.Service service = serviceRepository.getServiceByOrderId(orderId);
        List<OrderServiceDetail> serviceDetails = orderServiceDetailRepository.findAllByOrderId(orderId);
        return OrderDetailResponseDTO.OrderDetailResponseDTOFromOrderAndService(order, service, serviceDetails);
    }

    @Override
    public List<OrderResponseForShipperDTO> getOrdersByStatusForShipper(AcceptedShipperOrderStatusEnum status, int page, int size) {
        Pageable pageReq
                = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDate"));
        if (status == null){
            List<Order> orders = orderRepository.getUserOrderByStatusAndShipperUsername(null, SecurityContextHolder.getContext().getAuthentication().getName(), pageReq);
            return getOrderResponseForShipperDTOS(orders);
        }
        if (status.equals(AcceptedShipperOrderStatusEnum.INCOMPLETE_ORDER)){
            List<OrderStatusEnum> statuses = new ArrayList<>(Arrays.asList(OrderStatusEnum.CANCEL, OrderStatusEnum.COMPLETE_ORDER));
            List<Order> orders = orderRepository.getUserOrderShipperByUsernameAndOtherThanStatuses(statuses, SecurityContextHolder.getContext().getAuthentication().getName(), pageReq);
            return getOrderResponseForShipperDTOS(orders);
        }else{
            OrderStatusEnum statusEnum = OrderStatusEnum.valueOf(status.name());
            List<Order> orders = orderRepository.getUserOrderByStatusAndShipperUsername(statusEnum, SecurityContextHolder.getContext().getAuthentication().getName(), pageReq);
            return getOrderResponseForShipperDTOS(orders);
        }
    }

    @Override
    public void recivedOrderByStaff(Long orderId) {
        Order order = getOrderById(orderId);
        if (!order.getStatus().equals(OrderStatusEnum.SHIPPER_RECEIVED_ORDER)) throw new UnauthorizedException("Order can't be receive by store");
        order.setStatus(OrderStatusEnum.STORE_RECEIVED_ORDER);
        orderRepository.save(order);
    }

    @Override
    public void doneOrderByStaff(Long orderId) {
        Order order = getOrderById(orderId);
        if (!order.getStatus().equals(OrderStatusEnum.STORE_RECEIVED_ORDER)) throw new UnauthorizedException("Order isn't received by staff yet");
        order.setStatus(OrderStatusEnum.STORE_DONE_ORDER);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatusAndShipperForStaff(OrderStatusEnum status, String shipperLoginId, int page, int size) {
        Pageable pageReq
                = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDate"));
        if(shipperLoginId == null){
            Optional<ShipperUser> optionalShipperUser = shipperUserRepository.findUserByUsernameOrEmailOrPhone(shipperLoginId);
            if (!optionalShipperUser.isPresent()) throw new NoResultException("No shipper match the login information");
            ShipperUser shipperUser = optionalShipperUser.get();
            List<Order> orders = orderRepository.getUserOrderByStatusAndShipperUsername(status,shipperUser.getUsername(),pageReq);
            return getOrderResponseDTOS(orders);
        }
        List<Order> orders = orderRepository.getUserOrderByStatusAndShipperUsername(status,null,pageReq);
        return getOrderResponseDTOS(orders);
    }

    @Override
    public OrderDetailResponseDTO getOrderDetailForStaff(Long orderId) {
        Order order = getOrderById(orderId);
        com.laundy.laundrybackend.models.Service service = serviceRepository.getServiceByOrderId(orderId);
        List<OrderServiceDetail> serviceDetails = orderServiceDetailRepository.findAllByOrderId(orderId);
        return OrderDetailResponseDTO.OrderDetailResponseDTOFromOrderAndService(order, service, serviceDetails);
    }

    @Override
    public List<OrderResponseForShipperDTO> getAvailableOrderListForShipper(int page, int size) {
        Pageable pageReq
                = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDate"));
        List<Order> orders = orderRepository.getOrderByStatus(OrderStatusEnum.NEW, pageReq);
        return getOrderResponseForShipperDTOS(orders);
    }


    private List<OrderServiceDetail> orderServiceDetailsFromNewOrderForm(NewOrderForm orderForm, Order order) {
        List<Long> ids = orderForm.getOrderServiceDetails().stream().map(OrderServiceDetailForm::getServiceDetailId).collect(Collectors.toList());
        List<ServiceDetail> serviceDetails = serviceDetailsRepository.findAllById(ids);
        if (serviceDetails.isEmpty()) throw new NoResultException("NO SERVICE DETAIL");
        List<OrderServiceDetail> orderServiceDetails = new ArrayList<>();
        for (OrderServiceDetailForm serviceDetailForm : orderForm.getOrderServiceDetails()) {
            ServiceDetail serviceDetail = serviceDetails.stream()
                    .filter(detail -> serviceDetailForm.getServiceDetailId().equals(detail.getId()))
                    .findAny()
                    .orElse(null);

            orderServiceDetails.add(OrderServiceDetail.builder()
                    .order(order)
                    .serviceDetail(serviceDetail)
                    .quantity(serviceDetailForm.getQuantity())
                    .build());
        }
        return orderServiceDetails;
    }

    private NewOrderForm getReOrderInfo(Order order) {
        com.laundy.laundrybackend.models.Service service = serviceRepository.getServiceByOrderId(order.getId());
        List<OrderServiceDetailForm> detailForms = new ArrayList<>();
        for (OrderServiceDetail orderServiceDetail : order.getOrderServiceDetails()) {
            detailForms.add(new OrderServiceDetailForm(orderServiceDetail.getServiceDetail().getId(), orderServiceDetail.getQuantity()));
        }
        return NewOrderForm.builder()
                .serviceId(service.getId())
                .distance(order.getDistance())
                .orderServiceDetails(detailForms)
                .totalServiceFee(order.getTotalServiceFee())
                .shippingAddress(order.getShippingAddress())
                .shippingPersonName(order.getShippingPersonName())
                .shippingPersonPhoneNumber(order.getShippingPersonPhoneNumber())
                .totalShipFee(order.getTotalShipFee())
                .build();
    }

    private boolean checkIfOrderIsCancelable(Order order) {
        return order.getStatus().equals(OrderStatusEnum.NEW) || order.getStatus().equals(OrderStatusEnum.SHIPPER_ACCEPTED_ORDER);
    }

    private List<OrderResponseDTO> getOrderResponseDTOS(List<Order> orders) {
        List<OrderResponseDTO> responseDTOS = new ArrayList<>();
        for (Order order : orders) {
            com.laundy.laundrybackend.models.Service service = serviceRepository.getServiceByOrderId(order.getId());
            responseDTOS.add(OrderResponseDTO.orderResponseDTOFromOrderAndService(order, service));
        }
        return responseDTOS;
    }

    private List<OrderResponseForShipperDTO> getOrderResponseForShipperDTOS(List<Order> orders) {
        List<OrderResponseForShipperDTO> responseDTOS = new ArrayList<>();
        for (Order order : orders) {
            com.laundy.laundrybackend.models.Service service = serviceRepository.getServiceByOrderId(order.getId());
            responseDTOS.add(OrderResponseForShipperDTO.orderResponseForShipperDTODTOFromOrderAndService(order, service));
        }
        return responseDTOS;
    }

    private boolean checkIfOrderIsCancelableByShipper(Order order, ShipperUser shipperUser) {
        return order.getStatus().equals(OrderStatusEnum.SHIPPER_ACCEPTED_ORDER) && order.getShipperUser() != null && order.getShipperUser().equals(shipperUser);
    }

    private Order getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) throw new NoResultException("No order matched the id");
        return optionalOrder.get();
    }

    private ShipperUser getShipperBySecurityContext() {
        Optional<ShipperUser> optionalShipperUser = shipperUserRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!optionalShipperUser.isPresent()) throw new UnauthorizedException("Current user is  not a shipper");
        return optionalShipperUser.get();
    }

    private User getUserBySecurityContext() {
        Optional<User> user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!user.isPresent()) throw new UnauthorizedException("Current user is  not a shipper");
        return user.get();
    }
}
