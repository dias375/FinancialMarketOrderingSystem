package com.financial;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class FinancialMarketImpl implements FinancialMarket {

	private static Map<String, Order> orderMap;

	public FinancialMarketImpl() {
		orderMap = new HashMap<>();
	}

	@Override
	public String createOrder(String productId, OrderVerb verb, double price, double quantity) {
		String orderId = UUID.randomUUID().toString();
		Order order = new OrderImpl(orderId, quantity, price, productId, verb, true);
		orderMap.put(order.getId(), order);
		return order.getId();
	}

	@Override
	public Optional<Order> getOrder(String orderId) {
		return Optional.ofNullable(orderMap.get(orderId));
	}

	@Override
	public boolean modifyOrder(String orderId, double newPrice, double newQty) {
		Optional<Order> order = getOrder(orderId);
		if (!order.isPresent()) {
			return false;
		}
		order.get().addNewPrice(newPrice);
		order.get().addNewQuantity(newQty);
		return true;
	}

	@Override
	public boolean deleteOrder(String orderId) {
		if (!getOrder(orderId).isPresent()) {
			return false;
		}
		orderMap.remove(orderId);
		return true;
	}

	@Override
	public boolean suspendOrder(String orderId) {
		Optional<Order> order = getOrder(orderId);
		if (!order.isPresent() || !order.get().isActive()) {
			return false;
		}
		order.get().suspend();
		return true;
	}

	@Override
	public boolean activateOrder(String orderId) {
		Optional<Order> order = getOrder(orderId);
		if (!order.isPresent() || order.get().isActive()) {
			return false;
		}
		order.get().activate();
		return true;
	}

	@Override
	public Set<Order> getOrdersAtBestLevel(String productId, OrderVerb verb) {
		List<Order> orders = orderMap.values().stream()
				.filter(Order::isActive)
				.filter(order -> order.getProductId().equals(productId))
				.collect(Collectors.toList());
		List<Order> orderedList;
		if (OrderVerb.SELL.equals(verb)) {
			orderedList = orders.stream()
					.filter(order -> order.getVerb().equals(OrderVerb.SELL))
					.sorted(Comparator.comparingDouble(Order::getPrice))
					.collect(Collectors.toList());
		} else {
			orderedList = orders.stream()
					.filter(order -> order.getVerb().equals(OrderVerb.BUY))
					.sorted(Comparator.comparingDouble(Order::getPrice)
							.reversed())
					.collect(Collectors.toList());
		}
		return orderedList.stream()
				.filter(order -> order.getPrice() == orderedList.get(0).getPrice())
				.collect(Collectors.toSet());
}
}
