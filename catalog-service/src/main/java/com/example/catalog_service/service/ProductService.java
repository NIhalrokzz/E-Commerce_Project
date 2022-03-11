package com.example.catalog_service.service;

import java.util.List;

import java.util.Optional;


import com.example.catalog_service.dto.ProductUpdate;
import com.example.catalog_service.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.catalog_service.model.Product;
import com.example.catalog_service.repository.ProductRepository;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

//	@Autowired
//	private RestTemplate restTemplate;
	
	public List<Product> findAllProducts() throws Exception {
		List<Product> prod = productRepository.findAll();
		System.out.println(prod.size());
		return prod;
	}

	public Product findProduct(int productId){
		Optional<Product> product = productRepository.findById(productId);
		if(product.isPresent()){
			return product.get();
		}
		return null;
	}

	public Product addProduct(Product product) {
		Optional<Product> product1 = productRepository.findByProductName(product.getProductName());
		if(product1.isPresent()){
			System.out.println(product1);
			return null;
		}
		return productRepository.save(product);
	}

	public void deleteProduct(int productId) throws Exception {
		if(productRepository.findById(productId).isPresent()) {
			Product product = productRepository.findById(productId).get();
			productRepository.delete(product);
		}else{
			throw new CustomException("Product is not present in Catalog", HttpStatus.BAD_REQUEST);
		}

	}

	public Product updateProduct(int productId,ProductUpdate productUpdate){

		if(productRepository.findById(productId).isPresent()) {
			Product product = productRepository.findById(productId).get();
			product.setProductName(productUpdate.getProductName());
			product.setProductDescription(productUpdate.getProductDescription());
			product.setProductPrice(productUpdate.getProductPrice());

			productRepository.save(product);

			return product;
		}

		return null;
	}
}
