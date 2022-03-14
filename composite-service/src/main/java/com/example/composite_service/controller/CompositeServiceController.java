package com.example.composite_service.controller;

import com.example.composite_service.dto.*;

import com.example.composite_service.exception.CustomException;
import com.example.composite_service.model.CustomerOrder;
import com.example.composite_service.service.CompositeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingservice")
public class CompositeServiceController {

//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private CompositeService compositeService;

    @PostMapping("/product")
    public ResponseEntity<String> savingProducts(@RequestBody ProductRequest productRequest) throws CustomException {

        if(productRequest.getProductName().length() == 0 || productRequest.getProductDescription().length() == 0 || productRequest.getQuantity() < 0){
            throw new CustomException("Please provide valid input", HttpStatus.BAD_REQUEST);
        }

        String output = compositeService.saveProduct(productRequest);
        System.out.println(output);
        if(output.equals("Success")){
            return new ResponseEntity<>("Action Successfull", HttpStatus.CREATED);
        }else if(output.equals("Failure")){
            throw new CustomException("Unable to create Product and Inventory", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Something Wrong", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> savingCustomer(@RequestBody CustomerRequest customer) throws CustomException {

        if(customer.getCustomer().getCustomerName().length() == 0 || customer.getCustomer().getCustomerEmail().length() == 0){
            throw new CustomException("Invalid data", HttpStatus.BAD_REQUEST);
        }

        Customer output = compositeService.saveCustomer(customer);
        if(output != null){
            return new ResponseEntity<>(output, HttpStatus.CREATED);
        }
        throw new CustomException("Unable to create customer", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/customer/{customerId}/order")
    public ResponseEntity<Order> placeOrder(@PathVariable("customerId") int customerId) throws CustomException {

        if(customerId < 0){
            throw new CustomException("Customer Id can't be negative", HttpStatus.BAD_REQUEST);
        }

        Order order = compositeService.placeOrder(customerId);

        if(order == null){
            throw new CustomException("No record found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/customer/{customerId}/cart")
    public ResponseEntity<Cart> updateCartDetails(@PathVariable("customerId") int customerId, @RequestBody CartUpdate cartUpdate) throws CustomException {

        if(customerId < 0){
            throw new CustomException("Customer Id can't be negative", HttpStatus.BAD_REQUEST);
        }
        
        System.out.println(cartUpdate);

        Cart cart = compositeService.updateCartInfo(customerId, cartUpdate);

        if(cart == null){
            throw new CustomException("No record found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<List<Order>> getOrdersList(@PathVariable("customerId") int customerId) throws CustomException {

        if(customerId < 0){
            throw new CustomException("Customer Id can't be negative", HttpStatus.BAD_REQUEST);
        }

        List<Order> orders = compositeService.getAllOrder(customerId);

        if(orders == null){
            throw new CustomException("No Record Found",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(orders, HttpStatus.FOUND);
    }
}
