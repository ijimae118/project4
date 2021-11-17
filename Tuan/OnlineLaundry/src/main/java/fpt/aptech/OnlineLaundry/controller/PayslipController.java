package fpt.aptech.OnlineLaundry.controller;

import fpt.aptech.OnlineLaundry.model.Tbpayslip;
import fpt.aptech.OnlineLaundry.services.PayslipServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class PayslipController {
    @Autowired
    private PayslipServices payslipServices;

    @GetMapping({"/payslip"})
    public String viewHomeEmployee(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null) {
            model.addAttribute("error", "Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
// Business results
        double total_c = 0, total_t = 300000000;
        List<Tbpayslip> list = payslipServices.listAllTbpay();
        for (Tbpayslip item : list) {
            total_c = total_c + item.getSumreceived27();
        }
        double hq = total_t - total_c;
        model.addAttribute("payslip", payslipServices.listAllTbpay());
        model.addAttribute("totalc", total_c);
        model.addAttribute("totalt", total_t);
        model.addAttribute("hq", hq);
        double tylephantram = (hq / total_c) * 100;
        int t = (int) tylephantram;
        if (t > 0) {
            model.addAttribute("tylephantram", t);
        } else {
            model.addAttribute("tylephantram", t * (-1));
        }
        return "Employee/index";
    }

    @GetMapping({"/payslip/delete/{id}"})
    public String deleteHomeEmployee(@PathVariable(name = "id") String id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null) {
            model.addAttribute("error", "Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        payslipServices.deleteTbpay(id);
        return "redirect:/payslip";
    }

    @GetMapping({"/payslip/create"})
    public String showNewEmployee(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null) {
            model.addAttribute("error", "Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("payslip", new Tbpayslip());
        model.addAttribute("error", "");
        return "Employee/createnew";
    }

    @PostMapping({"/payslip/create"})
    public String saveNewEmployee(@ModelAttribute("payslip") @Valid Tbpayslip tbpayslip, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "Employee/createnew";
        }
        for (Tbpayslip t : payslipServices.listAllTbpay()) {
            if (t.getId().contains(tbpayslip.getId())) {
                model.addAttribute("error", "ID already exists");
                return "Employee/createnew";
            }
        }
        payslipServices.SaveOrUpdateTbpay(tbpayslip);
        return "redirect:/payslip";
    }

    @GetMapping("/payslip/edit/{id}")
    public String showEmployee(@PathVariable(name = "id") String id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null) {
            model.addAttribute("error", "Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }

        model.addAttribute("payslip", payslipServices.getTbpay(id));
        model.addAttribute("baseSalaray", Tbpayslip.BASE_SALARAY);
        model.addAttribute("Socialinsurance", Tbpayslip.SOCIAL_INSURANCE_RATE);
        model.addAttribute("Healthinsurance", Tbpayslip.HEALTH_INSURANCE_RATE);
        model.addAttribute("Unemploymentinsurance", Tbpayslip.UNEMPLOYEMENT_RATE);

        return "Employee/edit";
    }

    @GetMapping("/payslip/view/{id}")
    public String viewDetails(@PathVariable(name = "id") String id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null && session.getAttribute("staff") == null && session.getAttribute("employee") == null) {
            model.addAttribute("error", "Please sign in admin,staff,employee account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("payslip", payslipServices.getTbpay(id));
        return "Employee/details";
    }

    @PostMapping({"/payslip/edit/{id}"})
    public String saveEditEmployee(@ModelAttribute("payslip") @Valid Tbpayslip tbpayslip, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Employee/edit";
        }

        payslipServices.SaveOrUpdateTbpay(tbpayslip);
        return "redirect:/payslip";
    }

    @GetMapping(path = "/payslip/search/{key}", produces = "application/json")
    public Tbpayslip searchPaySlip(@PathVariable String key) {
        return payslipServices.getTbpay(key);
    }

    @GetMapping("/payslip/report/{id}")
    public String viewReport(@PathVariable(name = "id") String id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null && session.getAttribute("staff") == null && session.getAttribute("employee") == null) {
            model.addAttribute("error", "Please sign in admin,staff,employee account");
            session.invalidate();
            return "Admin/Login";
        }

        double total = 0;
        for (Tbpayslip s : payslipServices.listAllTbpay()) {
            total += s.getTransatm28();
        }

        model.addAttribute("payslip", payslipServices.getTbpay(id));
        model.addAttribute("total", total);
        return "Report/PayslipReport";
    }
}