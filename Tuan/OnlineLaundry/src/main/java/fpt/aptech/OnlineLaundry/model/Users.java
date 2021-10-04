package fpt.aptech.OnlineLaundry.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
public class Users implements Serializable {
    @Id
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "full_name is required")
    private String full_name;

    private String link_image;

    @NotBlank(message = "phone is required")
    @Size(max = 20, message = "maximum length is 20")
    private String phone;

    @NotBlank(message = "address is required")
    private String address;

    //@JoinColumn(name = "uo_fk",referencedColumnName = "email")
    //@OneToMany(targetEntity = Orders.class,cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "user_email", cascade = CascadeType.ALL) //mappedBy :cột khóa phụ
    @JsonManagedReference
    private List<Orders> ordersList;

    public Users() {
    }

    public Users(@Email(message = "Email is invalid") String email, @NotBlank(message = "password is required") String password, @NotBlank(message = "full_name is required") String full_name, @NotBlank(message = "link_image is required") String link_image, @NotBlank(message = "phone is required") String phone) {
        this.email = email;
        this.password = password;
        this.full_name = full_name;
        this.link_image = link_image;
        this.phone = phone;
    }

    public Users(@NotBlank(message = "Email is required") @Email(message = "Email is invalid") String email, @NotBlank(message = "password is required") String password, @NotBlank(message = "full_name is required") String full_name, String link_image, @NotBlank(message = "phone is required") @Size(max = 20, message = "maximum length is 20") String phone, String address) {
        this.email = email;
        this.password = password;
        this.full_name = full_name;
        this.link_image = link_image;
        this.phone = phone;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }
}
