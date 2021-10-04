package fpt.aptech.OnlineLaundry.services;

import fpt.aptech.OnlineLaundry.model.Admin;
import fpt.aptech.OnlineLaundry.model.LineChart;
import fpt.aptech.OnlineLaundry.model.Services;
import fpt.aptech.OnlineLaundry.model.Services_Item;
import fpt.aptech.OnlineLaundry.repository.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceServices {
    @Autowired
    ServicesRepository servicesRepository;

    //-------------------------------------------------tbService

    public List<Services> listAllService() {
        return (List<Services>) servicesRepository.findAll();
    }

    public Services getService(Long id) {
        return servicesRepository.findById(id).get();
    }

    public void deleteServices(Long id) {
        servicesRepository.deleteById(id);
    }

    public void SaveOrUpdateService(Services services) {
        servicesRepository.save(services);
    }

    public List<Services> listByName(String keyword, String type){
        List<Services> servicesList = (List<Services>) servicesRepository.findAll();
        if (keyword!= null && type != null){
            servicesList.stream().filter(se->se.getName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
            servicesList.stream().filter(se->se.getType().toLowerCase().contains(type)).collect(Collectors.toList());
            return servicesRepository.search(keyword, type);
        }else {
            return (List<Services>) servicesRepository.findAll();
        }
    }

    //--------------------------------------------------Cart

    List<Services_Item> services_items = null;

    //Contructor
    public ServiceServices() {
        services_items = new ArrayList<Services_Item>();
    }

    //Getter(Show) && Setter ~ All List Cart Service
    public List<Services_Item> getServices_items() {
        return services_items;
    }

    public void setServices_items(List<Services_Item> services_items) {
        this.services_items = services_items;
    }

    //Get Item in cart
    public Services_Item getItem(Long id) {
        Services_Item result = null;
        for (Services_Item s : services_items) {
            if (s.getId().equals(id)) {
                result = s;
                break;
            }
        }
        return result;
    }

    //Total Price in cart
    public Integer amountTotal() {
        float result = 0;
        for (Services_Item s : services_items) {
            result += (s.getPrice() * s.getQuantity());
        }
        return Math.round(result);
    }

    //Total Quantity in cart
    public Integer amountQuantity() {
        Integer result = 0;
        for (Services_Item s : services_items) {
            result += s.getQuantity();
        }
        return result;
    }

    //Add item to Cart
    public void addCart(Services_Item item) {
        Services_Item find = this.getItem(item.getId());
        if (find != null) {
            find.setQuantity(find.getQuantity() + 1);
        } else {
            services_items.add(item);
        }
    }

    public void addDemand(Services_Item item) {
        Services_Item find = this.getItem(item.getId());
        if (find != null) {
            find.setQuantity(find.getQuantity());
        } else {
            services_items.add(item);
        }
    }

    //Edit item quantity in Cart
    public void editCartQ(Services_Item item, Integer quantity) {
        Services_Item find = this.getItem(item.getId());
        if (find != null && quantity > 0) {
            find.setQuantity(quantity);
        } else {
            find.setQuantity(1);
        }
    }

    public void clickInCartQ(Long id) {
        Services_Item find = this.getItem(id);
        if (find != null) {
            find.setQuantity(find.getQuantity() + 1);
        }
    }

    public void clickDeCartQ(Long id) {
        Services_Item find = this.getItem(id);
        if (find != null) {
            if (find.getQuantity() <= 1) {
                find.setQuantity(1);
            } else {
                find.setQuantity(find.getQuantity() - 1);
            }
        }
    }

    //Remove item to Cart
    public void removeCart(Services_Item item) {
        List<Services_Item> items = services_items.stream().filter(s -> s.getType().contains("Kg") || s.getType().contains("Item")).collect(Collectors.toList());
        List<Services_Item> demand = services_items.stream().filter(s -> s.getType().contains("Demand")).collect(Collectors.toList());

        if (items.size() <= 1) {
            if (!item.getType().contains("Demand")){
                services_items.clear();
            }else{
                services_items.remove(item);
            }
        } else{
            services_items.remove(item);
        }
    }

}
