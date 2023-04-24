package com.financial;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FinancialMarketImplTest {
	
	private static final double PRECISION = 0.0001;

	private static final String PRODUCT0 = "PRODUCT0";
	private static final double PRICE0 = 5.2;
	private static final double QTY0 = 13.0;

	private static final String PRODUCT1 = "PRODUCT1";
	private static final double PRICE1 = 8.5;
	private static final double QTY1 = 21;

	private FinancialMarketImpl market;

	@Before
	public void setUp() {
		market = new FinancialMarketImpl();
	}

	private void validateOrderParameters(Order order, String productId, OrderVerb verb, double price, double quantity,
										 boolean isActive) {
		assertNotNull(order);
		assertEquals(price, order.getPrice(), PRECISION);
		assertEquals(quantity, order.getQuantity(), PRECISION);
		assertEquals(verb, order.getVerb());
		assertEquals(productId, order.getProductId());
		assertEquals(isActive, order.isActive());
	}

	private void validateOrderParametersWithGetOrder(FinancialMarket marketParam, String orderId, String productId,
			OrderVerb verb, double price, double quantity, boolean isActive) {
		Optional<Order> order = marketParam.getOrder(orderId);
		assertTrue(order.isPresent());
		validateOrderParameters(order.get(), productId, verb, price, quantity, isActive);
	}

	private void validateOrderSet(Set<Order> actualOrders, String ... expectedOrderIds) {
		Set<String> actualOrderIds = actualOrders.stream().map(Order::getId).collect(Collectors.toSet());
		assertEquals(expectedOrderIds.length, actualOrderIds.size());
		for (String orderId: expectedOrderIds) {
			assertTrue(actualOrderIds.contains(orderId));
		}
	}

	@Test
	public void testCreateOneNewOrderAndGet() {
		String id = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE0, QTY0, true);
	}

	@Test
	public void testCreateNewOrderWithSameDetails() {
		String id1 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id1, PRODUCT0, OrderVerb.SELL, PRICE0, QTY0, true);

		String id2 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id2, PRODUCT0, OrderVerb.SELL, PRICE0, QTY0, true);

		assertNotEquals(id1, id2);
	}

	@Test
	public void testModifyOrderChangePrice() {
		String id = market.createOrder(PRODUCT1, OrderVerb.BUY, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id, PRODUCT1, OrderVerb.BUY, PRICE0, QTY0, true);

		boolean modified = market.modifyOrder(id, PRICE1, QTY0);
		assertTrue(modified);
		validateOrderParametersWithGetOrder(market, id, PRODUCT1, OrderVerb.BUY, PRICE1, QTY0, true);
	}

	@Test
	public void testModifyOrderChangeQuantity() {
		String id = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.SELL, PRICE0, QTY0, true);

		boolean modified = market.modifyOrder(id, PRICE0, QTY1);
		assertTrue(modified);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.SELL, PRICE0, QTY1, true);
	}

	@Test
	public void testModifyOrderChangePriceAndQuantity() {
		String id = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE0, QTY0, true);

		boolean modified = market.modifyOrder(id, PRICE1, QTY1);
		assertTrue(modified);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE1, QTY1, true);
	}

	@Test
	public void testModifyOrderChangeNothing() {
		String id = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.SELL, PRICE0, QTY0, true);

		assertTrue(market.modifyOrder(id, PRICE0, QTY0));
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.SELL, PRICE0, QTY0, true);
	}

	@Test
	public void testDeleteOrder() {
		String id = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE0, QTY0, true);
		assertTrue(market.deleteOrder(id));
		assertFalse(market.getOrder(id).isPresent());
	}

	@Test
	public void testDeleteOrderTwice() {
		String id = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		assertTrue(market.deleteOrder(id));
		assertFalse(market.deleteOrder(id));
	}

	@Test
	public void testSuspendOrder() {
		String id = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE0, QTY0, true);
		assertTrue(market.suspendOrder(id));
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE0, QTY0, false);
	}

	@Test
	public void testSuspendOrderTwice() {
		String id = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		assertTrue(market.suspendOrder(id));
		assertFalse(market.suspendOrder(id));
	}

	@Test
	public void testActivateOrder() {
		String id = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE0, QTY0, true);
		assertTrue(market.suspendOrder(id));
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE0, QTY0, false);
		assertTrue(market.activateOrder(id));
		validateOrderParametersWithGetOrder(market, id, PRODUCT0, OrderVerb.BUY, PRICE0, QTY0, true);
	}

	@Test
	public void testActivateOrderTwice() {
		String id = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		assertTrue(market.suspendOrder(id));
		assertTrue(market.activateOrder(id));
		assertFalse(market.activateOrder(id));
	}

	@Test
	public void testGetOrdersAtBestLevelEmpty() {
		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.BUY));
	}

	@Test
	public void testGetOrdersAtBestLevel1() {
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 1, QTY0 + 1);
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 2);
		String id1 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 3, QTY0 + 3);
		String id2 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 4, QTY0);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0 + 1);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 6, QTY0 + 2);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 7, QTY0 + 3);

		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.BUY), id1);
		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.SELL), id2);
	}

	@Test
	public void testGetOrdersAtBestLevel2() {
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 1, QTY0 + 1);
		String id1 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 2);
		String id2 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 3);
		String id3 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0);
		String id4 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0 + 1);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 6, QTY0 + 2);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 7, QTY0 + 3);

		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.BUY), id1, id2);
		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.SELL), id3, id4);
	}

	@Test
	public void testGetOrdersAtBestLevelDeleted() {
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 1, QTY0 + 1);
		String id1 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 2);
		String id2 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 3);
		String id3 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0);
		String id4 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0 + 1);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 6, QTY0 + 2);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 7, QTY0 + 3);

		assertTrue(market.deleteOrder(id1));
		assertTrue(market.deleteOrder(id3));

		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.BUY), id2);
		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.SELL), id4);
	}

	@Test
	public void testGetOrdersAtBestLevelDeletedBestPrice() {
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 1, QTY0 + 1);
		String id1 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 2);
		String id2 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 3, QTY0 + 3);
		String id3 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 4, QTY0);
		String id4 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0 + 1);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 6, QTY0 + 2);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 7, QTY0 + 3);

		assertTrue(market.deleteOrder(id2));
		assertTrue(market.deleteOrder(id3));

		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.BUY), id1);
		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.SELL), id4);
	}

	@Test
	public void testGetOrdersAtBestLevelSuspended() {
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 1, QTY0 + 1);
		String id1 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 2);
		String id2 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 3);
		String id3 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0);
		String id4 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0 + 1);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 6, QTY0 + 2);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 7, QTY0 + 3);

		assertTrue(market.suspendOrder(id1));
		assertTrue(market.suspendOrder(id3));

		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.BUY), id2);
		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.SELL), id4);
	}

	@Test
	public void testGetOrdersAtBestLevelSuspendedBestPrice() {
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 1, QTY0 + 1);
		String id1 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 2);
		String id2 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 3, QTY0 + 3);
		String id3 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 4, QTY0);
		String id4 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0 + 1);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 6, QTY0 + 2);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 7, QTY0 + 3);

		assertTrue(market.suspendOrder(id2));
		assertTrue(market.suspendOrder(id3));

		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.BUY), id1);
		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.SELL), id4);
	}

	@Test
	public void testGetOrdersAtBestLevelMultiProduct() {
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0, QTY0);
		market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 1, QTY0 + 1);
		String id1 = market.createOrder(PRODUCT0, OrderVerb.BUY, PRICE0 + 2, QTY0 + 2);
		market.createOrder(PRODUCT1, OrderVerb.BUY, PRICE0 + 3, QTY0 + 3);
		market.createOrder(PRODUCT1, OrderVerb.SELL, PRICE0 + 4, QTY0);
		String id2 = market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 5, QTY0 + 1);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 6, QTY0 + 2);
		market.createOrder(PRODUCT0, OrderVerb.SELL, PRICE0 + 7, QTY0 + 3);

		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.BUY), id1);
		validateOrderSet(market.getOrdersAtBestLevel(PRODUCT0, OrderVerb.SELL), id2);
	}
}
