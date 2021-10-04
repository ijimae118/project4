package fpt.aptech.OnlineLaundry.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Services implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 50,message = "maximum length is 50")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 1,message = "Price must be greater than 0!")
    @Max(value = 50,message = "Price must be less than 50!")
    private int price;

    @NotBlank(message = "Type is required")
    @Size(max = 50,message = "maximum length is 50")
    private String type;

    private String image;

    private String description;

    @OneToMany(mappedBy = "services_id",cascade = CascadeType.ALL) //mappedBy :cột khóa phụ
    private Collection<Order_detail> order_detailCollection;

    public Services() {
    }

    public Services(@NotBlank(message = "Name is required") String name, @NotNull(message = "Price is required") @Min(value = 1, message = "Price must be greater than 0!") int price, @NotBlank(message = "Type is required") String type, String image, String description) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.image = image;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Order_detail> getOrder_detailCollection() {
        return order_detailCollection;
    }

    public void setOrder_detailCollection(Collection<Order_detail> order_detailCollection) {
        this.order_detailCollection = order_detailCollection;
    }

    @Transient
    public String getServiceImagePath(){
        if (image == null || id==null){
            return "null";
        }else{
            return "images/"+image;
        }
    }
}
