package fpt.aptech.OnlineLaundry.controller;

import fpt.aptech.OnlineLaundry.model.*;
import fpt.aptech.OnlineLaundry.repository.UsersRepository;
import fpt.aptech.OnlineLaundry.services.*;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class ReportController {
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private ServiceServices serviceServices;
    @Autowired
    private ShipperServices shipperServices;
    @Autowired
    private UsersServices usersServices;
    @Autowired
    private OrdersServices ordersServices;
    @Autowired
    private Order_detailServices order_detailServices;
    @Autowired
    private UsersRepository usersRepository;

//    @GetMapping("/getAdmins")
//    public List<Admin> getAdmins(){
//        return adminServices.listAllAdmin();
//    }
//
//    @GetMapping("/getServices")
//    public List<Services> getServices(){
//        return serviceServices.listAllService();
//    }
//
//    @GetMapping("/getShippers")
//    public List<Shipper> getShippers(){
//        return shipperServices.listAllShipper();
//    }
//
//    @GetMapping("/getUsers")
//    public ResponseEntity<?> getUsers(){
//        return ResponseEntity.status(HttpStatus.OK).body((List<UsersReport>)usersServices.reportUsers());
//    }
//
//
//    @GetMapping("/getOrders")
//    public List<Orders> getOrders(){
//        return ordersServices.listAllOrders();
//    }
//
//    @GetMapping("/getOrder_details")
//    public List<Order_detail> getOrder_details(){
//        return order_detailServices.listAllOrder_detail();
//    }

    @GetMapping("/reportShippers/{format}")
    public String generateShippersReport(@PathVariable String format) throws FileNotFoundException, JRException {
        return  shipperServices.exportReport(format);
    }
    @GetMapping("/reportUsersStatus/{format}")
    public String generateUsersStatusReport(@PathVariable String format) throws FileNotFoundException, JRException {
        return  usersServices.exportUsersStatusReport(format);
    }
    @GetMapping("/reportUsersRegister/{format}")
    public String generateUsersRegisterReport(@PathVariable String format) throws FileNotFoundException, JRException {
        return  usersServices.exportUserRegisterReport(format);
    }
}
