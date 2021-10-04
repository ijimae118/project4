package fpt.aptech.OnlineLaundry.services;

import fpt.aptech.OnlineLaundry.model.Admin;
import fpt.aptech.OnlineLaundry.model.Orders;
import fpt.aptech.OnlineLaundry.model.Users;
import fpt.aptech.OnlineLaundry.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrdersServices {
    @Autowired
    OrdersRepository ordersRepository;

    public List<Orders> listAllOrders()
    {
        return (List<Orders>) ordersRepository.findAll();
    }
    public Orders getOrders(Long id)
    {
        return ordersRepository.findById(id).get();
    }
    public void deleteOrders(Long id)
    {
        ordersRepository.deleteById(id);
    }
    public void SaveOrUpdateOrders(Orders orders)
    {
        ordersRepository.save(orders);
    }
    public List<Orders> getAllOrderCus(Users email)
    {
        return ordersRepository.getCus(email);
    }
    public List<Orders> searchOrdersByDate(Date a ,Date b) {return ordersRepository.searchByDate(a,b);}
}
