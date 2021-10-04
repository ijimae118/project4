package fpt.aptech.OnlineLaundry.services;

import fpt.aptech.OnlineLaundry.model.Admin;
import fpt.aptech.OnlineLaundry.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServices {
    @Autowired
    AdminRepository adminRepository;

    public List<Admin> listAllAdmin()
    {
        return  (List<Admin>) adminRepository.findAll();
    }
    public Admin getAdmin(String id)
    {
        return adminRepository.findById(id).get();
    }
    public  void deleteAdmin(String id)
    {
        adminRepository.deleteById(id);
    }
    public  void SaveOrUpdateAdmin(Admin admin)
    {
        adminRepository.save(admin);
    }
    public Admin login(String id,String password){
        return  adminRepository.findByIdAndAndPassword(id,password);
    }
}
