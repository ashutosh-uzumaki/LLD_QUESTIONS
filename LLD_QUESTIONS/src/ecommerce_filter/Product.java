package ecommerce_filter;

import java.math.BigDecimal;

public class Product {
    private final String name;
    private final String brand;
    private BigDecimal price;
    private final Category category;
    private int stockQty;
    private double rating;

    public Product(String name, String brand, BigDecimal price, Category category, int stockQty, double rating){
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.stockQty = stockQty;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", stockQty=" + stockQty +
                ", rating=" + rating +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public int getStockQty() {
        return stockQty;
    }

    public double getRating() {
        return rating;
    }
}
