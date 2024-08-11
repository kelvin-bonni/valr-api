package com.valr.api.service.order;

import com.valr.api.model.order.CurrencyPair;
import com.valr.api.model.order.Order;
import com.valr.api.model.order.OrderBook;
import com.valr.api.model.order.Side;
import com.valr.api.model.trade.Trade;
import com.valr.api.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderServiceImpl orderService;
    @Mock(strictness = Mock.Strictness.LENIENT)
    IdGenerator idGenerator;

    CurrencyPair currencyPair;

    private final String firstGeneratedId = UUID.fromString("65e0a581-971b-4520-974b-d0535c91e744").toString();

    @BeforeEach
    public void setUp() {
        currencyPair = CurrencyPair.BTCZAR;

        EnumMap<CurrencyPair, OrderBook> orderBookMap = new EnumMap<>(CurrencyPair.class);
        orderBookMap.put(currencyPair, new OrderBook());
        ReflectionTestUtils.setField(orderService, "orderBooks", orderBookMap);

        EnumMap<CurrencyPair, List<Trade>> recentTradeMap = new EnumMap<>(CurrencyPair.class);
        recentTradeMap.put(currencyPair, List.of(new Trade()));
        ReflectionTestUtils.setField(orderService, "recentTrades", recentTradeMap);

        when(idGenerator.generate()).thenReturn(firstGeneratedId);
    }

    @Test
    void testGetOrderBookReturnsNewOrderBookIfNotPresent() {
        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
        assertThat(orderBook.getAsks()).isEmpty();
        assertThat(orderBook.getBids()).isEmpty();
    }

    @Test
    void testPlaceLimitOrderAddsNewBuyOrder() {
        Order order = new Order(
                UUID.randomUUID().toString(),
                Side.BUY,
                1.0,
                50000L,
                currencyPair,
                Instant.now()
        );

        orderService.limitOrder(order);
        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
        assertThat(orderBook.getAsks()).isEmpty();
        assertThat(orderBook.getBids()).hasSize(1);
    }

    @Test
    void testPlaceLimitOrderAddsNewSellOrder() {
        Order order = new Order(
                UUID.randomUUID().toString(),
                Side.SELL,
                1.0,
                50000L,
                currencyPair,
                Instant.now()
        );

        orderService.limitOrder(order);
        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
        assertThat(orderBook.getBids()).isEmpty();
        assertThat(orderBook.getAsks()).hasSize(1);
    }

    @Test
    void testMatchOrderMatchesAndRemovesOrdersCorrectly() {
        Order sellOrder = new Order(
                UUID.randomUUID().toString(),
                Side.SELL,
                1.0,
                50000L,
                currencyPair,
                Instant.now()
        );

        orderService.limitOrder(sellOrder);

        Order buyOrder = new Order(
                UUID.randomUUID().toString(),
                Side.BUY,
                1.0,
                50000L,
                currencyPair,
                Instant.now()
        );

        orderService.limitOrder(buyOrder);

        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
        assertThat(orderBook.getBids()).isEmpty();
        assertThat(orderBook.getAsks()).isEmpty();

        List<Trade> trades = orderService.getTradeHistory(currencyPair);
        assertThat(trades).hasSize(1);
    }

    @Test
    void testMatchOrderPartialMatch() {
        Order sellOrder = new Order(
                UUID.randomUUID().toString(),
                Side.SELL,
                1.0,
                50000L,
                currencyPair,
                Instant.now()
        );

        orderService.limitOrder(sellOrder);

        Order buyOrder = new Order(
                UUID.randomUUID().toString(),
                Side.BUY,
                0.5,
                50000L,
                currencyPair,
                Instant.now()
        );

        orderService.limitOrder(buyOrder);

        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
        assertThat(orderBook.getBids()).isEmpty();
        assertThat(orderBook.getAsks()).hasSize(1);

        List<Trade> trades = orderService.getTradeHistory(currencyPair);
        assertThat(trades).hasSize(1);
    }
}
