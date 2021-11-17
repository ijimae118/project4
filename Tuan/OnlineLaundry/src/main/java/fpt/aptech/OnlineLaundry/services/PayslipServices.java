package fpt.aptech.OnlineLaundry.services;

import fpt.aptech.OnlineLaundry.model.Tbpayslip;
import fpt.aptech.OnlineLaundry.repository.TbpayslipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayslipServices {
    @Autowired
    TbpayslipRepository tbpayslipRepository;

    public List<Tbpayslip> listAllTbpay()
    {
        return  (List<Tbpayslip>) tbpayslipRepository.findAll();
    }
    public Tbpayslip getTbpay(String id)
    {
        return tbpayslipRepository.findById(id).get();
    }
    public  void deleteTbpay(String id)
    {
        tbpayslipRepository.deleteById(id);
    }
    public  void SaveOrUpdateTbpay(Tbpayslip tbpayslip)
    {
        tbpayslipRepository.save(tbpayslip);
    }
}
