package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.entity.Product;
import com.example.ecommerce_backend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;


    @PostMapping()
    public Product createProduct(@RequestBody Product productEntity){
        return productRepository.save(productEntity);
    }

    @GetMapping()
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/id")
    public Product getProductById(@PathVariable Long id){
        return productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
    }

}
