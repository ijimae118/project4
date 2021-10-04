package fpt.aptech.OnlineLaundry.repository;

import fpt.aptech.OnlineLaundry.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,String> {
    Admin findByIdAndAndPassword(String id,String password);
}
