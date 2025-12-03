package com.ramosa.lab_7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramosa.lab_7.entity.Product;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <--- CRITICAL FIX: Enables ordering
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. Test GET ALL (Runs FIRST: expects "Laptop")
    @Test
    @Order(1)
    public void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].name", is("Laptop")));
    }

    // 2. Test CREATE (Runs SECOND)
    @Test
    @Order(2)
    public void shouldCreateNewProduct() throws Exception {
        Product newProd = new Product(null, "Test Camera", 500.0);

        mockMvc.perform(post("/api/products/newProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Camera")));
    }

    // 3. Test UPDATE (Runs THIRD: Changes "Laptop" to "Updated Laptop")
    @Test
    @Order(3)
    public void shouldUpdateProduct() throws Exception {
        Product updateInfo = new Product(null, "Updated Laptop", 9999.0);

        mockMvc.perform(put("/api/products/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Laptop")));
    }

    // 4. Test DELETE (Runs LAST)
    @Test
    @Order(4)
    public void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/delete/2"))
                .andExpect(status().isOk());
    }
}