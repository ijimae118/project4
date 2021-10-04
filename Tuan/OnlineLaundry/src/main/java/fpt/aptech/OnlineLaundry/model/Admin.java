package fpt.aptech.OnlineLaundry.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
@Entity
public class Admin implements Serializable {
    @Id
    @NotBlank(message = "Id is required")
    @Size(max = 20,message = "maximum length is 20")
    private String id;

    @NotBlank(message = "Password is required")
    @Size(max = 20,message = "maximum length is 20")
    private String password;

    private int role;

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Tbpayslip employee_id;

    public Admin() {
    }

    public Admin(@NotBlank(message = "Id is required") @Size(max = 20, message = "maximum length is 20") String id, @NotBlank(message = "Password is required") @Size(max = 20, message = "maximum length is 20") String password, int role, Tbpayslip employee_id) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.employee_id = employee_id;
    }

    public Admin(String id, @NotBlank(message = "Password is required") String password) {
        this.id = id;
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Tbpayslip getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Tbpayslip employee_id) {
        this.employee_id = employee_id;
    }
}
