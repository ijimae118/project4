package fpt.aptech.OnlineLaundry.controller.Client;

import fpt.aptech.OnlineLaundry.model.Orders;
import fpt.aptech.OnlineLaundry.model.Services;
import fpt.aptech.OnlineLaundry.model.Services_Item;
import fpt.aptech.OnlineLaundry.model.Users;
import fpt.aptech.OnlineLaundry.services.OrdersServices;
import fpt.aptech.OnlineLaundry.services.ServiceServices;
import fpt.aptech.OnlineLaundry.services.UsersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientController {
    @Autowired
    UsersServices usersServices;
    @Autowired
    ServiceServices serviceServices;
    @Autowired
    OrdersServices ordersServices;

    //---------------------------------------
    @GetMapping("/selfServices")
    public String showClientPricesSelfServices() {
        return "ClientSide/Client/Prices/selfService";
    }

    @GetMapping("/ironing")
    public String showClientPricesIroning() {
        return "ClientSide/Client/Prices/ironing";
    }

    @GetMapping("/dryCleaning")
    public String showClientPricesDryCleaning() {
        return "ClientSide/Client/Prices/dryCleaning";
    }

    @GetMapping("/specialCareItems")
    public String showClientPricesSpecialCareItems() {
        return "ClientSide/Client/Prices/specialCareItems";
    }
    //---------------------------------------

    @GetMapping("/home")
    public String showClientHomePage() {
        return "ClientSide/Home/home";
    }

    @GetMapping("/client")
    public String showClientAboutPage(Model model) {
        model.addAttribute("users", new Users());
        model.addAttribute("valid_create", "");
        return "ClientSide/Client/index";
    }

    @GetMapping({"/", "/show_product"})
    public String showClientProductPage(HttpServletRequest request, Model model, @Param("key") String key, @Param("type") String type) {
        HttpSession session = request.getSession();
        if (session.getAttribute("client") == null) {
            session.invalidate();
            model.addAttribute("users", new Users());
            model.addAttribute("valid", "Please sign in user account");
            return "ClientSide/Client/index";
        }
        List<Services> servicesList = serviceServices.listByName(key, type);
        model.addAttribute("services_list", serviceServices.listByName(key, type).stream().filter(s -> s.getType().contains("Kg") || s.getType().contains("Item")).collect(Collectors.toList()));
        model.addAttribute("list_services", serviceServices.listAllService().stream().filter(s -> s.getType().contains("Kg") || s.getType().contains("Item")).collect(Collectors.toList()));
        model.addAttribute("list_best", serviceServices.listAllService().subList(0, 3));
        return "ClientSide/Client/product";
    }


    @RequestMapping(value = "/Register_Save", method = RequestMethod.POST)
    public String saveNewRegister(@ModelAttribute("users") @Valid Users users, @RequestParam String address, @RequestParam("fileImage") MultipartFile multipartFile, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "ClientSide/Client/index";
        }
        for (Users u : usersServices.listAllUsers()) {
            if (u.getEmail().equals(users.getEmail())) {
                model.addAttribute("users", new Users());
                model.addAttribute("valid_create", "Email already exists");
                return "ClientSide/Client/index";
            }
        }
        //--
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        users.setLink_image(fileName);
        usersServices.ClientRegister(users, address);
        //--
        String uploadDir = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/images/users-link_image/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save uploaded file: " + fileName);
        }
        //--
        return "redirect:/client";
    }

    //Client login
    @PostMapping("/Login_Client")
    public String Login_Client(HttpServletRequest request, @RequestParam String email, @RequestParam String password, Model model) {
        Users users = usersServices.login(email, password);
        //Orders orders = ordersServices.getOrderCus(users);
        if (users != null) {
            HttpSession session = request.getSession();
            //session.setAttribute("order", orders);
            session.setAttribute("client", users);
            session.setAttribute("user_address", users.getAddress());
            session.setAttribute("user_email", users.getEmail());
            model.addAttribute("valid", "");
            return "redirect:/show_product";
        } else {
            model.addAttribute("users", new Users());
            model.addAttribute("valid", "Email or Password invalid");
            return "ClientSide/Client/index";
        }
    }

    @GetMapping("/Logout_Client")
    public String Logout(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        session.invalidate();
        serviceServices.setServices_items(new ArrayList<>());
        return "redirect:/home";
    }

    //for pay by cash
    @GetMapping("/reUrl_Client")
    public String reUrl(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("client") == null) {
            session.invalidate();
            model.addAttribute("users", new Users());
            model.addAttribute("valid", "Please sign in user account");
            return "ClientSide/Client/index";
        }

        model.addAttribute("transaction", "finished");
        model.addAttribute("list_services", serviceServices.listAllService().stream().filter(s -> s.getType().contains("Kg") || s.getType().contains("Item")).collect(Collectors.toList()));
        model.addAttribute("list_best", serviceServices.listAllService().subList(0, 3));
        serviceServices.setServices_items(new ArrayList<>());
        session.setAttribute("qtySer", serviceServices.getServices_items().size());
        return "ClientSide/Client/product";
    }


    //getServices
    @GetMapping("/getServices")
    public String getSer(@RequestParam Long id, Model model) {
        model.addAttribute("ser", serviceServices.getService(id));
        model.addAttribute("list_services", serviceServices.listAllService().stream().filter(s -> s.getType().contains("Kg") || s.getType().contains("Item")).collect(Collectors.toList()));
        return "ClientSide/Client/pro-details";
    }

    //User Manager
    @GetMapping("/show_userSide")
    public String showUserSide(@RequestParam String email, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("client") == null) {
            session.invalidate();
            model.addAttribute("users", new Users());
            model.addAttribute("valid", "Please sign in user account");
            return "ClientSide/Client/index";
        }
        Users users = usersServices.getUsers(email);
        model.addAttribute("users", users);
        model.addAttribute("orders", ordersServices.getAllOrderCus(users));
        return "ClientSide/Client/pro-user";
    }

    @RequestMapping(value = "/show_userSide_Save", method = RequestMethod.POST)
    public String saveUsersEdit(@ModelAttribute("users") @Valid Users users, @RequestParam("fileImage") MultipartFile multipartFile, BindingResult bindingResult, HttpServletRequest request, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "Users/Users_Edit";
        }
        //--
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        users.setLink_image(fileName);
        usersServices.SaveOrUpdateUsers(users);
        //--
        String uploadDir = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/images/users-link_image/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save uploaded file: " + fileName);
        }
        //--
        HttpSession session = request.getSession();
        model.addAttribute("userEdit", "Edit Success");
        model.addAttribute("list_services", serviceServices.listAllService().stream().filter(s -> s.getType().contains("Kg") || s.getType().contains("Item")).collect(Collectors.toList()));
        model.addAttribute("list_best", serviceServices.listAllService().subList(0, 3));
        session.setAttribute("qtySer", serviceServices.getServices_items().size());
        return "ClientSide/Client/product";
    }

    @RequestMapping(value = "/cancel_Service", method = RequestMethod.GET)
    public String saveCancelOrder(@RequestParam Long id, Model model) {
        Orders orders = ordersServices.getOrders(id);
        orders.setStatus(5);
        ordersServices.SaveOrUpdateOrders(orders);
        model.addAttribute("Canceled", "No " + id + ": Cancel successfully");

        Users users = usersServices.getUsers(orders.getUser_email().getEmail());
        model.addAttribute("users", users);
        model.addAttribute("orders", ordersServices.getAllOrderCus(users));
        return "ClientSide/Client/pro-user";
    }



//    @GetMapping("/reMoMo")
//    public String reUrlMoMo(HttpServletRequest request, Model model, @RequestParam(name = "partnerCode") String partnerCode, @RequestParam(name = "orderId") Integer orderId, @RequestParam(name = "requestId") Integer requestId,@RequestParam(name = "amount") Integer amount, @RequestParam(name = "orderInfo") String orderInfo, @RequestParam(name = "orderType") String orderType, @RequestParam(name = "transId") Integer transId, @RequestParam(name = "resultCode") Integer resultCode, @RequestParam(name = "message") String message, @RequestParam(name = "payType") String payType,@RequestParam(name = "responseTime") Integer responseTime, @RequestParam(name = "extraData") String extraData, @RequestParam(name = "signature") String signature) {
//        if (resultCode == 0) { // thành công
//            return "redirect:/show_product";
//        } else { // thất bại
//            return "redirect:/show_checkout";
//        }
//    }
}
