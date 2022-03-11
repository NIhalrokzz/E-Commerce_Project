package com.example.inventory_service.controller;

import com.example.inventory_service.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.service.InventoryService;
import com.example.inventory_service.dto.InventoryUpdate;

@RestController
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping("/inventory/{id}")
	public ResponseEntity<Inventory> searchInventory(@PathVariable("id") int inventoryId) throws CustomException {

		if(inventoryId <= 0){
			throw new CustomException("Please enter a valid Inventory ID", HttpStatus.BAD_REQUEST);
		}
		Inventory inventory = inventoryService.searchInventory(inventoryId);

		if(inventory != null){
			return new ResponseEntity<Inventory>(inventory, HttpStatus.OK);
		}
		throw new CustomException("No Inventory found for this ID", HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/inventory")
	public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) throws CustomException {
		if(inventory.getQuantity() < 0){
			throw new CustomException("Quantity can not be less than 0", HttpStatus.BAD_REQUEST);
		}
		Inventory invent = inventoryService.addInventory(inventory);
		if(invent == null){
			throw new CustomException("Error occured while creating Inventory", HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<Inventory>(invent, HttpStatus.CREATED);
	}
	
	@PutMapping("/inventory/{id}")
	public ResponseEntity<Inventory> updateInventory(@PathVariable("id") int inventoryId, @RequestBody InventoryUpdate inventoryUpdate) throws CustomException {

		if(inventoryId < 0){
			throw new CustomException("Inventory Id cannot be less than 0", HttpStatus.BAD_REQUEST);
		}

		if(inventoryUpdate.getQuantity() < 0){
			throw new CustomException("Quantity can not be less than 0", HttpStatus.BAD_REQUEST);
		}

		Inventory inventory = inventoryService.updateInventory(inventoryUpdate, inventoryId);
		if(inventory == null){
			throw new CustomException("Inventory is not present", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Inventory>(inventory, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/inventory/{id}")
	public void deleteInventory(@PathVariable("id") int inventoryId) throws CustomException {
		if(inventoryId < 0){
			throw new CustomException("Inventory Id cannot be less than 0", HttpStatus.BAD_REQUEST);
		}
		inventoryService.deleteInventory(inventoryId);
	}
	
	@GetMapping("/inventoryByProduct/{productId}")
	public ResponseEntity<Inventory> searchByProductId(@PathVariable("productId") int productId) throws CustomException {
		Inventory inventory = inventoryService.searchByProductId(productId);

		if(inventory == null){
			throw new CustomException("Inventory is not present", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Inventory>(inventory, HttpStatus.OK);
	}
}
