package com.ramosa.lab_7.graphql;

import com.ramosa.lab_7.Product;
import com.ramosa.lab_7.ProductService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * GraphQL Resolver for Product entity.
 * This class maps the GraphQL queries and mutations defined in schema.graphqls
 * to methods in the ProductService.
 */
@Controller
public class ProductControllerGraphql {

    private final ProductService productService;

    /**
     * Constructor for dependency injection of the ProductService.
     * @param productService The service layer for product operations.
     */
    public ProductControllerGraphql(ProductService productService) {
        this.productService = productService;
    }

    // --- Query Mappings (Reads) ---

    /**
     * Fetches all products. Corresponds to 'allProducts' in the schema.
     * @return A list of all available products.
     */
    @QueryMapping
    public List<Product> allProducts() {
        return productService.getAllProducts();
    }

    /**
     * Fetches a single product by its ID. Corresponds to 'productById(id: ID!)' in the schema.
     * @param id The ID of the product to fetch.
     * @return The found Product object, or null if not found.
     */
    @QueryMapping
    public Product productById(@Argument Long id) {
        return productService.getProductById(id);
    }

    // --- Mutation Mappings (Writes) ---

    /**
     * Adds a new product. Corresponds to 'addProduct(product: ProductInput!)' in the schema.
     * @param product A Map containing the 'name' (String) and 'price' (Float) fields from the ProductInput.
     * @return The newly created Product object, including its generated ID.
     */
    @MutationMapping
    public Product addProduct(@Argument Map<String, Object> product) {
        String name = (String) product.get("name");
        // GraphQL Float maps to Java Double. Use Number to safely handle potential integer inputs.
        Double price = ((Number) product.get("price")).doubleValue();

        Product newProduct = new Product(null, name, price);
        return productService.addProduct(newProduct);
    }

    /**
     * Updates an existing product. Corresponds to 'updateProduct(id: ID!, product: ProductInput!)' in the schema.
     * @param id The ID of the product to update.
     * @param product A Map containing the new 'name' (String) and 'price' (Float) fields.
     * @return The updated Product object, or null if the original product was not found.
     */
    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument Map<String, Object> product) {
        String name = (String) product.get("name");
        Double price = ((Number) product.get("price")).doubleValue();

        // The ProductService implementation replaces the entire product, so we create a new Product object
        Product updatedDetails = new Product(null, name, price);
        return productService.updateProduct(id, updatedDetails);
    }

    /**
     * Deletes a product by ID. Corresponds to 'deleteProduct(id: ID!)' in the schema.
     * @param id The ID of the product to delete.
     * @return True if the product was found and deleted, False otherwise.
     */
    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        // Check if the product exists before attempting to delete (for the Boolean return type)
        Product product = productService.getProductById(id);
        if (product != null) {
            productService.deleteProduct(id);
            return true;
        }
        return false;
    }
}