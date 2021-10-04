package fpt.aptech.OnlineLaundry.services;

import fpt.aptech.OnlineLaundry.model.Admin;
import fpt.aptech.OnlineLaundry.model.Orders;
import fpt.aptech.OnlineLaundry.model.Users;
import fpt.aptech.OnlineLaundry.model.UsersReport;
import fpt.aptech.OnlineLaundry.repository.OrdersRepository;
import fpt.aptech.OnlineLaundry.repository.UsersRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UsersServices {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    OrdersRepository ordersRepository;

    //    Admin
    public List<Users> listAllUsers() {
        return (List<Users>) usersRepository.findAll();
    }

    public Users getUsers(String email) {
        return usersRepository.findById(email).get();
    }

    public void deleteUsers(String email) {
        usersRepository.deleteById(email);
    }

    public void SaveOrUpdateUsers(Users users) {
        usersRepository.save(users);
    }

    public List<UsersReport> reportUsers(){
        return (List<UsersReport>) usersRepository.getUsers();
    }

    public String exportUsersStatusReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = Paths.get("").toAbsolutePath().toString();
        List<UsersReport> usersReports = (List<UsersReport>) usersRepository.getUsers();
        if (usersReports.size()<=0){
            return "The database is empty";
        }
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:Users.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usersReports);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("CreateBy","Q.GiaLam");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        if (reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,path+"\\UsersStatus.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,path+"\\UsersStatus.pdf");
        }
        return "Report Users Status generated in path : "+ path;
    }

    public String exportUserRegisterReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = Paths.get("").toAbsolutePath().toString();
        List<Users> users = usersRepository.findAll();
        if (users.size()<=0){
            return "The database is empty";
        }
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:UsersRegister.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("CreateBy","Q.GiaLam");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        if (reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,path+"\\UsersRegister.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,path+"\\UsersRegister.pdf");
        }
        return "Report Users Register generated in path : "+ path;
    }

    //    Client
    public void ClientRegister(Users users,String address) {
        users.setAddress(address);
        usersRepository.saveAndFlush(users);
//        Date date = new Date();
//        ordersRepository.save(new Orders(address,date,users,users.getPhone(),0));
    }

    public Users login(String email, String password){
        return usersRepository.findByEmailAndPassword(email,password);
    }
}
