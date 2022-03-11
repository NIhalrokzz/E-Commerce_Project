package com.example.order_service.service;

import java.util.List;

import java.util.Optional;

import com.example.order_service.dto.OrderUpdate;
import com.example.order_service.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.order_service.model.Order;
import com.example.order_service.model.LineItem;
import com.example.order_service.repository.LineItemRepository;
import com.example.order_service.repository.OrderRepository;

@Service
public class LineItemService {

    @Autowired
    private LineItemRepository lineItemRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderService orderService;

    public LineItem addLineItem(int orderId, OrderUpdate orderUpdate) throws CustomException {
    	
    	Optional<Order> order = orderRepository.findById(orderId);
    	
    	if (order.isPresent()) {
			List<LineItem> line = order.get().getLineItems();

			if (line.isEmpty()) {
				line.addAll(orderUpdate.getLineItems());
				order.get().setLineItems(line);
			} else {

				System.out.println(line);

				for (LineItem line1 : line) {
					for (LineItem line2 : orderUpdate.getLineItems()) {
						if (line1.getProductId() == line2.getProductId()) {
							orderService.addingLineItem(line2);
							int quantity = line1.getQuantity() + line2.getQuantity();
							line1.setQuantity(quantity);
						} else {
							line.add(line2);
							orderService.addingLineItem(line2);
						}
					}
				}

				order.get().setLineItems(line);
			}

			orderRepository.save(order.get());
		}
    	return null;
    }

    public void deleteLineItem(int orderID,int lineItem) throws CustomException {
		Optional<Order> order = orderRepository.findById(orderID);

		if (order.isPresent()) {

			List<LineItem> line = order.get().getLineItems();

			Optional<LineItem> lineItem1 = lineItemRepository.findById(lineItem);
			
			if(!lineItem1.isPresent()) {
				throw new CustomException("Item not found", HttpStatus.BAD_REQUEST);
			}

			line.remove(lineItem1.get());

			System.out.println(line);
			orderService.deleteLineItem(lineItem1.get());

			lineItemRepository.delete(lineItem1.get());

			order.get().setLineItems(line);
		}else {
			throw new CustomException("Cart Not found", HttpStatus.BAD_REQUEST);
		}
    }

    public LineItem updateLineItem() {
    	return null;
    }

    public LineItem searchLineItem(int orderId, int lineItem) {
    	Optional<Order> order = orderRepository.findById(orderId);
    	
    	if(order.isPresent()) {
    		List<LineItem> line = order.get().getLineItems();
    		
    		for(LineItem t: line) {
    			if(t.getItemId() == lineItem) {
    				return t;
    			}
    		}
    	}
    	
    	return null;
    }
}
