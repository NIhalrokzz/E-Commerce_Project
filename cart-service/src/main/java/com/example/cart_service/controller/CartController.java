package com.example.cart_service.controller;

import com.example.cart_service.dto.CartUpdate;

import com.example.cart_service.exception.CustomException;
import com.example.cart_service.model.Cart;
import com.example.cart_service.model.LineItem;
import com.example.cart_service.repository.CartRepository;
import com.example.cart_service.service.CartService;
import com.example.cart_service.service.LineItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private LineItemService lineItemService;

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable("cartId") int cartId) throws CustomException {
        if(cartId < 0){
            throw new CustomException("Cart Id can't be less than 0", HttpStatus.BAD_REQUEST);
        }
        Cart cart = cartService.searchCart(cartId);
        if(cart == null){
            throw new CustomException("No record for this Cart ID : "+ cartId, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PostMapping("/cart")
    public ResponseEntity<Cart> addCart(@RequestBody Cart cart) throws CustomException {
        Cart cart1 = cartService.addCart(cart);
        
        System.out.println(cart1);

        if(cart1 == null){
            throw new CustomException("Unable create the cart", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Cart>(cart1, HttpStatus.CREATED);
    }

    @DeleteMapping("/cart/{cartId}")
    public void deleteCart(@PathVariable("cartId") int cartId) throws CustomException {

        if(cartId < 0){
            throw new CustomException("Cart Id can't be less than 0", HttpStatus.BAD_REQUEST);
        }

        cartService.deleteCart(cartId);
    }
    
    @PutMapping("/cart/{cartId}/item")
    public ResponseEntity<Cart> addLineItem(@PathVariable("cartId") int cartId, @RequestBody CartUpdate itemId ) throws CustomException {

        if(cartId < 0){
            throw new CustomException("Cart Id can't be less than 0", HttpStatus.BAD_REQUEST);
        }

    	lineItemService.addLineItem(cartId, itemId);
    	
    	Cart cart = cartService.searchCart(cartId);

        if(cart == null){
            throw new CustomException("Unable to add new line item", HttpStatus.BAD_REQUEST);
        }
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }
    
    @DeleteMapping("/cart/{cartId}/item/{itemId}")
    public ResponseEntity<Cart> deleteItem(@PathVariable("cartId") int cartId, @PathVariable("itemId") int itemId) throws CustomException {

        if(cartId < 0){
            throw new CustomException("Cart Id cannot be negative", HttpStatus.BAD_REQUEST);
        }
        if(itemId < 0){
            throw new CustomException("Item Id cannot be negative", HttpStatus.BAD_REQUEST);
        }

        lineItemService.deleteLineItem(cartId, itemId);
    	
    	Cart cart = cartService.searchCart(cartId);

        for(LineItem item: cart.getLineItems()){
            if(item.getItemId() == itemId){
                throw new CustomException("Unable to delete lineItem", HttpStatus.BAD_REQUEST);
            }
        }
    	return new ResponseEntity<Cart>(cart,HttpStatus.OK);
    }
    
    @GetMapping("/cart/{cartId}/item/{itemId}")
    public ResponseEntity<LineItem> searchItem(@PathVariable("cartId") int cartId, @PathVariable("itemId") int itemId) throws CustomException {

        if(cartId < 0){
            throw new CustomException("Cart Id cannot be negative", HttpStatus.BAD_REQUEST);
        }
        if(itemId < 0){
            throw new CustomException("Item Id cannot be negative", HttpStatus.BAD_REQUEST);
        }

    	LineItem lineItem = lineItemService.searchLineItem(cartId,itemId);

        if(lineItem == null){
            throw new CustomException("No Data found", HttpStatus.NOT_FOUND);
        }
    	
    	return new ResponseEntity<LineItem>(lineItem, HttpStatus.OK);
    }

    @PutMapping("/cart/{cartId}")
    public ResponseEntity<Cart> updateCart(@PathVariable("cartId") int cartId, @RequestBody CartUpdate cartUpdate) throws CustomException {
        if(cartId < 0){
            throw new CustomException("Cart Id cannot be negative", HttpStatus.BAD_REQUEST);
        }
        Cart cart = cartService.updateCart(cartId, cartUpdate);

        if(cart == null){
            throw new CustomException("Unable update the cart", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }
    
}
