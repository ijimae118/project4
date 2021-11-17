package fpt.aptech.OnlineLaundry.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fpt.aptech.OnlineLaundry.model.*;
import fpt.aptech.OnlineLaundry.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private PayslipServices tbpayslipServices;
    @Autowired
    private ShipperServices shipperServices;
    @Autowired
    private OrdersServices ordersServices;
    @Autowired
    private ServiceServices serviceServices;

    @PostMapping("/signin")
    public String Login(HttpServletRequest request, @RequestParam String id, @RequestParam String password, Model model) {
        Admin admin = adminServices.login(id, password);
//        if (admin != null) {
//            HttpSession session = request.getSession();
//            session.setAttribute("admin_id", admin.getId());
//            model.addAttribute("error","");
//            return "redirect:/Admin_Index";
//        } else {
//            model.addAttribute("error","ID or Password invalid");
//            return "Admin/Login";
//        }
        if (admin != null){
            if (admin.getRole()==0){
                HttpSession session = request.getSession();
                session.setAttribute("employee", admin.getId());
                model.addAttribute("error","");
                if(admin.getEmployee_id().getId()==null){
                    model.addAttribute("error","Please call admin register employee role");
                    session.invalidate();
                    return "Admin/Login";
                }
                return "redirect:/payslip/view/"+admin.getEmployee_id().getId();
            }else if(admin.getRole()==1){
                HttpSession session = request.getSession();
                session.setAttribute("staff",admin.getId());
                model.addAttribute("error","");
                if(admin.getEmployee_id().getId()==null){
                    model.addAttribute("error","Please call admin register employee role");
                    session.invalidate();
                    return "Admin/Login";
                }
                return "redirect:/payslip/view/"+admin.getEmployee_id().getId();
            }else if(admin.getRole()==2){
                HttpSession session = request.getSession();
                session.setAttribute("admin_id", admin.getId());
                model.addAttribute("error","");
                return "redirect:/Admin_Index";
            }
        }
        model.addAttribute("error","ID or Password invalid");
        return "Admin/Login";
    }

    @GetMapping("/signout")
    public String Logout(HttpServletRequest request, Model model) {
            HttpSession session = request.getSession();
            session.invalidate();
            return "Admin/Login";
    }

//    @GetMapping({"/", "/login"})
    @GetMapping("/login")
    public String viewHomeLogin(Model model) {
        return "Admin/Login";
    }

    @GetMapping({"/Admin_Index"})
    public String viewHomeAdmin(HttpServletRequest request,Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("admin",adminServices.listAllAdmin());
        return "Admin/Admin_Index";
    }

    @GetMapping({"/Admin_ConnectLineChart"})
    public String showLineChartAdmin(HttpServletRequest request,Model model) {
        List<LineChart> lineCharts = new ArrayList<>();
        for (Services s : serviceServices.listAllService()){
            lineCharts.add(new LineChart(s.getName(),s.getOrder_detailCollection().size()));
        }
        model.addAttribute("LineChart",lineCharts);
        return "Admin/LineChart";
    }
    @GetMapping({"/Admin_LineChart"})
    @ResponseBody
    public String viewLineChartAdmin(HttpServletRequest request,Model model) {

        List<LineChart> lineCharts = new ArrayList<>();
        for (Services s : serviceServices.listAllService()){
            lineCharts.add(new LineChart(s.getName(),s.getOrder_detailCollection().size()));
        }
//        model.addAttribute("LineChart",lineCharts);

        JsonArray jsonServices = new JsonArray();
        JsonArray jsonBook = new JsonArray();
        JsonObject json = new JsonObject();
        lineCharts.forEach(data->{
            jsonServices.add(data.getName());
            jsonBook.add(data.getSize());
        });
        json.add("services",jsonServices);
        json.add("books",jsonBook);
        return json.toString();
    }

    @GetMapping({"/Admin_Delete"})
    public String deleteHomeAdmin(@RequestParam String id,Model model) {
        adminServices.deleteAdmin(id);
        return "redirect:/Admin_Index";
    }

    @GetMapping("/Admin_New")
    public String showNewAdmin(HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("pays", tbpayslipServices.listAllTbpay());
        model.addAttribute("admin", new Admin());
        model.addAttribute("error","");
        return  "Admin/Admin_New";
    }
    @RequestMapping(value = "/Admin_Save" , method = RequestMethod.POST)
    public String saveNewAdmin(@ModelAttribute("admin") @Valid Admin admin,@RequestParam String employee_id,BindingResult bindingResult,Model model)
    {
        if (bindingResult.hasErrors()){
            return "Admin/Admin_New";
        }
        for (Admin a : adminServices.listAllAdmin()){
            if (a.getId().contains(admin.getId())){
                model.addAttribute("error","ID already exists");
                return "Admin/Admin_New";
            }
        }
        admin.setEmployee_id(tbpayslipServices.getTbpay(employee_id));
        adminServices.SaveOrUpdateAdmin(admin);
        return "redirect:/Admin_Index";
    }
    @GetMapping("/Admin_Edit/{id}")
    public String showAdmin(@PathVariable(name = "id") String id,HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("pays", tbpayslipServices.listAllTbpay());
        model.addAttribute("admin", adminServices.getAdmin(id));
        return "Admin/Admin_Edit";
    }

    @RequestMapping(value = "/Admin_Edit_Save" , method = RequestMethod.POST)
    public String saveEditAdmin(@ModelAttribute("admin") @Valid Admin admin,@RequestParam String employee_id,BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()){
            return "Admin/Admin_Edit";
        }
        admin.setEmployee_id(tbpayslipServices.getTbpay(employee_id));
        adminServices.SaveOrUpdateAdmin(admin);
        return "redirect:/Admin_Index";
    }

    @GetMapping("/reportShipper")
    public String showClientHomePage(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }

        Date date = new Date();
        int salaryTotal = 0;
        for (Shipper s : shipperServices.listAllShipper()){
            salaryTotal += s.getSalary();
        }
        model.addAttribute("shipperList",shipperServices.listAllShipper());
        model.addAttribute("current_date",date.toLocaleString());
        model.addAttribute("salaryTotal",salaryTotal);
        return "Report/ShipperReport";
    }

    @GetMapping("/reportOrders")
    public String showClientOrders(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }

        Date date = new Date();
        float paymentTotal = 0;
        for (Orders o : ordersServices.listAllOrders().stream().filter(o->o.getStatus()!=5).collect(Collectors.toList())){
            paymentTotal += o.getPayment();
        }
        model.addAttribute("ordersList",ordersServices.listAllOrders());
        model.addAttribute("current_date",date.toLocaleString());
        model.addAttribute("paymentTotal",String.format("%.2f", paymentTotal));
        return "Report/OrdersReport";
    }
    //EmployeeReport
    @GetMapping("/EmployeeReport")
    public String showClientHomePageEmployee(Model model) {
        Date date = new Date();
        int total = 0;
        for (Tbpayslip s : tbpayslipServices.listAllTbpay()) {
            total += s.getTransatm28();
        }
        model.addAttribute("payslip", tbpayslipServices.listAllTbpay());
        model.addAttribute("current_date", date.toLocaleString());
        model.addAttribute("total", total);
        return "Report/EmployeeReport";
    }
}
