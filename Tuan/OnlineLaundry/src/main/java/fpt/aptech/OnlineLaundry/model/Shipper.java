package fpt.aptech.OnlineLaundry.model;

import groovy.transform.ToString;
import net.bytebuddy.build.ToStringPlugin;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Shipper implements Serializable {
    @Id
    @NotBlank(message = "id is required")
    @Size(max = 20,message = "maximum length is 20")
    private String id;

    @NotBlank(message = "Password is required")
    @Size(max = 20,message = "maximum length is 20")
    private String password;

    @NotBlank(message = "Phone is required")
    @Size(max = 20,message = "maximum length is 20")
    private String phone;

    @NotNull(message = "Salary is required")
//    @Min(value = 1,message = "Salary must be greater than 0!")
    private int salary;

    private String name;

    private String image;

    private int status;

    @OneToMany(mappedBy = "shipper_id",cascade = CascadeType.ALL) //mappedBy :cột khóa phụ
    private Collection<Orders> ordersCollection;

    public Shipper() {
    }

    public Shipper(@NotBlank(message = "id is required") @Size(max = 20, message = "maximum length is 20") String id, @NotBlank(message = "Password is required") @Size(max = 20, message = "maximum length is 20") String password, @NotBlank(message = "Phone is required") @Size(max = 20, message = "maximum length is 20") String phone, @NotNull(message = "Salary is required") int salary) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.salary = salary;
    }

    public Shipper(@NotBlank(message = "id is required") @Size(max = 20, message = "maximum length is 20") String id, @NotBlank(message = "Password is required") @Size(max = 20, message = "maximum length is 20") String password, @NotBlank(message = "Phone is required") @Size(max = 20, message = "maximum length is 20") String phone, @NotNull(message = "Salary is required") int salary, String name, String image) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.salary = salary;
        this.name = name;
        this.image = image;
    }

    public Shipper(@NotBlank(message = "id is required") @Size(max = 20, message = "maximum length is 20") String id, @NotBlank(message = "Password is required") @Size(max = 20, message = "maximum length is 20") String password, @NotBlank(message = "Phone is required") @Size(max = 20, message = "maximum length is 20") String phone, @NotNull(message = "Salary is required") int salary, String name, String image, int status) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.salary = salary;
        this.name = name;
        this.image = image;
        this.status = status;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Collection<Orders> getOrdersCollection() {
        return ordersCollection;
    }

    public void setOrdersCollection(Collection<Orders> ordersCollection) {
        this.ordersCollection = ordersCollection;
    }
}
