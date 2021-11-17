package fpt.aptech.OnlineLaundry.controller;

import fpt.aptech.OnlineLaundry.model.Orders;
import fpt.aptech.OnlineLaundry.model.Shipper;
import fpt.aptech.OnlineLaundry.services.OrdersServices;
import fpt.aptech.OnlineLaundry.services.ServiceServices;
import fpt.aptech.OnlineLaundry.services.ShipperServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class OrdersController {
    @Autowired
    private OrdersServices ordersServices;
    @Autowired
    private ShipperServices shipperServices;
    @Autowired
    private ServiceServices serviceServices;

    @GetMapping({"/Orders_Index"})
    public String viewHomeOrders(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null && session.getAttribute("staff") == null) {
            model.addAttribute("error", "Please sign in admin or staff account");
            session.invalidate();
            return "Admin/Login";
        }
        List<Date> dates = ordersServices.listAllOrders().stream().map(x->x.getCreated_at()).collect(Collectors.toList());
        List<Float> priceList = ordersServices.listAllOrders().stream().map(x->x.getPayment()).collect(Collectors.toList());
        model.addAttribute("dates", dates);
        model.addAttribute("price", priceList);
        model.addAttribute("orders", ordersServices.listAllOrders());
        return "Orders/Orders_Index";
    }

    @GetMapping({"/Orders_Delete"})
    public String deleteHomeOrders(@RequestParam Long id, Model model) {
        ordersServices.deleteOrders(id);
        return "redirect:/Orders_Index";
    }

    @GetMapping("/Orders_Edit/{id}")
    public String showOrders(@PathVariable(name = "id") Long id, HttpServletRequest request, Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null && session.getAttribute("staff") == null){
            model.addAttribute("error","Please sign in admin or staff account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("shipper",shipperServices.listAllShipper().stream().filter(s->s.getStatus()==0).collect(Collectors.toList()));
        model.addAttribute("orders", ordersServices.getOrders(id));
        return "Orders/Orders_Edit";
    }

    @RequestMapping(value = "/Orders_Edit_Save" , method = RequestMethod.POST)
    public String saveEditOrders(@RequestParam Long id,@RequestParam int status,@RequestParam String shipper_id,Model model)
    {
        Orders orders1 = ordersServices.getOrders(id);
        double vnd = orders1.getPayment()*0.2;
        int i = (int) vnd;
        if (i == 0){
            i = 1;
        }
        Shipper shipper = shipperServices.getShipper(shipper_id);
        if (shipper.getStatus()==0){
            shipper.setSalary(shipper.getSalary()+i);
        }
        shipper.setStatus(1);
        shipperServices.SaveOrUpdateShipper(shipper);

        orders1.setStatus(status);
        orders1.setShipper_id(shipper);
        ordersServices.SaveOrUpdateOrders(orders1);
        return "redirect:/Orders_Index";
    }

    @RequestMapping(value = "/Orders_Search_Date", method = RequestMethod.POST)
    public String searchOrdersByDate(HttpServletRequest request, @RequestParam String search, Model model) {

        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null) {
            model.addAttribute("error", "Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }

        if (search.isEmpty()) {
            return "redirect:/reportOrders";
        }

        List<Orders> find = ordersServices.listAllOrders().stream().filter(o->String.valueOf(o.getId()).contains(search) || o.getUser_email().getEmail().contains(search) || o.getAddress().contains(search) || o.getCreated_at().toString().contains(search) || o.getUser_phone().contains(search) || String.valueOf(o.getPayment()).contains(search) || String.valueOf(o.getStatus()).contains(search)).collect(Collectors.toList());

        if (find.size()==0){
            return "redirect:/reportOrders";
        }
        Date date = new Date();
        float paymentTotal = 0;
        for (Orders o : find) {
            paymentTotal += o.getPayment();
        }
        model.addAttribute("ordersList", find);
        model.addAttribute("current_date", date.toLocaleString());
        model.addAttribute("paymentTotal", String.format("%.2f",paymentTotal));
        return "Report/OrdersReport";
    }

}
