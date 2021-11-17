package fpt.aptech.OnlineLaundry.controller;


import fpt.aptech.OnlineLaundry.model.Users;
import fpt.aptech.OnlineLaundry.services.UsersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UsersController {
    @Autowired
    private UsersServices usersServices;

    @GetMapping({"/Users_Index"})
    public String viewHomeUsers(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("users",usersServices.listAllUsers());
        return "Users/Users_Index";
    }

    @GetMapping({"/Users_Delete"})
    public String deleteHomeUsers(@RequestParam String email, Model model) {
        usersServices.deleteUsers(email);
        return "redirect:/Users_Index";
    }

    @GetMapping("/Users_New")
    public String showNewUsers(HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("users", new Users());
        model.addAttribute("error","");
        return  "Users/Users_New";
    }
    @RequestMapping(value = "/Users_Save" , method = RequestMethod.POST)
    public String saveNewUsers(@ModelAttribute("users") @Valid Users users, @RequestParam("fileImage") MultipartFile multipartFile, BindingResult bindingResult, Model model, RedirectAttributes ra) throws IOException {
        if (bindingResult.hasErrors()){
            return  "Users/Users_New";
        }
        for (Users u : usersServices.listAllUsers()){
            if (u.getEmail().equals(users.getEmail())){
                //ra.addFlashAttribute("error","Email already exists");
                model.addAttribute("error","Email already exists");
                return  "Users/Users_New";
            }
        }
        //--
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        users.setLink_image(fileName);
        usersServices.SaveOrUpdateUsers(users);
        //--
        String uploadDir = Paths.get("").toAbsolutePath().toString()+"/src/main/resources/static/images/users-link_image/"; //"./users-link_image/" + users.getEmail();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            //System.out.println(Paths.get("").toAbsolutePath().toString());
            //System.out.println(filePath.toString());
            //System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw  new IOException("Could not save uploaded file: "+ fileName);
        }
        //--
        return "redirect:/Users_Index";
    }
    @GetMapping("/Users_Edit/{email}")
    public String showUsers(@PathVariable(name = "email") String email,HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("users", usersServices.getUsers(email));
        return "Users/Users_Edit";
    }
    @RequestMapping(value = "/Users_Edit_Save" , method = RequestMethod.POST)
    public String saveEditUsers(@ModelAttribute("users") @Valid Users users, @RequestParam("fileImage") MultipartFile multipartFile,BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()){
            return "Users/Users_Edit";
        }
        //--
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        users.setLink_image(fileName);
        usersServices.SaveOrUpdateUsers(users);
        //--
        String uploadDir = Paths.get("").toAbsolutePath().toString()+"/src/main/resources/static/images/users-link_image/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw  new IOException("Could not save uploaded file: "+ fileName);
        }
        //--
        return "redirect:/Users_Index";
    }
}
