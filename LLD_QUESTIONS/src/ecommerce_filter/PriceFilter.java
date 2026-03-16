package ecommerce_filter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PriceFilter implements  ProductFilter{
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;

    public PriceFilter(BigDecimal minPrice, BigDecimal maxPrice){
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public List<Product> applyFilter(List<Product> products){
        List<Product> filteredPriceProduct = new ArrayList<>();
        for(Product product: products){
            if(product.getPrice().compareTo(minPrice) >= 0 && product.getPrice().compareTo(maxPrice) <= 0){
                filteredPriceProduct.add(product);
            }
        }
        return filteredPriceProduct;
    }
}
