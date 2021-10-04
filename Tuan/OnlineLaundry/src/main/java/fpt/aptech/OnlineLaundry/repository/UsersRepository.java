package fpt.aptech.OnlineLaundry.repository;

import fpt.aptech.OnlineLaundry.model.Admin;
import fpt.aptech.OnlineLaundry.model.Users;
import fpt.aptech.OnlineLaundry.model.UsersReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, String> {
    //Query for report Users Status
    @Query("SELECT new UsersReport (u.email,u.password,u.full_name,u.phone,o.address,o.status) FROM Users u,Orders o where u.email = o.user_email")
    List<UsersReport> getUsers();

    Users findByEmailAndPassword(String email, String password);
}
