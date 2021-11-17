package fpt.aptech.OnlineLaundry.repository;

import fpt.aptech.OnlineLaundry.model.Orders;
import fpt.aptech.OnlineLaundry.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
    @Query("SELECT o FROM Orders o where o.user_email= :email")
    List<Orders> getCus(@Param("email") Users email);

    @Query("SELECT o FROM Orders o where o.created_at between ?1 and ?2")
    List<Orders> searchByDate(Date from, Date to);

}
