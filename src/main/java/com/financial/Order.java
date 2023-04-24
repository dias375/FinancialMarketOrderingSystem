package com.financial;

/**
 * An Order represents the intention to buy or sell a given quantity of a certain product at a specified price.
 */
public interface Order {

	/**
	 * @return The order's unique identifier
	 */
	String getId();

	/**
	 * @return The order's price
	 */
	double getPrice();

	/**
	 * @return The ID of the order's product
	 */
	String getProductId();

	/**
	 * @return The order's quantity
	 */
	double getQuantity();

	/**
	 * @return The order's verb
	 */
	OrderVerb getVerb();

	/**
	 * @return {@code true} if the order is active, or {@code false} if it is suspended
	 */
	boolean isActive();

	void addNewPrice(double newPrice);
	void addNewQuantity(double newQty);

	void suspend();
	void activate();
}
