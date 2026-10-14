package com.kipps.billingsystem.cotroller;

import com.kipps.billingsystem.entity.Product;
import com.kipps.billingsystem.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<Product> searchByName(@RequestParam String name){
        return productService.searchProductByName(name);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @PutMapping("/{id}")
    public Product UpdateProduct(@PathVariable("id") Long id,
                                    @RequestBody Product product){
        return productService.updateProduct(id,product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteProduct(id);

    }

    @PatchMapping("/{id}")
    public Product patchProduct(@PathVariable("id") Long id,
                                @RequestBody Product product){
        return productService.patchProduct(id,product);
    }


}