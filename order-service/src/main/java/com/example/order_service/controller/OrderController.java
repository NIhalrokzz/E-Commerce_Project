package com.example.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.dto.OrderUpdate;
import com.example.order_service.exception.CustomException;
import com.example.order_service.model.LineItem;
import com.example.order_service.model.Order;
import com.example.order_service.service.LineItemService;
import com.example.order_service.service.OrderService;

@RestController
public class OrderController {

    @Autowired
    private OrderService cartService;

    @Autowired
    private LineItemService lineItemService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getCart(@PathVariable("orderId") int orderId) throws CustomException {
        if(orderId < 0){
            throw new CustomException("Order Id can't be negative", HttpStatus.BAD_REQUEST);
        }
        Order order = cartService.searchOrder(orderId);

        if(order == null){
            throw new CustomException("Unable to find the order",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Order>(order, HttpStatus.FOUND);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> addCart(@RequestBody Order order) throws CustomException {
        Order order1 = cartService.addOrder(order);
        
        if(order1 == null) {
        	throw new CustomException("Unable create the order", HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<Order>(order1, HttpStatus.CREATED);
    }

    @DeleteMapping("/order/{orderId}")
    public void deleteCart(@PathVariable("orderId") int orderId) throws CustomException {

        if(orderId < 0){
            throw new CustomException("Order Id can't be negative", HttpStatus.BAD_REQUEST);
        }
        cartService.deleteOrder(orderId);
    }
    
    @PutMapping("/order/{orderId}/item")
    public ResponseEntity<Order> addLineItem(@PathVariable("orderId") int orderId, @RequestBody OrderUpdate itemId ) throws CustomException {

        if(orderId < 0){
            throw new CustomException("Order Id can't be negative", HttpStatus.BAD_REQUEST);
        }

    	lineItemService.addLineItem(orderId, itemId);
    	
    	Order order = cartService.searchOrder(orderId);

        if(order == null){
            throw new CustomException("Unable to find the order",HttpStatus.NOT_FOUND);
        }

		return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
    
    @DeleteMapping("/order/{orderId}/item/{itemId}")
    public ResponseEntity<Order> deleteItem(@PathVariable("orderId") int orderId, @PathVariable("itemId") int itemId) throws CustomException {

        if(orderId < 0){
            throw new CustomException("Order Id can't be negative", HttpStatus.BAD_REQUEST);
        }

        if(itemId < 0){
            throw new CustomException("Item Id can't be negative", HttpStatus.BAD_REQUEST);
        }

        lineItemService.deleteLineItem(orderId, itemId);
    	
    	Order order = cartService.searchOrder(orderId);

        for(LineItem item: order.getLineItems()){
            if(item.getItemId() == itemId){
                throw new CustomException("Unable to delete the line item", HttpStatus.CONFLICT);
            }
        }

    	return new ResponseEntity<Order>(order,HttpStatus.OK);
    }
    
    @GetMapping("/order/{orderId}/item/{itemId}")
    public ResponseEntity<LineItem> searchItem(@PathVariable("orderId") int orderId, @PathVariable("itemId") int itemId) throws CustomException {
        if(orderId < 0){
            throw new CustomException("Order Id can't be negative", HttpStatus.BAD_REQUEST);
        }
        if(itemId < 0){
            throw new CustomException("Item Id cannot be negative", HttpStatus.BAD_REQUEST);
        }

        LineItem lineItem = lineItemService.searchLineItem(orderId,itemId);

        if(lineItem == null){
            throw new CustomException("Unable to find the line Item", HttpStatus.NOT_FOUND);
        }

    	return new ResponseEntity<LineItem>(lineItem, HttpStatus.OK);
    }
}
