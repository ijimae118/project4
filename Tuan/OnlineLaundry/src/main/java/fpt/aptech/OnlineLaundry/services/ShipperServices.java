package fpt.aptech.OnlineLaundry.services;


import fpt.aptech.OnlineLaundry.model.Shipper;
import fpt.aptech.OnlineLaundry.repository.ShipperRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShipperServices {
    @Autowired
    ShipperRepository shipperRepository;

    public List<Shipper> listAllShipper() {
        return (List<Shipper>) shipperRepository.findAll();
    }

    public Shipper getShipper(String id) {
        return shipperRepository.findById(id).get();
    }

    public void deleteShipper(String id) {
        shipperRepository.deleteById(id);
    }

    public void SaveOrUpdateShipper(Shipper shipper) {
        shipperRepository.save(shipper);
    }

    public String exportReport(String reportFormat) throws JRException, FileNotFoundException {
        String path = Paths.get("").toAbsolutePath().toString();
        List<Shipper> ship = shipperRepository.findAll();
        if (ship.size()<=0){
            return "The database is empty";
        }
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:Shipper.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ship);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("CreateBy","Q.GiaLam");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        if (reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,path+"\\Shippers.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,path+"\\Shippers.pdf");
        }
        return "Report generated in path : "+ path;
    }
}
