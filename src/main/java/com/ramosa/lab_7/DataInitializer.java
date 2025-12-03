package com.ramosa.lab_7;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(ProductRepository productRepository,
                           CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize some sample products if database is empty
        if (productRepository.count() == 0) {
            Product p1 = new Product("Laptop", 999.99);
            Product p2 = new Product("Mouse", 29.99);
            Product p3 = new Product("Keyboard", 59.99);
            Product p4 = new Product("Monitor", 199.99);
            Product p5 = new Product("Headphones", 89.99);

            productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
            System.out.println("Initialized 5 sample products");
        }

        // Initialize a sample customer if database is empty
        if (customerRepository.count() == 0) {
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Doe");
            customerRepository.save(customer);
            System.out.println("Initialized sample customer");
        }
    }
}