package com.ramosa.lab_7;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/newProduct")
    public Product newProduct(@RequestBody Product product) {
        return service.addProduct(product);
    }

    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
    }


    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        return service.updateProduct(productId, product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return service.getProductById(id);
    }
}