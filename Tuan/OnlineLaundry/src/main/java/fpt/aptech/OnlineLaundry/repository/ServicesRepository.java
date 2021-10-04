package fpt.aptech.OnlineLaundry.repository;

import fpt.aptech.OnlineLaundry.model.LineChart;
import fpt.aptech.OnlineLaundry.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServicesRepository extends JpaRepository<Services,Long> {
    @Query("SELECT se FROM Services se WHERE se.name LIKE %?1% AND se.type = ?2")
    public List<Services> search(String keyword, String type);
}
