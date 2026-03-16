package ecommerce_filter;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {
    private List<ProductFilter> productFilterList;
    public FilterChain(){
        productFilterList = new ArrayList<>();
    }


    public List<Product> applyFilter(List<Product> products){
        List<Product> filteredResults = products;
        for(ProductFilter filter: productFilterList){
            filteredResults = filter.applyFilter(filteredResults);
        }
        return filteredResults;
    }

    public FilterChain addFilter(ProductFilter productFilter){
        productFilterList.add(productFilter);
        return this;
    }

    public FilterChain removeFilter(ProductFilter productFilter){
        productFilterList.remove(productFilter);
        return this;
    }

}
