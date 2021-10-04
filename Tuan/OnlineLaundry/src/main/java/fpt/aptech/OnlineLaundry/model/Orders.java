package fpt.aptech.OnlineLaundry.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

@Entity
public class Orders implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "address is required")
    private String address;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created_at;

    @JoinColumn(name = "user_email", referencedColumnName = "email")
    @ManyToOne
    @JsonBackReference
    private Users user_email;

    @NotBlank(message = "user_phone is required")
    private String user_phone;

    @NotNull(message = "Status is required")
    private int status;

    @OneToMany(mappedBy = "order_id",cascade = CascadeType.ALL) //mappedBy :cột khóa phụ
    private Collection<Order_detail> order_detailCollection;

    private float payment;

    @JoinColumn(name = "shipper_id", referencedColumnName = "id") // referencedColumnName là cột khóa chính của bảng một// còn lại là foregin key của bảng nhiều
    @ManyToOne
    private Shipper shipper_id;

    public Orders() {
    }

    public Orders(@NotBlank(message = "address is required") String address, Date created_at, Users user_email, @NotBlank(message = "user_phone is required") String user_phone, @NotNull(message = "Status is required") int status) {
        this.address = address;
        this.created_at = created_at;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.status = status;
    }

    public Orders(@NotBlank(message = "address is required") String address, Date created_at, Users user_email, @NotBlank(message = "user_phone is required") String user_phone, @NotNull(message = "Status is required") int status, float payment) {
        this.id = id;
        this.address = address;
        this.created_at = created_at;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.status = status;
        this.payment = payment;
    }

    public Orders(@NotBlank(message = "address is required") String address, Date created_at, Users user_email, @NotBlank(message = "user_phone is required") String user_phone, @NotNull(message = "Status is required") int status, float payment, Shipper shipper_id) {
        this.address = address;
        this.created_at = created_at;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.status = status;
        this.payment = payment;
        this.shipper_id = shipper_id;
    }

    public Shipper getShipper_id() {
        return shipper_id;
    }

    public void setShipper_id(Shipper shipper_id) {
        this.shipper_id = shipper_id;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Users getUser_email() {
        return user_email;
    }

    public void setUser_email(Users user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public Collection<Order_detail> getOrder_detailCollection() {
        return order_detailCollection;
    }

    public void setOrder_detailCollection(Collection<Order_detail> order_detailCollection) {
        this.order_detailCollection = order_detailCollection;
    }
}
