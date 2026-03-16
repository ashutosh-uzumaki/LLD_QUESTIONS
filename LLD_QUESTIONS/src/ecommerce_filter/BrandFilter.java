package ecommerce_filter;

import java.util.*;

public class BrandFilter implements ProductFilter{
    private final Set<String> brands;
    public BrandFilter(Set<String> brands){
        this.brands = brands;
    }

    @Override
    public List<Product> applyFilter(List<Product> products){
        List<Product> filteredBrandProduct = new ArrayList<>();
        for(Product product: products){
            if(brands.contains(product.getBrand())){
                filteredBrandProduct.add(product);
            }
        }
        return filteredBrandProduct;
    }
}
