package com.ramosa.lab_7.graphql;

import com.ramosa.lab_7.Customer;
import com.ramosa.lab_7.Invoice;
import com.ramosa.lab_7.Product;
import com.ramosa.lab_7.CustomerRepository;
import com.ramosa.lab_7.InvoiceRepository;
import com.ramosa.lab_7.ProductRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CommerceGraphqlController {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;

    public CommerceGraphqlController(CustomerRepository customerRepository,
                                     InvoiceRepository invoiceRepository,
                                     ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
    }

    // Customer queries
    @QueryMapping
    public List<Customer> allCustomers() {
        return customerRepository.findAll();
    }

    @QueryMapping
    public Customer customerById(@Argument Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    // Invoice queries
    @QueryMapping
    public List<Invoice> allInvoices() {
        return invoiceRepository.findAll();
    }

    @QueryMapping
    public Invoice invoiceById(@Argument Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    // Customer mutation
    @MutationMapping
    public Customer addCustomer(@Argument String firstName, @Argument String lastName) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        return customerRepository.save(customer);
    }

    // Invoice mutation - FIXED VERSION
    @MutationMapping
    public Invoice createInvoice(@Argument Long customerId, @Argument List<Long> productIds) {
        try {
            System.out.println("Creating invoice for customerId: " + customerId + ", productIds: " + productIds);

            // Check if customer exists
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

            // Check if products exist
            List<Product> products = productRepository.findAllById(productIds);

            // Verify all products were found
            if (products.size() != productIds.size()) {
                throw new RuntimeException("Some products not found. Requested " +
                        productIds.size() + " products, but found " + products.size());
            }

            // Create invoice
            Invoice invoice = new Invoice();
            invoice.setCustomer(customer);
            invoice.setProducts(products);
            invoice.setDate(LocalDate.now());

            return invoiceRepository.save(invoice);

        } catch (Exception e) {
            System.err.println("Error creating invoice: " + e.getMessage());
            throw new RuntimeException("Failed to create invoice: " + e.getMessage());
        }
    }
}