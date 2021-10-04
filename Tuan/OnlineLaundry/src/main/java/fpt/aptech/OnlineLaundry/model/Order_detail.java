package fpt.aptech.OnlineLaundry.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Order_detail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id", referencedColumnName = "id") // referencedColumnName là cột khóa chính của bảng một// còn lại là foregin key của bảng nhiều
    @ManyToOne
    private Orders order_id;

    @JoinColumn(name = "services_id", referencedColumnName = "id") // referencedColumnName là cột khóa chính của bảng một// còn lại là foregin key của bảng nhiều
    @ManyToOne
    private Services services_id;

    @NotNull(message = "Price is required")
    private int price;

    private int shipping_fee;

    private int quantity;

    public Order_detail() {
    }

    public Order_detail(Orders order_id, Services services_id, @NotNull(message = "Price is required") int price, int shipping_fee, int quantity) {
        this.order_id = order_id;
        this.services_id = services_id;
        this.price = price;
        this.shipping_fee = shipping_fee;
        this.quantity = quantity;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Orders order_id) {
        this.order_id = order_id;
    }

    public Services getServices_id() {
        return services_id;
    }

    public void setServices_id(Services services_id) {
        this.services_id = services_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(int shipping_fee) {
        this.shipping_fee = shipping_fee;
    }
}
