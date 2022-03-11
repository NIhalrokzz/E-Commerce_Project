package com.example.inventory_service.service;

import java.util.Optional;

import com.example.inventory_service.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.example.inventory_service.dto.InventoryUpdate;
import com.example.inventory_service.dto.Product;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;

import org.springframework.http.ResponseEntity;

@Service
@Transactional
public class InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	
	public Inventory addInventory(Inventory inventory) throws CustomException {

		try {
			ResponseEntity<Product> response = restTemplate.getForEntity("http://catalogservice/product/{productId}", Product.class, inventory.getProductId());

			Optional<Inventory> inventoryfound = inventoryRepository.findByProductId(inventory.getProductId());

			if(inventoryfound.isPresent()) {
				int quantity = inventoryfound.get().getQuantity() + inventory.getQuantity();
				inventoryfound.get().setQuantity(quantity);
				return inventoryRepository.save(inventoryfound.get());
			}

			System.out.println(response);
			if(response.getBody() != null){
				return inventoryRepository.save(inventory);
			}
		}catch (RestClientResponseException ex){
			throw new CustomException("Invalid Product ID", HttpStatus.BAD_REQUEST);
		}
		
		return null;
	}
	
	public Inventory updateInventory(InventoryUpdate inventoryUpdate, int inventoryId) {
		if(inventoryRepository.findById(inventoryId).isPresent()) {
			Inventory inventory = inventoryRepository.findById(inventoryId).get();
			
			inventory.setProductId(inventoryUpdate.getProductId());
			inventory.setQuantity(inventoryUpdate.getQuantity());
			
			inventoryRepository.save(inventory);
			return inventory;
		}
		
		return null;
	}
	
	public void deleteInventory(int inventoryId) throws CustomException {
		if(inventoryRepository.findById(inventoryId).isPresent()) {
			Inventory inventory = inventoryRepository.findById(inventoryId).get();
			inventoryRepository.delete(inventory);
		}else{
			throw new CustomException("Inventory is to present", HttpStatus.NOT_FOUND);
		}
	}
	
	public Inventory searchInventory(int inventoryId) {
		if(inventoryRepository.findById(inventoryId).isPresent()) {
			Inventory inventory = inventoryRepository.findById(inventoryId).get();
			
			return inventory;
		}
		return null;
	}
	
	public Inventory searchByProductId(int productId) {
		if(inventoryRepository.findByProductId(productId).isPresent()) {
			Inventory inventory = inventoryRepository.findByProductId(productId).get();
			
			return inventory;
		}
		return null;
	}
}
