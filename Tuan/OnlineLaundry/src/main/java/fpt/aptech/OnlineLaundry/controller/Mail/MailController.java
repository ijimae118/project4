package fpt.aptech.OnlineLaundry.controller.Mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/FeedBack/{email_id}")
    public String showSendFrom(@PathVariable(name = "email_id") String email_id, Model model){
        model.addAttribute("Email",email_id);
        return "Email/Send_New";
    }
    @PostMapping("/Sending")
    public String submitSendFrom(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");
//      C1
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("FeedBack@laundry.com");
//        message.setTo(email);
//
//        //Tieu De
//        String mailSubject = fullname + " has sent a message ";
//        //Noi Dung
//        String mailContent = " Sender Name: " + fullname + "\n";
//        mailContent += " Sender to E-mail: "+email +"\n";
//        mailContent += " Subject: "+subject+"\n";
//        mailContent += " Content: "+content+"\n";
//
//        message.setSubject(mailSubject);
//        message.setText(mailContent);
//        mailSender.send(message);
//      C2
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        String mailSubject = fullname + " has sent a message ";

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
                "    <h1>"+fullname+"</h1>      \n" +
                "    <p>Always keep your clothes clean and beautiful</p>\n" +
                "  </div>\n" +
                "  <div class=\"jumbotron\">\n" +
//                "    <h2>Send to You:</h2>\n" +
                "    <h2>"+subject+"</h2>\n" +
                "    <p>"+content+"</p>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
                mailContent += "<hr/><img src='cid:FooterImage'/>";
        helper.setFrom("ABClaundry@gmail.com","Laundry FeedBack");
        helper.setTo(email);
        helper.setSubject(mailSubject);
        helper.setText(mailContent,true);

        ClassPathResource resource = new ClassPathResource("/static/images/AdminLogin.png");
        helper.addInline("FooterImage",resource);

        mailSender.send(message);
        return "Email/Message";
    }
}
