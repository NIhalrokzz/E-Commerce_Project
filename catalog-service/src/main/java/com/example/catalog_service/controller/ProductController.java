package com.example.catalog_service.controller;

import com.example.catalog_service.dto.ProductUpdate;

import com.example.catalog_service.exceptions.CustomException;
import com.example.catalog_service.model.Product;
import com.example.catalog_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) throws Exception{

        if(product.getProductName().length() < 1 || product.getProductDescription().length() < 1){
            throw new CustomException("Please Enter a valid input", HttpStatus.NOT_ACCEPTABLE);
        }

    	Product prod = productService.addProduct(product);

        if(prod == null){
            throw new CustomException("Product is already present in catalog",HttpStatus.FOUND);
        }
    	System.out.println(prod);
        return new ResponseEntity<>(prod, HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> searchProduct(@PathVariable("id") int productId) throws Exception {

        if(productId < 0){
            throw new CustomException("Product Id must be greater than 0",HttpStatus.BAD_REQUEST);
        }

        Product product = productService.findProduct(productId);

        if(product != null){
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        throw new CustomException("No product found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int productId, @RequestBody ProductUpdate productUpdate) throws Exception{

        if(productId < 0){
            throw new CustomException("Product Id must be greater than 0",HttpStatus.BAD_REQUEST);
        }

        if(productUpdate.getProductName().length() < 1 || productUpdate.getProductDescription().length() < 1){
            throw new CustomException("Please Enter a valid input", HttpStatus.NOT_ACCEPTABLE);
        }

        Product product = productService.updateProduct(productId,productUpdate);
        if(product == null){
            throw new CustomException("Error occured while updating the product", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int productId) throws Exception {
        if(productId < 0){
            throw new CustomException("Product Id must be greater than 0",HttpStatus.BAD_REQUEST);
        }

        productService.deleteProduct(productId);

        Product product = productService.findProduct(productId);
        if(product == null){
            return new ResponseEntity<>("Product Deleted SuccessFully", HttpStatus.OK);
        }
        throw new CustomException("Product is not present in Catalog", HttpStatus.BAD_REQUEST);
    }
}
