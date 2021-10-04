package fpt.aptech.OnlineLaundry.services;

import fpt.aptech.OnlineLaundry.model.*;
import fpt.aptech.OnlineLaundry.repository.Order_detailRepository;
import fpt.aptech.OnlineLaundry.repository.OrdersRepository;
import fpt.aptech.OnlineLaundry.repository.ServicesRepository;
import fpt.aptech.OnlineLaundry.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class Order_detailServices {
    @Autowired
    Order_detailRepository order_detailRepository;
    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    ServicesRepository servicesRepository;
    @Autowired
    UsersRepository usersRepository;

    public List<Order_detail> listAllOrder_detail() {
        return (List<Order_detail>) order_detailRepository.findAll();
    }

    public Order_detail getOrder_detail(Long id) {
        return order_detailRepository.findById(id).get();
    }

    public void deleteOrder_detail(Long id) {
        order_detailRepository.deleteById(id);
    }

    public void SaveOrUpdateOrder_detail(Order_detail order_detail) {
        order_detailRepository.save(order_detail);
    }

    public void SaveCash(String email,float payment,List<Services_Item> itemList) {
        Date date = new Date();
        Users users = usersRepository.getById(email);
        Orders ordersCash = new Orders(users.getAddress(),date,users,users.getPhone(),0,payment);
        ordersRepository.saveAndFlush(ordersCash);

        for (Services_Item s : itemList) {
            Services servicesCash = servicesRepository.getById(s.getId());
            Order_detail orderDetail = new Order_detail(ordersCash, servicesCash, s.getPrice() * s.getQuantity(), 0, s.getQuantity());
            order_detailRepository.saveAndFlush(orderDetail);
        }

    }
}
