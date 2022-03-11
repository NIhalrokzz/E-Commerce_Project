package com.example.cart_service.service;

import java.util.List;

import java.util.Optional;

import com.example.cart_service.dto.CartUpdate;
import com.example.cart_service.exception.CustomException;

import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cart_service.model.Cart;
import com.example.cart_service.model.LineItem;
import com.example.cart_service.repository.CartRepository;
import com.example.cart_service.repository.LineItemRepository;

import io.swagger.v3.oas.models.PathItem.HttpMethod;


@Service
public class LineItemService {

	@Autowired
	private LineItemRepository lineItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartService cartService;

	public void addLineItem(int cartId, CartUpdate cartUpdate) throws CustomException {

		Optional<Cart> cart = cartRepository.findById(cartId);

		if (cart.isPresent()) {
			List<LineItem> line = cart.get().getLineItems();

			if (line.isEmpty()) {
				line.addAll(cartUpdate.getLineItems());
				cart.get().setLineItems(line);
			} else {

				System.out.println(line);

				for (LineItem line1 : line) {
					for (LineItem line2 : cartUpdate.getLineItems()) {
						if (line1.getProductId() == line2.getProductId()) {
							cartService.addingLineItem(line2);
							int quantity = line1.getQuantity() + line2.getQuantity();
							line1.setQuantity(quantity);
						} else {
							line.add(line2);
							cartService.addingLineItem(line2);
						}
					}
				}

				cart.get().setLineItems(line);
			}

			cartRepository.save(cart.get());
		}
	}

	public void deleteLineItem(int cartId, int lineItem) throws CustomException {
		Optional<Cart> cart = cartRepository.findById(cartId);

		if (cart.isPresent()) {

			List<LineItem> line = cart.get().getLineItems();

			Optional<LineItem> lineItem1 = lineItemRepository.findById(lineItem);
			
			if(!lineItem1.isPresent()) {
				throw new CustomException("Item not found", HttpStatus.BAD_REQUEST);
			}

			line.remove(lineItem1.get());

			System.out.println(line);
			cartService.deleteLineItem(lineItem1.get());

			lineItemRepository.delete(lineItem1.get());

			cart.get().setLineItems(line);
		}else {
			throw new CustomException("Cart Not found", HttpStatus.BAD_REQUEST);
		}
	}

	public LineItem updateLineItem() {
		return null;
	}

	public LineItem searchLineItem(int cartId, int lineItem) {
		Optional<Cart> cart = cartRepository.findById(cartId);

		if (cart.isPresent()) {
			List<LineItem> line = cart.get().getLineItems();

			for (LineItem t : line) {
				if (t.getItemId() == lineItem) {
					return t;
				}
			}
		}

		return null;
	}
}
