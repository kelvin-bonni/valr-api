//package com.valr.api.service.order;
//
//import com.valr.api.model.order.CurrencyPair;
//import com.valr.api.model.order.Order;
//import com.valr.api.model.order.OrderBook;
//import com.valr.api.model.order.Side;
//import com.valr.api.model.trade.Trade;
//import com.valr.api.utils.IdGenerator;
//import com.valr.api.utils.Locker;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.time.Instant;
//import java.util.*;
//
//@ExtendWith(MockitoExtension.class)
//public class OrderServiceTest {
//
//    @InjectMocks
//    OrderServiceImpl orderService;
//    @Mock(strictness = Mock.Strictness.LENIENT)
//    IdGenerator idGenerator;
//    @Mock(strictness = Mock.Strictness.LENIENT)
//    Locker locker;
//
//    private Map<CurrencyPair, OrderBook> orderBooks;
//    Map<CurrencyPair, List<Trade>> recentTrades;
//    CurrencyPair currencyPair;
//
//    private final String firstGeneratedId = UUID.fromString("65e0a581-971b-4520-974b-d0535c91e744").toString();
//
//    @BeforeEach
//    public void setUp() {
//        orderBooks = new HashMap<>();
//        recentTrades = new HashMap<>();
//        currencyPair = CurrencyPair.BTCZAR;
//
//        Map<CurrencyPair, OrderBook> orderBookMap = new HashMap<>();
//        orderBookMap.put(currencyPair, new OrderBook());
//
//        ReflectionTestUtils.setField(orderService, "orderBooks", orderBookMap);
//
////        when(locker.withReadLock(any())).th
//        when(idGenerator.generate()).thenReturn(firstGeneratedId);
//    }
//
//    @Test
//    public void testGetOrderBookReturnsNewOrderBookIfNotPresent() {
//        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
//        assertThat(orderBook.getAsks()).isEmpty();
//        assertThat(orderBook.getBids()).isEmpty();
//    }
//
//    @Test
//    public void testPlaceLimitOrderAddsNewBuyOrder() {
//        Order order = new Order(
//                UUID.randomUUID().toString(),
//                Side.BUY,
//                1.0,
//                50000L,
//                currencyPair,
//                Instant.now()
//        );
//
//        orderService.limitOrder(order);
//        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
//        assertThat(orderBook.getAsks()).isEmpty();
//        assertThat(orderBook.getBids()).hasSize(1);
//        assertThat(orderBook.getBids().iterator().next()).isEqualTo(copyOrder(firstGeneratedId, order));
//
//        assertThat(orderService.getTradeHistory(currencyPair)).isEmpty();
//    }
//
//    public Order copyOrder(String id, Order order){
//        return new Order(id, order.getSide(), order.getQuantity(), order.getPrice(), order.getCurrencyPair(), order.getTimestamp());
//    }
//
//    @Test
//    public void testPlaceLimitOrderAddsNewSellOrder() {
//        Order order = new Order(
//                UUID.randomUUID().toString(),
//                Side.SELL,
//                1.0,
//                50000L,
//                currencyPair,
//                Instant.now()
//        );
//
//        orderService.limitOrder(order);
//        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
//        assertThat(orderBook.getBids()).isEmpty();
//        assertThat(orderBook.getAsks()).hasSize(1);
//        assertThat(orderBook.getAsks().iterator().next()).isEqualTo(copyOrder(firstGeneratedId, order));
//
//        assertThat(orderService.getTradeHistory(currencyPair)).isEmpty();
//    }
//
//    @Test
//    public void testMatchOrderMatchesAndRemovesOrdersCorrectly() {
//        Order sellOrder = new Order(
//                UUID.randomUUID().toString(),
//                Side.SELL,
//                1.0,
//                50000L,
//                currencyPair,
//                Instant.now()
//        );
//
//        orderService.limitOrder(sellOrder);
//
//        Order buyOrder = new Order(
//                UUID.randomUUID().toString(),
//                Side.BUY,
//                1.0,
//                50000L,
//                currencyPair,
//
//                Instant.now()
//        );
//
//        orderService.limitOrder(buyOrder);
//
//        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
//        assertThat(orderBook.getBids()).isEmpty();
//        assertThat(orderBook.getAsks()).isEmpty();
//
//        List<Trade> trades = orderService.getTradeHistory(currencyPair);
//        assertThat(trades).hasSize(1);
//        assertThat(trades.get(0).getPrice()).isEqualTo(50000);
//        assertThat(trades.get(0).getQuantity()).isEqualTo(1.0);
//    }
//
//    @Test
//    public void testMatchOrderPartialMatch() {
//        Order sellOrder = new Order(
//                UUID.randomUUID().toString(),
//                Side.SELL,
//                1.0,
//                50000L,
//                currencyPair,
//                Instant.now()
//        );
//
//        orderService.limitOrder(sellOrder);
//
//        Order buyOrder = new Order(
//                UUID.randomUUID().toString(),
//                Side.BUY,
//                0.5,
//                50000L,
//                currencyPair,
//                Instant.now()
//        );
//
//        orderService.limitOrder(buyOrder);
//
//        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
//        assertThat(orderBook.getBids()).isEmpty();
//        assertThat(orderBook.getAsks()).hasSize(1);
//        Order generatedOrder = orderBook.getAsks().iterator().next();
//        assertThat(generatedOrder).isEqualTo(new Order(
//                firstGeneratedId,
//                Side.SELL,
//                0.5,
//                50000L,
//                currencyPair,
//                generatedOrder.getTimestamp()
//        ));
//
//        List<Trade> trades = orderService.getTradeHistory(currencyPair);
//        assertThat(trades).hasSize(1);
//        assertThat(trades.get(0).getPrice()).isEqualTo(50000);
//        assertThat(trades.get(0).getQuantity()).isEqualTo(0.5);
//    }
//
//    @Test
//    public void testOrderMatchingOverfillsDemand() {
//        OrderBook initialOrderBook = new OrderBook(
//                new TreeSet<>(Set.of(
//                        new Order(UUID.randomUUID().toString(), Side.SELL, 0.5, 1000000L, currencyPair),
//                        new Order(UUID.randomUUID().toString(), Side.SELL, 1.0, 1100000L, currencyPair)
//                )),
//                Instant.now(),
//                1L
//        );
//
//        orderService.getOrderBooks().put(currencyPair, initialOrderBook);
//
//        Order overfillOrder = new Order(UUID.randomUUID().toString(), Side.BUY, 2.0, 1200000L, currencyPair);
//        Order resultOrder = orderService.limitOrder(overfillOrder);
//
//        assertThat(resultOrder.getQuantity()).isEqualTo(2.0);
//
//        OrderBook updatedOrderBook = orderService.getOrderBooks(currencyPair);
//        assertThat(updatedOrderBook.getAsks()).isEmpty();
//        assertThat(updatedOrderBook.getBids()).hasSize(1);
//        Order generatedOrder = updatedOrderBook.getBids().iterator().next();
//        assertThat(generatedOrder).isEqualTo(new Order(
//                firstGeneratedId,
//                Side.BUY,
//                0.5,
//                1200000L,
//                currencyPair,
//                generatedOrder.getTimestamp()
//        ));
//
//        List<Trade> trades = orderService.getTradeHistory(currencyPair);
//        assertThat(trades).hasSize(2);
//        assertThat(trades.get(0).getPrice()).isEqualTo(1000000);
//        assertThat(trades.get(0).getQuantity()).isEqualTo(0.5);
//        assertThat(trades.get(1).getPrice()).isEqualTo(1100000);
//        assertThat(trades.get(1).getQuantity()).isEqualTo(1.0);
//    }
//
//    @Test
//    public void testOrderMatchingDoesNotMatchOnLastOrderPrices() {
//        OrderBook initialOrderBook = new OrderBook(
//                new TreeSet<>(Set.of(
//                        new Order("1", Side.SELL, 0.5, 1000000L, currencyPair),
//                        new Order("2", Side.SELL, 1.0, 1100000L, currencyPair)
//                )),
//                Instant.now(),
//                1L
//        );
//
//        orderService.getOrderBooks().put(currencyPair, initialOrderBook);
//
//        Order overfillOrder = new Order("4", Side.BUY, 2.0, 1000500L, currencyPair);
//        Order resultOrder = orderService.limitOrder(overfillOrder);
//        assertThat(resultOrder.getId()).isNotNull();
//        assertThat(resultOrder.getQuantity()).isEqualTo(2.0);
//
//        OrderBook updatedOrderBook = orderService.getOrderBooks(currencyPair);
//        assertThat(updatedOrderBook.getAsks()).hasSize(2);
//        assertThat(updatedOrderBook.getBids()).hasSize(1);
//        Order generatedOrder = updatedOrderBook.getBids().iterator().next();
//        assertThat(generatedOrder).isEqualTo(new Order(
//                firstGeneratedId,
//                Side.BUY,
//                1.5,
//                1000500L,
//                currencyPair,
//                generatedOrder.getTimestamp()
//        ));
//
//        List<Trade> trades = orderService.getTradeHistory(currencyPair);
//        assertThat(trades).hasSize(1);
//        assertThat(trades.get(0).getPrice()).isEqualTo(1000000);
//        assertThat(trades.get(0).getQuantity()).isEqualTo(0.5);
//    }
//}
