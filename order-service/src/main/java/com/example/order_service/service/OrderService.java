package com.example.order_service.service;

import com.example.order_service.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.example.order_service.dto.Inventory;
import com.example.order_service.dto.InventoryUpdate;
import com.example.order_service.dto.OrderUpdate;
import com.example.order_service.dto.Product;
import com.example.order_service.model.Order;
import com.example.order_service.model.LineItem;
import com.example.order_service.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Order addOrder(Order cart) throws CustomException {

        List<LineItem> lineItems = cart.getLineItems();

        for(LineItem lineItem: lineItems){
        	addingLineItem(lineItem);
        }

        return orderRepository.save(cart);
    }

    public void deleteOrder(int cartId) throws CustomException {
        Optional<Order> cart = orderRepository.findById(cartId);
        
        if(cart.isPresent()){
        	
        	List<LineItem> lineItems = cart.get().getLineItems();
        	
        	for(LineItem lineItem: lineItems) {
        		deleteLineItem(lineItem);
        	}
        	
            orderRepository.delete(cart.get());
        }else{
            throw new CustomException("Order is not present",HttpStatus.NOT_FOUND);
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

                String resourceUrl = "lb://INVENTORYSERVICE/inventory/" + havequantity.getInventoryId();
                HttpEntity<InventoryUpdate> requestUpdate = new HttpEntity<>(inventoryUpdate, headers);
                restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, Void.class);
            }catch (RestClientResponseException ex){
                throw new CustomException("Unable to update the Inventory", HttpStatus.BAD_REQUEST);
            }
        }
	}

	public Order updateOrder(int orderId, OrderUpdate orderUpdate) throws CustomException {
        Optional<Order> findCart = orderRepository.findById(orderId);
        if(findCart.isPresent()){

            List<LineItem> lineItems = findCart.get().getLineItems();

            for(LineItem item: orderUpdate.getLineItems()){
                lineItems.add(item);
                addingLineItem(item);
            }

        }else{
            throw new CustomException("Order Id not valid", HttpStatus.NOT_FOUND);
        }
        return null;
    }

    public Order searchOrder(int orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        return order.orElse(null);
    }
    
    public void addingLineItem(LineItem lineItem) throws CustomException {
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
 
            int quantity = havequantity.getQuantity() - lineItem.getQuantity();
            
            System.out.println(quantity);
            

            InventoryUpdate inventoryUpdate = new InventoryUpdate();
            inventoryUpdate.setProductId(havequantity.getProductId());
            inventoryUpdate.setQuantity(quantity);

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                String resourceUrl = "lb://INVENTORYSERVICE/inventory/" + havequantity.getInventoryId();
                HttpEntity<InventoryUpdate> requestUpdate = new HttpEntity<>(inventoryUpdate, headers);
                restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, Void.class);
            }catch (RestClientResponseException ex){
                throw new CustomException("Unable to update the Inventory", HttpStatus.BAD_REQUEST);
            }
        }
    }

}
