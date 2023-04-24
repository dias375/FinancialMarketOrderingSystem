package com.financial;

public class OrderImpl implements Order {

	private boolean active;
	private String orderId;
	private double price;
	private String productId;
	private double quantity;
	private OrderVerb verb;

	public OrderImpl(String orderId, double quantity, double price, String productId, OrderVerb verb, boolean active) {
		this.active = active;
		this.orderId = orderId;
		this.price = price;
		this.productId = productId;
		this.quantity = quantity;
		this.verb = verb;
	}

	// Feel free to implement additional methods if you need them.

	@Override
	public String getId() {
		return orderId;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public String getProductId() {
		return productId;
	}

	@Override
	public double getQuantity() {
		return quantity;
	}

	@Override
	public OrderVerb getVerb() {
		return verb;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void addNewPrice(double newPrice) {
		if (newPrice > 0)
			this.price = newPrice;
	}

	@Override
	public void addNewQuantity(double newQty) {
		if (newQty > 0)
			this.quantity = newQty;
	}

	@Override
	public void suspend() {
		this.active = false;
	}

	@Override
	public void activate() {
		this.active = true;
	}

	@Override
	public String toString() {
		return new StringBuilder("Order [orderId=")
				.append(orderId)
				.append(", quantity=").append(quantity)
				.append(", price=").append(price)
				.append(", productId=").append(productId)
				.append(", verb=").append(verb)
				.append(", active=").append(active)
				.append("]").toString();
	}
}
