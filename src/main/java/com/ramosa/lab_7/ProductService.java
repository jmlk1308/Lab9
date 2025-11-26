package com.ramosa.lab_7;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProductService {

    private final Map<Long, Product> products = new HashMap<>();
    private Long nextId = 4L; // Start IDs at 4

    public ProductService() {

        products.put(1L, new Product(1L, "Laptop", 1200.00));
        products.put(2L, new Product(2L, "Mouse", 25.00));
        products.put(3L, new Product(3L, "Keyboard", 50.00));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Product getProductById(Long id) {
        return products.get(id);
    }

    /**
     * FIX: This method must accept a Product object, not String and double,
     * to match the ProductResolver's invocation.
     */
    public Product addProduct(Product product) {
        product.setId(nextId++);
        products.put(product.getId(), product);
        return product;
    }

    public Product updateProduct(Long id, Product updatedDetails) {
        if (products.containsKey(id)) {
            updatedDetails.setId(id);
            products.put(id, updatedDetails);
            return updatedDetails;
        }
        return null;
    }

    public void deleteProduct(Long id) {
        products.remove(id);
    }
}