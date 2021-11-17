package fpt.aptech.OnlineLaundry.controller;

import fpt.aptech.OnlineLaundry.model.Services;
import fpt.aptech.OnlineLaundry.services.ServiceServices;
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
public class ServicesController {
    @Autowired
    private ServiceServices serviceServices;

    @GetMapping({"/Services_Index"})
    public String viewHomeServices(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("services",serviceServices.listAllService());
        return "Services/Services_Index";
    }

    @GetMapping({"/Services_Delete/{id}"})
    public String deleteHomeServices(@PathVariable(name = "id") Long id, Model model) {
        serviceServices.deleteServices(id);
        return "redirect:/Services_Index";
    }

    @GetMapping("/Services_New")
    public String showNewServices(HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("services", new Services());
        model.addAttribute("error","");
        return  "Services/Services_New";
    }

    @RequestMapping(value = "/Services_Save" , method = RequestMethod.POST)
    public String saveNewServices(@ModelAttribute("services") @Valid Services services, @RequestParam("fileImage") MultipartFile multipartFile, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()){
            return "Services/Services_New";
        }
        for (Services s : serviceServices.listAllService()){
            if (s.getId().equals(services.getId())){
                model.addAttribute("error","ID already exists");
                return "Services/Services_New";
            }
        }
        //--
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        services.setImage(fileName);

        //--
        String uploadDir = Paths.get("").toAbsolutePath().toString()+"/src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw  new IOException("Could not save uploaded file: "+ fileName);
        }
        //--
        serviceServices.SaveOrUpdateService(services);
        return "redirect:/Services_Index";
    }
    @GetMapping("/Services_Edit/{id}")
    public String showServices(@PathVariable(name = "id") Long id,HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin_id") == null){
            model.addAttribute("error","Please sign in admin account");
            session.invalidate();
            return "Admin/Login";
        }
        model.addAttribute("services", serviceServices.getService(id));
        return "Services/Services_Edit";
    }

    @RequestMapping(value = "/Services_Edit_Save/{id}" , method = RequestMethod.POST)
    public String saveEditServices(@PathVariable(name = "id") Long id,@ModelAttribute("services") @Valid Services services,@RequestParam("fileImage") MultipartFile multipartFile,BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()){
            return "Services/Services_Edit";
        }
        //--
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Services services1 = serviceServices.getService(id);
        services1.setName(services.getName());
        services1.setPrice(services.getPrice());
        services1.setType(services.getType());
        services1.setImage(fileName);
        services1.setDescription(services.getDescription());
        serviceServices.SaveOrUpdateService(services1);
        //--
        String uploadDir = Paths.get("").toAbsolutePath().toString()+"/src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw  new IOException("Could not save uploaded file: "+ fileName);
        }
        //--
        return "redirect:/Services_Index";
    }
}
