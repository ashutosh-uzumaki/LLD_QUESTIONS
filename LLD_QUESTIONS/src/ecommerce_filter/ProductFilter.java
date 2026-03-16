package ecommerce_filter;

import java.util.*;

public interface ProductFilter {
    List<Product> applyFilter(List<Product> products);
}
