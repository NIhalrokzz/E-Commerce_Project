package com.example.cart_service.service;

import com.example.cart_service.dto.CartUpdate;

import com.example.cart_service.dto.Inventory;
import com.example.cart_service.dto.InventoryUpdate;
import com.example.cart_service.dto.Product;
import com.example.cart_service.exception.CustomException;
import com.example.cart_service.model.Cart;
import com.example.cart_service.model.LineItem;
import com.example.cart_service.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Cart addCart(Cart cart) throws CustomException {

        List<LineItem> lineItems = cart.getLineItems();
        
        System.out.println("This is called");

        for(LineItem lineItem: lineItems){
        	addingLineItem(lineItem);
        }

        return cartRepository.save(cart);
    }

    public void deleteCart(int cartId) throws CustomException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        
        if(cart.isPresent()){
        	
        	List<LineItem> lineItems = cart.get().getLineItems();
        	
        	for(LineItem lineItem: lineItems) {
        		deleteLineItem(lineItem);
        	}
        	
            cartRepository.delete(cart.get());
        }else{
            throw new CustomException("Cart is not present",HttpStatus.NOT_FOUND);
        }
    }

    public void deleteLineItem(LineItem lineItem) throws CustomException {
		// TODO Auto-generated method stub

        Product foundProduct;
        Inventory havequantity;

        try {
            foundProduct = restTemplate.getForObject("http://catalogservice/product/{productId}", Product.class, lineItem.getProductId());
        }catch (RestClientResponseException ex){
            throw new CustomException("Unable to find the Product", HttpStatus.NOT_FOUND);
        }

        if(foundProduct.getProductId() != lineItem.getProductId() || !lineItem.getProductName().equals(foundProduct.getProductName())){
            throw new CustomException("Product mismatching", HttpStatus.CONFLICT);
        }

        try {
            havequantity = restTemplate.getForObject("http://inventoryservice/inventoryByProduct/{productId}", Inventory.class, lineItem.getProductId());
        }catch (RestClientResponseException ex){
            throw new CustomException("Unable to find the Inventory", HttpStatus.NOT_FOUND);
        }

        if(havequantity.getQuantity() <= lineItem.getQuantity()){
            throw new CustomException("Quantity cannot exceed inventory quantity", HttpStatus.CONFLICT);
        }

        System.out.println(foundProduct);
        System.out.println(havequantity);
        
        if(foundProduct != null && havequantity != null){
        	System.out.println(lineItem);
 
            int quantity = havequantity.getQuantity() + lineItem.getQuantity();
            
            System.out.println(quantity);
            

            InventoryUpdate inventoryUpdate = new InventoryUpdate();
            inventoryUpdate.setProductId(havequantity.getProductId());
            inventoryUpdate.setQuantity(quantity);

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                String resourceUrl = "http://inventoryservice/inventory/" + havequantity.getInventoryId();
                HttpEntity<InventoryUpdate> requestUpdate = new HttpEntity<>(inventoryUpdate, headers);
                restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, Void.class);
            }catch (RestClientResponseException ex){
                throw new CustomException("Unable to update the Inventory", HttpStatus.BAD_REQUEST);
            }
        }
	}

	public Cart updateCart(int cartId, CartUpdate cartUpdate) throws CustomException {

        Optional<Cart> findCart = cartRepository.findById(cartId);
        if(findCart.isPresent()){
        	

            List<LineItem> line = findCart.get().getLineItems();
            
            if(line.isEmpty()) {
            	line.addAll(cartUpdate.getLineItems());
            	findCart.get().setLineItems(line);
            }else {
            
            System.out.println(line);

            for (LineItem line1 : line) {
				for (LineItem line2 : cartUpdate.getLineItems()) {
					if (line1.getProductId() == line2.getProductId()) {
						addingLineItem(line2);
						int quantity = line1.getQuantity() + line2.getQuantity();
						line1.setQuantity(quantity);
					} else {
						line.add(line2);
						addingLineItem(line2);
					}
				}
			}
            
            findCart.get().setLineItems(line);
            }
            return cartRepository.save(findCart.get());
            
            

        }else{
            throw new CustomException("Cart Id not valid", HttpStatus.NOT_FOUND);
        }
    }

    public Cart searchCart(int orderId){
        Optional<Cart> cart = cartRepository.findById(orderId);
        return cart.orElse(null);
    }
    
    public void addingLineItem(LineItem lineItem) throws CustomException {

        Product foundProduct;
        Inventory havequantity;

        try {
            foundProduct = restTemplate.getForObject("http://catalogservice/product/{productId}", Product.class, lineItem.getProductId());
        }catch (RestClientResponseException ex){
            throw new CustomException("Product not found", HttpStatus.NOT_FOUND);
        }
        System.out.println(foundProduct);

        if(foundProduct.getProductId() != lineItem.getProductId() || !lineItem.getProductName().equals(foundProduct.getProductName())){
            throw new CustomException("Product mismatching", HttpStatus.CONFLICT);
        }

        try {
            havequantity = restTemplate.getForObject("http://inventoryservice/inventoryByProduct/{productId}", Inventory.class, lineItem.getProductId());
        }catch (RestClientResponseException ex){
            throw new CustomException("Inventory not found", HttpStatus.NOT_FOUND);
        }

        System.out.println(havequantity);

        if(havequantity.getQuantity() <= lineItem.getQuantity()){
            throw new CustomException("Quantity cannot exceed inventory quantity", HttpStatus.CONFLICT);
        }
        
        if(foundProduct != null && havequantity != null){
        	System.out.println(lineItem);
 
            int quantity = havequantity.getQuantity() - lineItem.getQuantity();
            
            System.out.println(quantity);
            

            InventoryUpdate inventoryUpdate = new InventoryUpdate();
            inventoryUpdate.setProductId(havequantity.getProductId());
            inventoryUpdate.setQuantity(quantity);

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String resourceUrl = "http://inventoryservice/inventory/" + havequantity.getInventoryId();
                HttpEntity<InventoryUpdate> requestUpdate = new HttpEntity<>(inventoryUpdate, headers);
                restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, Void.class);
            }catch (RestClientResponseException ex){
                throw new CustomException("Unable to update the Inventory", HttpStatus.BAD_REQUEST);
            }
        }
    }

}
