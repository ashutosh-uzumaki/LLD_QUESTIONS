package VendingMachine.models;

import java.math.BigDecimal;

public class Product {
    private final String productName;
    private final BigDecimal price;

    public Product(String productName, BigDecimal price){
        this.price = price;
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
