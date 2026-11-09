
import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String category;
    private String size;
    private String color;
    private double price;
    private int stock;
    private boolean clearance;

    public Product(String name, String category, String size,
                   String color, double price, int stock,
                   boolean clearance) {
        this.name = name;
        this.category = category;
        this.size = size;
        this.color = color;
        this.price = price;
        this.stock = stock;
        this.clearance = clearance;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getSize() { return size; }
    public String getColor() { return color; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public boolean isClearance() { return clearance; }

    public double getSellingPrice() {
        return clearance ? price * 0.80 : price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setSize(String size) { this.size = size; }
    public void setColor(String color) { this.color = color; }
    public void setPrice(double price) { this.price = price; }
    public void setClearance(boolean clearance) {
        this.clearance = clearance;
    }
}
