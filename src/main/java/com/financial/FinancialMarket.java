package com.financial;

import java.util.Optional;
import java.util.Set;

public interface FinancialMarket {

	/**
	 * Creates a new order and adds it to the market.
	 *
	 * @param productId The ID of the order's product
	 * @param verb      The order's verb, either {@code BUY} or {@code SELL}
	 * @param price     The order's price, i.e how much is paid/received for each unit bought/sold of this product
	 * @param quantity  The order's quantity, i.e. how many units of product are to be bought or sold
	 * @return The newly-created order's market identifier
	 */
	String createOrder(String productId, OrderVerb verb, double price, double quantity);

	/**
	 * Retrieves an order from the market.
	 *
	 * @param orderId The ID of the order to be retrieved
	 * @return The order with the specified ID if it exists, otherwise the Optional empty value
	 */
	Optional<Order> getOrder(String orderId);

	/**
	 * Changes the quantity and price of an existing order.
	 *
	 * @param orderId  The ID of the order to be modified
	 * @param newPrice The order's new price
	 * @param newQty   The order's new quantity
	 * @return {@code true} if the order was successfully modified, otherwise {@code false}.
	 */
	boolean modifyOrder(String orderId, double newPrice, double newQty);

	/**
	 * Removes an existing order from the market.
	 *
	 * @param orderId  The ID of the order to be deleted
	 * @return {@code true} if the order was successfully deleted, otherwise {@code false}.
	 */
	boolean deleteOrder(String orderId);

	/**
	 * Suspends an active order.
	 *
	 * @param orderId The ID of the order to be suspended
	 * @return {@code true} if the order was successfully suspended, otherwise {@code false}.
	 */
	boolean suspendOrder(String orderId);

	/**
	 * Activates a suspended order.
	 *
	 * @param orderId The ID of the order to be suspended
	 * @return {@code true} if the order was successfully activated, otherwise {@code false}.
	 */
	boolean activateOrder(String orderId);

	/**
	 * Retrieve all active orders at the best level for a given product and verb.
	 *
	 * <p>
	 * An order is considered to be at the best level if its price is the best price amongst active orders
	 * with the same verb. The best price depends on the order's verb:
	 * <ul>
	 *   <li>Sell orders - a lower price is better</li>
	 *   <li>Buy orders - an higher price is better</li>
	 * </ul>
	 * </p>
	 *
	 * @param productId The product ID
	 * @param verb      The verb
	 * @return A set of active orders at the best level for a given product and verb.
	 */
	Set<Order> getOrdersAtBestLevel(String productId, OrderVerb verb);

}
