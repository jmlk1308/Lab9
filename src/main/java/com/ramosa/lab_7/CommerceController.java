package com.ramosa.lab_7;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/commerce")
public class CommerceController {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;

    public CommerceController(CustomerRepository customerRepository,
                              InvoiceRepository invoiceRepository,
                              ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PostMapping("/invoice")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest request) {
        try {
            // Check if customer exists
            Customer customer = customerRepository.findById(request.customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + request.customerId));

            // Check if products exist
            List<Product> products = productRepository.findAllById(request.productIds);

            // Verify all requested products were found
            if (products.size() != request.productIds.size()) {
                return ResponseEntity.badRequest()
                        .body("Some products not found. Requested: " +
                                request.productIds.size() + ", Found: " + products.size());
            }

            // Create and save invoice
            Invoice invoice = new Invoice();
            invoice.setCustomer(customer);
            invoice.setProducts(products);
            invoice.setDate(LocalDate.now());

            Invoice savedInvoice = invoiceRepository.save(invoice);
            return ResponseEntity.ok(savedInvoice);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // Helper class for the invoice request body
    public static class InvoiceRequest {
        public Long customerId;
        public List<Long> productIds;
    }
}