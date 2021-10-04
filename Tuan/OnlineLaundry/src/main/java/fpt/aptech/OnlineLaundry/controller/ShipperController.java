package fpt.aptech.OnlineLaundry.controller;

import fpt.aptech.OnlineLaundry.model.Shipper;
import fpt.aptech.OnlineLaundry.services.ShipperServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class ShipperController {
    @Autowired
    private ShipperServices shipperServices;

    @GetMapping({"/Shipper_Index"})
    public String viewHomeShipper(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("shipper",shipperServices.listAllShipper());
        return "Shipper/Shipper_Index";
    }

    @GetMapping({"/Shipper_Delete"})
    public String deleteHomeShipper(@RequestParam String id, Model model) {
        shipperServices.deleteShipper(id);
        return "redirect:/Shipper_Index";
    }

    @GetMapping("/Shipper_New")
    public String showNewShipper(HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("shipper", new Shipper());
        model.addAttribute("error","");
        return  "Shipper/Shipper_New";
    }

    @RequestMapping(value = "/Shipper_Save" , method = RequestMethod.POST)
    public String saveNewShipper(@ModelAttribute("shipper") @Valid Shipper shipper,@RequestParam int status, @RequestParam("fileImage") MultipartFile multipartFile, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()){
            return  "Shipper/Shipper_New";
        }
        for (Shipper sh : shipperServices.listAllShipper()){
            if (sh.getId().contains(shipper.getId())){
                model.addAttribute("error","ID already exists");
                return  "Shipper/Shipper_New";
            }
        }
        //--
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        shipper.setImage(fileName);
        shipper.setStatus(status);
        shipperServices.SaveOrUpdateShipper(shipper);
        //--
        String uploadDir = Paths.get("").toAbsolutePath().toString()+"/src/main/resources/static/images/shipers-link_image/"; //"./users-link_image/" + users.getEmail();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            //System.out.println(Paths.get("").toAbsolutePath().toString());
            //System.out.println(filePath.toString());
            //System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw  new IOException("Could not save uploaded file: "+ fileName);
        }
        //--
        return "redirect:/Shipper_Index";
    }

    @GetMapping("/Shipper_Edit/{id}")
    public String showShipper(@PathVariable(name = "id") String id,HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("shipper", shipperServices.getShipper(id));
        return "Shipper/Shipper_Edit";
    }

    @RequestMapping(value = "/Shipper_Edit_Save" , method = RequestMethod.POST)
    public String saveEditShipper(@ModelAttribute("shipper") @Valid Shipper shipper,@RequestParam int status, @RequestParam("fileImage") MultipartFile multipartFile, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()){
            return "Shipper/Shipper_Edit";
        }
        //--
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        shipper.setImage(fileName);
        shipper.setStatus(status);
        shipperServices.SaveOrUpdateShipper(shipper);
        //--
        String uploadDir = Paths.get("").toAbsolutePath().toString()+"/src/main/resources/static/images/shipers-link_image/"; //"./users-link_image/" + users.getEmail();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            //System.out.println(Paths.get("").toAbsolutePath().toString());
            //System.out.println(filePath.toString());
            //System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw  new IOException("Could not save uploaded file: "+ fileName);
        }
        //--
        return "redirect:/Shipper_Index";
    }
}
