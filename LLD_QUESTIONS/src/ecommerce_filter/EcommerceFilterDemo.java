package ecommerce_filter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.logging.Filter;

public class EcommerceFilterDemo {
    public static void main(String[] args) {
        Product p1 = new Product("Iphone", "APPLE", new BigDecimal("76235.00"), Category.ELECTRONICS, 100, 4.0);
        Product p2 = new Product("Galaxy S24", "SAMSUNG", new BigDecimal("176235.00"), Category.ELECTRONICS, 10, 4.9);
        Product p3 = new Product("PX10", "LENOVO", new BigDecimal("21235.00"), Category.ELECTRONICS, 100, 4.0);
        ProductFilter priceFilter = new PriceFilter(new BigDecimal(0), new BigDecimal("100000"));
        ProductFilter brandFilter = new BrandFilter(Set.of("APPLE", "SAMSUNG", "LENOVO"));

        FilterChain filterChain = new FilterChain().addFilter(priceFilter).addFilter(brandFilter);
        List<Product> result = filterChain.applyFilter(List.of(p1, p2, p3));
        for(Product product: result){
            System.out.println(product.toString());
        }
    }
}
