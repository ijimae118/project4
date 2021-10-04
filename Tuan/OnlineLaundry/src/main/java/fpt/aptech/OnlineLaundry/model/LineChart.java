package fpt.aptech.OnlineLaundry.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Entity
public class LineChart implements Serializable {
    @Id
    private String name;

    private int size;

    public LineChart() {
    }

    public LineChart(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "LineChart{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
