package com.kipps.billingsystem.service;

import com.kipps.billingsystem.entity.Product;
import com.kipps.billingsystem.exception.ProductNotFoundException;
import com.kipps.billingsystem.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                "Product not found with id: " + id
        ));

        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setGstPercentage(product.getGstPercentage());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setUnit(product.getUnit());

        return productRepository.save(existingProduct);
    }

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product){
        return productRepository.save(product);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id: " + id
                ));
    }


    public String deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                "MODI its Done bro its done " + id
        ));

        productRepository.delete(existingProduct);
        return "done";
    }

    public List<Product> searchProductByName(String name){
        return productRepository.findByNameContainingIgnoreCase(name);
    }


    public Product patchProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id: " + id));

        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getCategory() != null) {
            existingProduct.setCategory(product.getCategory());
        }
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getGstPercentage() != null) {
            existingProduct.setGstPercentage(product.getGstPercentage());
        }
        if (product.getStockQuantity() != null) {
            existingProduct.setStockQuantity(product.getStockQuantity());
        }
        if (product.getUnit() != null) {
            existingProduct.setUnit(product.getUnit());
        }

        return productRepository.save(existingProduct);
    }

}
