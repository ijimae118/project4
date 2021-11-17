package fpt.aptech.OnlineLaundry.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class UsersReport implements Serializable {
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
    @Size(max = 20,message = "maximum length is 20")
    private String phone;

    @NotBlank(message = "address is required")
    private String address;

    @NotNull(message = "Status is required")
    private int status;

    public UsersReport() {
    }

    public UsersReport(@NotBlank(message = "Email is required") @Email(message = "Email is invalid") String email, @NotBlank(message = "password is required") String password, @NotBlank(message = "full_name is required") String full_name, String link_image, @NotBlank(message = "phone is required") @Size(max = 20, message = "maximum length is 20") String phone, @NotBlank(message = "address is required") String address, @NotNull(message = "Status is required") int status) {
        this.email = email;
        this.password = password;
        this.full_name = full_name;
        this.link_image = link_image;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    public UsersReport(@NotBlank(message = "Email is required") @Email(message = "Email is invalid") String email, @NotBlank(message = "password is required") String password, @NotBlank(message = "full_name is required") String full_name, @NotBlank(message = "phone is required") @Size(max = 20, message = "maximum length is 20") String phone, @NotBlank(message = "address is required") String address, @NotNull(message = "Status is required") int status) {
        this.email = email;
        this.password = password;
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
        this.status = status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
