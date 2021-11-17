package fpt.aptech.OnlineLaundry.controller;

import fpt.aptech.OnlineLaundry.model.Order_detail;
import fpt.aptech.OnlineLaundry.model.Orders;
import fpt.aptech.OnlineLaundry.model.Shipper;
import fpt.aptech.OnlineLaundry.services.Order_detailServices;
import fpt.aptech.OnlineLaundry.services.ShipperServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class Order_detailController {
    @Autowired
    private Order_detailServices order_detailServices;
    @Autowired
    private ShipperServices shipperServices;

    @GetMapping({"/Order_detail_Index"})
    public String viewHomeAdmin(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null && session.getAttribute("staff") == null){
            model.addAttribute("error","Please sign in admin or staff account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("order_detail",order_detailServices.listAllOrder_detail());
        return "Order_detail/Order_detail_Index";
    }
//
//    @GetMapping("/Order_detail_Edit/{id}")
//    public String showOrders(@PathVariable(name = "id") Long id, HttpServletRequest request, Model model)
//    {
//        HttpSession session = request.getSession();
//        if (session.getAttribute("admin_id") == null && session.getAttribute("staff") == null){
//            model.addAttribute("error","Please sign in admin or staff account");
//            session.invalidate();
//            return "Admin/Login";
//        }
//
//        model.addAttribute("order_detail", order_detailServices.getOrder_detail(id));
//        model.addAttribute("shipper",shipperServices.listAllShipper());
//        return "Order_detail/Order_detail_Edit";
//    }
//
//    @RequestMapping(value = "/Order_detail_Edit_Save" , method = RequestMethod.POST)
//    public String saveEditOrders(@RequestParam Long id, @RequestParam String  shipper_id, Model model)
//    {
//        Order_detail detail = order_detailServices.getOrder_detail(id);
//        Shipper shipper = shipperServices.getShipper(shipper_id);
//        detail.setShipper_id(shipper);
//        order_detailServices.SaveOrUpdateOrder_detail(detail);
//        return "redirect:/Order_detail_Index";
//    }
}
