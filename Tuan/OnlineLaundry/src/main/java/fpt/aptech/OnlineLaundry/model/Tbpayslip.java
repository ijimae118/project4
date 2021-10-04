package fpt.aptech.OnlineLaundry.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Tbpayslip implements Serializable {
    @Id
    @NotBlank(message = "Id is required")
    private String id;

    private String name2;

    private String depart3;

    private String atm4;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date salmonth5;

    private float coefsal7;

    private float coefposis8;

    private float liabfac9;

    private float marsys10;

    private int acworkday12;

    private float oversal14;

    private float superdiem15;

    private float phonesup16;

    private float tradeallow17;

    private float salincrease18;

    private float midsimeal19;

    private float bonoustet20;

    private float monthlyunfe25;

    private float advances;

    @OneToMany(mappedBy = "employee_id", cascade = CascadeType.ALL)
    private List<Admin> adminList;

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }
// private float transatm28;

    public static float BASE_SALARAY = 1490000f;
    public static float SOCIAL_INSURANCE_RATE = 0.08f;
    public static float HEALTH_INSURANCE_RATE = 0.015f;
    public static float UNEMPLOYEMENT_RATE = 0.01f;

    public Tbpayslip(
            @NotBlank(message = "Id is required")
                    String id, String name2, String depart3, String atm4, Date salmonth5,
            float coefsal7, float coefposis8, float liabfac9, float marsys10,
            int acworkday12, float oversal14, float superdiem15,
            float phonesup16, float tradeallow17, float salincrease18,
            float midsimeal19, float bonoustet20,
            float monthlyunfe25, float advances) {

        this.id = id;
        this.name2 = name2;
        this.depart3 = depart3;
        this.atm4 = atm4;
        this.salmonth5 = salmonth5;
        this.coefsal7 = coefsal7;
        this.coefposis8 = coefposis8;
        this.liabfac9 = liabfac9;
        this.marsys10 = marsys10;
        this.acworkday12 = acworkday12;
        this.oversal14 = oversal14;
        this.superdiem15 = superdiem15;
        this.phonesup16 = phonesup16;
        this.tradeallow17 = tradeallow17;
        this.salincrease18 = salincrease18;
        this.midsimeal19 = midsimeal19;
        this.bonoustet20 = bonoustet20;
        this.monthlyunfe25 = monthlyunfe25;
        this.advances = advances;

    }

    public Tbpayslip() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getDepart3() {
        return depart3;
    }

    public void setDepart3(String depart3) {
        this.depart3 = depart3;
    }

    public String getAtm4() {
        return atm4;
    }

    public void setAtm4(String atm4) {
        this.atm4 = atm4;
    }

    public Date getSalmonth5() {
        return this.salmonth5;
    }

    public String getSaleMonth5ToString() {
        String pattern = "dd/MM/yyyy";

        DateFormat df = new SimpleDateFormat(pattern);

        return df.format(this.salmonth5);
    }

    public void setSalmonth5(Date salmonth5) {
        this.salmonth5 = salmonth5;
    }

    //Phân tích lương theo thâm niên công tác COEFSAL7()

    public float getCoefsal7() {
        if (this.coefsal7 <= 0.0) {
            return 0;
        } else {
            if (this.coefsal7 >= 9.0) {
                this.tradeallow17 = (float) 2000000.0;
                this.bonoustet20 = (float) 3000000.0;
                return coefsal7;
            } else {
                if (this.coefsal7 >= 1.0 & this.coefsal7 <= 3.0) {
                    this.tradeallow17 = (float) 500000.0;
                    this.bonoustet20 = (float) 1000000.0;
                    return coefsal7;
                } else { if (this.coefsal7 >= 4.0 & this.coefsal7 <= 6.0) {
                        this.tradeallow17 = (float)1000000.0;
                        this.bonoustet20 = (float) 2000000.0;
                        return coefsal7;
                    } else {
                        if (this.coefsal7 >= 7.0 & this.coefsal7 <= 9.0) {
                            this.tradeallow17 = (float) 2000000.0;
                            this.bonoustet20 = (float) 3000000.0;
                            return coefsal7;
                        }

                    }

                }

            }

        }
  return 0;
    }

    public void setCoefsal7(float coefsal7) {

        this.coefsal7 = coefsal7;
    }

    // Hệ số chức vụ lãnh đạo

    public float getCoefposis8()
    {
        if (this.getDepart3() == null)
        {
            return 0;
        }

        switch (this.getDepart3().toLowerCase())
        {
            case "director":
                return 7f;
            case "chifac":
                return 5f;
            case "leader":
                return 4f;
            case "staff":
                return 3f;
        }

        return 0;
    }
    public void setCoefposis8(float coefposis8) {
        this.coefposis8 = coefposis8;
    }

    // Hệ số trách nhiệm theo dịch vụ của công ty
    public float getLiabfac9()
    {
        if (this.getDepart3() == null)
        {
            return 0;
        }
        switch (this.getDepart3().toLowerCase()) {
            case "technic":
                return 4f;
            case "iron":
                return 3f;
            case "Shipper":
                return 2.5f;
            case "wash":
                return 2f;
            case "worker":
                return 1f;
        }
        return 0;
    }

    public void setLiabfac9(float liabfac9) {
        this.liabfac9 = liabfac9;
    }

    public float getMarsys10() {
        return marsys10;
    }

    public void setMarsys10(float marsys10) {
        this.marsys10 = 1f;
    }

    public float getTotalfac11() {
        return (this.getCoefsal7() + this.getCoefposis8() +this.getLiabfac9() + this.getMarsys10());
    }

    public int getAcworkday12() {
        return acworkday12;
    }

    public void setAcworkday12(int acworkday12) {
        this.acworkday12 = acworkday12;
    }

    public float getBasissal13() {
        return (BASE_SALARAY * this.getTotalfac11() * this.getAcworkday12())/30;
    }

    public float getOversal14() {
        return oversal14;
    }

    public void setOversal14(float oversal14) {
        this.oversal14 = 3000000;
    }

    public float getSuperdiem15() {
        return superdiem15;
    }

    public void setSuperdiem15(float superdiem15) {
        this.superdiem15 = 1f;
    }

    public float getPhonesup16() {
        return phonesup16;
    }

    public void setPhonesup16(float phonesup16) {
        this.phonesup16 = 300000;
    }

    public float getTradeallow17() {
        return tradeallow17;
    }

    public void setTradeallow17(float tradeallow17) {
        this.tradeallow17 = tradeallow17;
    }

    public float getSalincrease18() {
        return salincrease18;
    }

    public void setSalincrease18(float salincrease18) {
        this.salincrease18 = 500000;
    }

    public float getMidsimeal19() {
        return midsimeal19;
    }

    public void setMidsimeal19(float midsimeal19) {
        this.midsimeal19 = 800000;
    }

    public float getBonoustet20() {
        return bonoustet20;
    }

    public void setBonoustet20(float bonoustet20) {
        this.bonoustet20 = bonoustet20;
    }

    public float getTotalincome21() {
        return (  this.getOversal14()
                + this.getSuperdiem15()
                + this.getPhonesup16()
                + this.getTradeallow17()
                + this.getMidsimeal19()
                + this.getBonoustet20());
    }

    public float getSociinsur22() {
        return (SOCIAL_INSURANCE_RATE * this.getBasissal13());
    }

    public float getHealthinsur23() {
        return (HEALTH_INSURANCE_RATE * this.getBasissal13());
    }

    public float getUnemploinsur24() {
        return (UNEMPLOYEMENT_RATE * this.getBasissal13());
    }

    public float getMonthlyunfe25() {
        return monthlyunfe25;
    }

    public void setMonthlyunfe25(float monthlyunfe25) {
        this.monthlyunfe25 = 100000;
    }

    public float getTotaldeductions26() {
        return (this.getSociinsur22()
                + this.getHealthinsur23()
                + this.getUnemploinsur24()
                + this.getMonthlyunfe25());
    }

    public float getSumreceived27() {
        return (this.getBasissal13()
                + this.getTotalincome21()
                - this.getTotaldeductions26());
    }

    public float getTransatm28() {
        return this.getSumreceived27() - this.getAdvances();
    }

    public float getAdvances() {
        return this.advances;
    }

    public void setAdvances(float advances) {
        this.advances = advances;
    }

}
