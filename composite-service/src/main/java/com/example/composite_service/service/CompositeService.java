package com.example.composite_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.composite_service.model.OrderId;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.example.composite_service.dto.Cart;
import com.example.composite_service.dto.CartUpdate;
import com.example.composite_service.dto.Customer;
import com.example.composite_service.dto.CustomerRequest;
import com.example.composite_service.dto.Inventory;
import com.example.composite_service.dto.LineItem;
import com.example.composite_service.dto.Order;
import com.example.composite_service.dto.Product;
import com.example.composite_service.dto.ProductRequest;
import com.example.composite_service.exception.CustomException;
import com.example.composite_service.model.CustomerCart;
import com.example.composite_service.model.CustomerOrder;
import com.example.composite_service.repository.CustomerCartRepository;
import com.example.composite_service.repository.CustomerOrderRepository;
import com.example.composite_service.repository.OrderIdRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CompositeService {

    public static final String COMPOSITE_SERVICE= "composite_service";
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerCartRepository customerCartRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private OrderIdRepository orderIdRepository;

    @CircuitBreaker(name = COMPOSITE_SERVICE, fallbackMethod = "saveProductFallback")
    public String saveProduct(ProductRequest productRequest) throws CustomException {
    	
    	System.out.println("********************************Starting saving the product****************");
    	
        Product product = new Product(productRequest.getProductName(), productRequest.getProductDescription(),productRequest.getProductPrice());
        
        try {
        	ResponseEntity<Product> result = restTemplate.postForEntity("http://catalogservice/products", product, Product.class);
        	
        	
        	System.out.println(result);
            
            Inventory inventory = new Inventory(Objects.requireNonNull(result.getBody()).getProductId(), productRequest.getQuantity());
            
            try {
            	ResponseEntity<Inventory> inventoryResponseEntity = restTemplate.postForEntity("http://inventoryservice/inventory", inventory, Inventory.class);
            	
            	System.out.println(inventoryResponseEntity);
            	
            	if(result.getStatusCodeValue() == 201 && inventoryResponseEntity.getStatusCodeValue() == 201){
                    return "Success";
                }else if(result.getStatusCodeValue() != 201){
                    throw new CustomException("Unable to create product", HttpStatus.BAD_REQUEST);
                }else if(inventoryResponseEntity.getStatusCodeValue() != 201){
                    throw new CustomException("Unable to create Inventory", HttpStatus.BAD_REQUEST);
                }
            	
            	
            }catch (RestClientResponseException ex){
                throw new CustomException("Unable to post Inventory information", HttpStatus.BAD_REQUEST);
            }
            
        }catch (RestClientResponseException ex){
            throw new CustomException("Unable to post product information", HttpStatus.BAD_REQUEST);
        }
        
        return "Failure";
    }

    public String saveProductFallback(Exception ex){
        return "Failure";
    }


    @CircuitBreaker(name = COMPOSITE_SERVICE, fallbackMethod = "saveCustomerFallback")
    public Customer saveCustomer(CustomerRequest customer) throws CustomException {

        CustomerCart customerCart = new CustomerCart();

//        Check is the customer exists
        
        

        try {
            ResponseEntity<Customer> customerResponseEntity = restTemplate.getForEntity("http://customerservice/customerByEmail/{customerEmail}", Customer.class, customer.getCustomer().getCustomerEmail());
            
            System.out.println(customerResponseEntity);
            return customerResponseEntity.getBody();
        }catch (RestClientResponseException ex){
        	
            try {
            	ResponseEntity<Customer> createCustomer = restTemplate.postForEntity("http://customerservice/customer", customer, Customer.class);
                
                System.out.println(createCustomer);
                
                int customerId = createCustomer.getBody().getId();

                ResponseEntity<Cart> createCart = null;

                try{
                	
                	List<LineItem> lt = new ArrayList<>();
                	Cart cart = new Cart();
                	
                	cart.setLineItems(lt);
                	
                    createCart = restTemplate.postForEntity("http://cartservice/cart", cart, Cart.class);
                    System.out.println(createCart.getStatusCodeValue());
                    int cartId = createCart.getBody().getCartId();
                    
                    

                    customerCart.setCustomerId(customerId);
                    customerCart.setCartId(cartId);
                    
                    System.out.println(customerCart);

                    customerCartRepository.save(customerCart);

                }catch (RestClientResponseException ex1){
                    throw new CustomException("Unable to create the Cart", HttpStatus.BAD_REQUEST);
                }

                return createCustomer.getBody();
            }catch (RestClientResponseException ex1){
                throw new CustomException("Unable to create the customer",HttpStatus.BAD_REQUEST);
            }
        }
    }

    public Customer saveCustomerFallback(Exception ex){
        Customer customer = new Customer();
        customer.setCustomerName("Dummy");
        customer.setCustomerEmail("dummy@dummy.com");
        customer.setCustomerBillingAddress(null);
        customer.setCustomerShippingAddress(null);

        return  customer;
    }

    @CircuitBreaker(name = COMPOSITE_SERVICE, fallbackMethod = "placeOrderFallback")
    public Order placeOrder(int customerId) throws CustomException {
    	
    	System.out.println("******************************** Placing Orders ********************************");
    	
        Optional<CustomerCart> customerCart = customerCartRepository.findById(customerId);
        
        System.out.println(customerCart);

        if(customerCart.isPresent()){
            int cartId = customerCart.get().getCartId();
            
            System.out.println(cartId);

            try {
                Cart cart = restTemplate.getForObject("http://cartservice/cart/{cartId}", Cart.class, cartId);

                List<LineItem> items = cart.getLineItems();
                
                System.out.println(cart);
                
                Order order = new Order();
                
                order.setLineItems(items);
                
                System.out.println(order);
                
                for(LineItem item: items){
                    try {
                        String url = "http://cartservice/cart/" + cartId + "/item/" + item.getItemId();
                        System.out.println(url);
                        restTemplate.delete(url);
                    }catch (RestClientResponseException ex){
                        throw new CustomException("Unable to remove item from cart",HttpStatus.BAD_REQUEST);
                    }
                }

                try {

                    for(LineItem item: order.getLineItems()){
                        item.setItemId(0);
                    }

                    ResponseEntity<Order> ord = restTemplate.postForEntity("http://orderservice/order", order, Order.class);
                    
                    System.out.println(ord);

                    int orderId = Objects.requireNonNull(ord.getBody()).getOrderId();

                    System.out.println(orderId);
                    
                    Optional<CustomerOrder> custorder = customerOrderRepository.findById(customerId);
                    
                    if(custorder.isPresent()) {
                    	CustomerOrder customerOrder = custorder.get();
                    	if(customerOrder.getOrderId() == null){
                            List<OrderId> orders = new ArrayList<>();
                            OrderId orderId1 = new OrderId();
                            orderId1.setOrderId(orderId);
                            orders.add(orderId1);
                            customerOrder.setOrderId(orders);
                        }else{
                            List<OrderId> orders = customerOrder.getOrderId();
                            OrderId orderId1 = new OrderId();
                            orderId1.setOrderId(orderId);
                            orders.add(orderId1);
                            customerOrder.setOrderId(orders);
                        }
                    	
                    	 System.out.println(customerOrder);

                         customerOrderRepository.save(customerOrder);
                    	
                    	
                    }else {
                    	CustomerOrder customerOrder = new CustomerOrder();
                        customerOrder.setCustomerId(customerId);

                        if(customerOrder.getOrderId() == null){
                            List<OrderId> orders = new ArrayList<>();
                            OrderId orderId1 = new OrderId();
                            orderId1.setOrderId(orderId);
                            orders.add(orderId1);
                            customerOrder.setOrderId(orders);
                        }else{
                            List<OrderId> orders = customerOrder.getOrderId();
                            OrderId orderId1 = new OrderId();
                            orderId1.setOrderId(orderId);
                            orders.add(orderId1);
                            customerOrder.setOrderId(orders);
                        }
                        
                        System.out.println(customerOrder);

                        customerOrderRepository.save(customerOrder);
                    }
                   
                    
                    
                    return ord.getBody();
                }catch (RestClientResponseException ex){
                    throw new CustomException("Unable to create Order",HttpStatus.BAD_REQUEST);
                }
            }catch (RestClientResponseException ex){
                throw new CustomException("Unable to find the cart", HttpStatus.NOT_FOUND);
            }
        }else{
            throw new CustomException("No data present for this customer",HttpStatus.BAD_REQUEST);
        }
    }

    public Order placeOrderFallback(Exception ex){
        Order order = new Order();
        order.setLineItems(null);
        return order;
    }

    @CircuitBreaker(name = COMPOSITE_SERVICE, fallbackMethod = "getCartInfoFallback")
    public Cart updateCartInfo(int customerId, CartUpdate cartUpdate) throws CustomException {
    	
    	System.out.println("++++++++++++++ Adding element to cart ++++++++++++");
    	
    	System.out.println(customerCartRepository.findById(customerId).get());
    	
        Optional<CustomerCart> customerCart = customerCartRepository.findById(customerId);
        
        for(LineItem item: cartUpdate.getLineItems()) {
        	System.out.println(item);
        }
        
        if(customerCart.isPresent()){
            int cartId = customerCart.get().getCartId();

            try {
            	
            	Cart cart = new Cart();
            	
            	cart.setCartId(cartId);
            	
            	System.out.println(cart);
            	
            	List<LineItem> item1 = new ArrayList<>();
            	
            	for(LineItem item: cartUpdate.getLineItems()) {
            		System.out.println(item);
            		item1.add(item);
            	}
            	
            	cart.setLineItems(item1);
            	
            	System.out.println(cart);

            	for(LineItem item: cart.getLineItems()) {
            		System.out.println(item);
            	}
            	
            	try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    String resourceUrl = "http://cartservice/cart/"+cartId+"/item";
                    
                    System.out.println(resourceUrl);
                    
                    HttpEntity<CartUpdate> requestUpdate = new HttpEntity<>(cartUpdate, headers);

                    System.out.println(requestUpdate);
                    
                    restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, Void.class);
                }catch (RestClientResponseException ex){
                    throw new CustomException("Unable to update the Cart", HttpStatus.BAD_REQUEST);
                }
            	
            	return cart;
            }catch (RestClientResponseException ex){
                throw new CustomException("No data found",HttpStatus.NOT_FOUND);
            }
        }else{
            throw new CustomException("No data found for this customer",HttpStatus.NOT_FOUND);
        }
    }

    public Cart getCartInfoFallback(Exception ex){
        Cart cart = new Cart();
        cart.setLineItems(null);

        return cart;
    }

    @CircuitBreaker(name = COMPOSITE_SERVICE, fallbackMethod = "getAllOrderFallback")
    public List<Order> getAllOrder(int customerId) throws CustomException {

        System.out.println("********************************** Get All orders **************************");

        Optional<CustomerOrder> customerCart = customerOrderRepository.findById(customerId);

        System.out.println(customerCart);

        List<Order> orders = new ArrayList<>();



        if(customerCart.isPresent()){
            List<OrderId> orderIds = customerCart.get().getOrderId();

            System.out.println(orderIds);

            try {
                for (OrderId i : orderIds) {

                    System.out.println(i);

                    int id = i.getOrderId();

                    System.out.println(id);

                    Order ord = restTemplate.getForObject("http://orderservice/order/{orderId}", Order.class, id);

                    System.out.println(ord);

                    orders.add(ord);

                    System.out.println(orders);
                }
                return orders;
            }catch (RestClientResponseException ex) {
                throw new CustomException("No Order record found", HttpStatus.NOT_FOUND);
            }
        }else{
            throw new CustomException("No data found for this customer", HttpStatus.NOT_FOUND);
        }
    }

    public List<Order> getAllOrderFallback(Exception ex){
        return null;
    }
}
