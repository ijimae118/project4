package fpt.aptech.OnlineLaundry.controller.Client;

import fpt.aptech.OnlineLaundry.model.Order_detail;
import fpt.aptech.OnlineLaundry.model.Services;
import fpt.aptech.OnlineLaundry.model.Services_Item;
import fpt.aptech.OnlineLaundry.model.Users;
import fpt.aptech.OnlineLaundry.services.Order_detailServices;
import fpt.aptech.OnlineLaundry.services.ServiceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CartController {
    @Autowired
    ServiceServices serviceServices;
    @Autowired
    Order_detailServices order_detailServices;
    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "/addCart/{id}", method = RequestMethod.GET)
    public String addSerCart(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Services services = serviceServices.getService(id);
        serviceServices.addCart(new Services_Item(services.getId(), services.getName(), services.getPrice(), services.getType(), services.getImage(), services.getDescription(), 1));
        HttpSession session = request.getSession();
        session.setAttribute("qtySer", serviceServices.getServices_items().size());
        return "redirect:/getServices?id=" + id;
    }

    @RequestMapping(value = "/addDemand/{id}", method = RequestMethod.GET)
    public String addSerDemand(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Services services = serviceServices.getService(id);
        serviceServices.addDemand(new Services_Item(services.getId(), services.getName(), services.getPrice(), services.getType(), services.getImage(), services.getDescription(), 1));
        HttpSession session = request.getSession();
        session.setAttribute("qtySer", serviceServices.getServices_items().size());
        return "redirect:/show_cart";
    }

    @RequestMapping(value = "/addNowCart/{id}", method = RequestMethod.GET)
    public String addNowSerCart(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Services services = serviceServices.getService(id);
        serviceServices.addCart(new Services_Item(services.getId(), services.getName(), services.getPrice(), services.getType(), services.getImage(), services.getDescription(), 1));
        HttpSession session = request.getSession();
        session.setAttribute("qtySer", serviceServices.getServices_items().size());
        return "redirect:/show_product#ser";
    }

    @GetMapping("/show_cart")
    public String showClientProductCart(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("client") == null) {
            session.invalidate();
            model.addAttribute("users", new Users());
            model.addAttribute("valid", "Please sign in user account");
            return "ClientSide/Client/index";
        }
        model.addAttribute("sumBeforeTax", serviceServices.amountTotal());
        model.addAttribute("demand", serviceServices.listAllService().stream().filter(d -> d.getType().contains("Demand")).collect(Collectors.toList()));
        model.addAttribute("list_SerCart", serviceServices.getServices_items());
        return "ClientSide/Client/pro-cart";
    }

    @GetMapping("/increase_cartItem")
    public String increaseCart(@RequestParam Long id, Model model) {
        serviceServices.clickInCartQ(id);
        return "redirect:/show_cart";
    }

    @GetMapping("/decrease_cartItem")
    public String decreaseCart(@RequestParam Long id, Model model) {
        serviceServices.clickDeCartQ(id);
        return "redirect:/show_cart";
    }

    @GetMapping("/edit_cartItem/{id}")
    public String editCartItem(@PathVariable(name = "id") Long id, @RequestParam int qty, Model model) {
        serviceServices.editCartQ(serviceServices.getItem(id), qty);
        return "redirect:/show_cart";
    }

    @GetMapping("/remove_cartItem/{id}")
    public String removeCartItem(@PathVariable(name = "id") Long id, HttpServletRequest request, Model model) {
        serviceServices.removeCart(serviceServices.getItem(id));
        HttpSession session = request.getSession();
        session.setAttribute("qtySer", serviceServices.getServices_items().size());
        return "redirect:/show_cart";
    }

    //pro-check
    @GetMapping("/show_checkout")
    public String showCheckProductPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("client") == null) {
            session.invalidate();
            model.addAttribute("users", new Users());
            model.addAttribute("valid", "Please sign in user account");
            return "ClientSide/Client/index";
        }
        model.addAttribute("sumBeforeTax", serviceServices.amountTotal());
        double d = (serviceServices.amountTotal() + (serviceServices.amountTotal() * 0.1)) * 23 * 1000;
        int i = (int) d;
        model.addAttribute("sumVNDAfterTax", i);
        model.addAttribute("list_SerCart", serviceServices.getServices_items());
        //----------Session info book for paypal----------------
        String messPaypal = "";
        for (Services_Item s : serviceServices.getServices_items()) {
            messPaypal += s.getName() + "- Price: $" + s.getQuantity() * s.getPrice() + "|";
        }
        session.setAttribute("messPaypal",messPaypal);
        //----------Session info for momo----------------
        String info = "";
        Date date = new Date();
        info += date.toLocaleString() + ".";
        for (Services_Item s : serviceServices.getServices_items()) {
            info += " Your services is: " + s.getName() + "- Price: $" + s.getQuantity() * s.getPrice() + "|";
        }
        info += " Vat: 10%." + "Address: " + session.getAttribute("user_address").toString() + " .Email: " + session.getAttribute("user_email").toString();
        double dou = (serviceServices.amountTotal() + (serviceServices.amountTotal() * 0.1)) * 23 * 1000;
        int in = (int) dou;
        model.addAttribute("sumVNDAfterTaxMoMo", in);
        model.addAttribute("infoMoMo", info);
        return "ClientSide/Client/pro-check";
    }

    //momo
    @GetMapping("/show_momo")
    public String showCheckMomoPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("client") == null) {
            session.invalidate();
            model.addAttribute("users", new Users());
            model.addAttribute("valid", "Please sign in user account");
            return "ClientSide/Client/index";
        }

        String info = "";
        Date date = new Date();
        info += date.toLocaleString() + ".";
        //info += serviceServices.getServices_items().toString();
        for (Services_Item s : serviceServices.getServices_items()) {
            info += " Your services is: " + s.getName() + "- Price: $" + s.getQuantity() * s.getPrice() + "|";
        }
        info += " Vat: 10%." + "Address: " + session.getAttribute("user_address").toString() + " .Email: " + session.getAttribute("user_email").toString();
        double d = (serviceServices.amountTotal() + (serviceServices.amountTotal() * 0.1)) * 23 * 1000;
        int i = (int) d;
        model.addAttribute("sumVNDAfterTax", i);
        model.addAttribute("info", info);
        return "ClientSide/Client/Momopay";
    }

    //cash
    @RequestMapping(value = "/payCash_Save", method = RequestMethod.POST)
    public String savePayment(@RequestParam String email,HttpServletRequest request,Model model) throws MessagingException, UnsupportedEncodingException {
        List<Services_Item> itemList = serviceServices.getServices_items();
        double usd = serviceServices.amountTotal() + (serviceServices.amountTotal() * 0.1);
        float payment = (float) usd;
        order_detailServices.SaveCash(email, payment, itemList);
        //------------Mail-------------
        double vnd = (serviceServices.amountTotal() + (serviceServices.amountTotal() * 0.1)) * 23 * 1000;
        int i = (int) vnd;
        HttpSession session = request.getSession();
        String mess = "";
        Date date = new Date();
        mess += date.toLocaleString() + ".";
        for (Services_Item s : serviceServices.getServices_items()) {
            mess += " Your services is: " + s.getName() + "- Price: $" + s.getQuantity() * s.getPrice() + "|";
        }
        mess += " Vat: 10%. "+"Total Payment: $ "+payment+" ~ "+i+" VND. "+ "Address: " + session.getAttribute("user_address").toString() + " .Email: " + session.getAttribute("user_email").toString();
        //-----------
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String mailSubject = "ABC Laundry has sent a message ";

        String mailContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <title>Bootstrap Example</title>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>\n" +
                "  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>\n" +
                "</head>\n" +
                "<body class=\"panel\">\n" +
                "\n" +
                "<div class=\"container\">\n" +
                "  <div class=\"jumbotron\" style=\"background-image: linear-gradient(to right,deeppink,#00ffff, white);\">\n" +
                "    <h1>ABC Laundry</h1> \n" +
                "    <p>Always keep your clothes clean and beautiful</p>\n" +
                "  </div>\n" +
                "  <div class=\"jumbotron\">\n" +
                "    <h2> Laundry please notify </h2>\n" +
                "    <p> All the services you have booked: </p>\n" +
                "    <p> +"+mess+"+ </p>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        mailContent += "Thanks you for choosing our service.<hr/><img src='cid:FooterImage'/>";
        helper.setFrom("ABClaundry@gmail.com", "Laundry FeedBack");
        helper.setTo(email);
        helper.setSubject(mailSubject);
        helper.setText(mailContent, true);

        ClassPathResource resource = new ClassPathResource("/static/images/thanksCus.jpg");
        helper.addInline("FooterImage", resource);

        mailSender.send(message);
        //-----------------------------
        return "redirect:/reUrl_Client";
    }
}
