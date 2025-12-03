package com.ramosa.lab_7;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "products") // Explicit table name
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    // Inverse side of Many-to-Many relationship
    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<Invoice> invoices;

    // Constructor for creating new products (ID auto-generated)
    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}