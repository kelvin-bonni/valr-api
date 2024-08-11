package com.valr.api.service.order;

import com.valr.api.model.order.CurrencyPair;
import com.valr.api.model.order.Order;
import com.valr.api.model.order.OrderBook;
import com.valr.api.model.order.Side;
import com.valr.api.model.trade.Trade;
import com.valr.api.utils.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceImpl implements OrderService{
    @Autowired
    IdGenerator idGenerator;

    private EnumMap<CurrencyPair, OrderBook> orderBooks = new EnumMap<>(CurrencyPair.class);
    private EnumMap<CurrencyPair, List<Trade>> recentTrades = new EnumMap<>(CurrencyPair.class);

    @Override
    public OrderBook getOrderBooks(CurrencyPair pair) {
        return orderBooks.getOrDefault(pair, createOrderBooks());
    }

    @Override
    public List<Trade> getTradeHistory(CurrencyPair pair) {
        return recentTrades.getOrDefault(pair, new ArrayList<>());
    }

    @Override
    public Order limitOrder(Order order) {
        OrderBook orderBook = orderBooks.computeIfAbsent(order.getCurrencyPair(), k -> createOrderBooks());
        orderBook.setSequenceNumber(orderBook.getSequenceNumber() + 1);

        double remainingQuantity;
        if(Side.BUY == order.getSide()){
            remainingQuantity = matchOrder(order, orderBook.getAsks(), orderBook, order.getCurrencyPair());
            if (remainingQuantity > 0.0) {
                // Add remaining quantity as a new buy order
                Order remainingOrder = new Order(idGenerator.generate(), order.getSide(), remainingQuantity, order.getPrice(), order.getCurrencyPair(), order.getTimestamp());
                orderBook.setBids(new HashSet<>(Set.of(remainingOrder)));
            }
        }else if(Side.SELL == order.getSide()){
            remainingQuantity = matchOrder(order, orderBook.getBids(), orderBook, order.getCurrencyPair());
            if (remainingQuantity > 0.0) {
                // Add remaining quantity as a new sell order
                Order remainingOrder = new Order(idGenerator.generate(), order.getSide(), remainingQuantity, order.getPrice(), order.getCurrencyPair(), order.getTimestamp());
                orderBook.setAsks(new HashSet<>(Set.of(remainingOrder)));
            }
        }

        orderBook.setLastChange(Instant.now());
        return order;
    }

    private double matchOrder(
            Order order,
            Set<Order> oppositeOrders,
            OrderBook orderBook,
            CurrencyPair pair
    ) {
        List<Trade> trades = new ArrayList<>(recentTrades.computeIfAbsent(pair, k -> new ArrayList<>()));

        final double[] remainingQuantity = {order.getQuantity()};

        List<Order> ordersToRemove = oppositeOrders.stream()
                .filter(oppositeOrder ->
                        (order.getSide() == Side.BUY && order.getPrice() >= oppositeOrder.getPrice()) ||
                                (order.getSide() == Side.SELL && order.getPrice() <= oppositeOrder.getPrice())
                )
                .map(oppositeOrder -> {
                    double tradedQuantity = Math.min(remainingQuantity[0], oppositeOrder.getQuantity());
                    Trade trade = new Trade(
                            UUID.randomUUID().toString(),
                            oppositeOrder.getPrice(),
                            tradedQuantity,
                            order.getCurrencyPair(),
                            Instant.now(),
                            order.getSide(),
                            orderBook.getSequenceNumber(),
                            tradedQuantity * oppositeOrder.getPrice().doubleValue()
                    );
                    trades.add(trade);
                    remainingQuantity[0] -= tradedQuantity;

                    // Update the remaining quantity of the opposite order
                    oppositeOrder.setQuantity(oppositeOrder.getQuantity() - tradedQuantity);

                    if (oppositeOrder.getQuantity() <= 0.0) {
                        return oppositeOrder; // Collect orders to be removed
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        if (!ordersToRemove.isEmpty()) {
            oppositeOrders.removeAll(ordersToRemove);
            log.info("Removed orders " + ordersToRemove.size());
        }

        return remainingQuantity[0];
    }

    public OrderBook createOrderBooks() {
        return new OrderBook(
                new TreeSet<>(Comparator
                        .comparing(Order::getPrice)
                        .thenComparing(Order::getTimestamp)
                        .thenComparing(Order::getId)
                ),
                new TreeSet<>(Comparator
                        .comparing(Order::getPrice).reversed()
                        .thenComparing(Order::getTimestamp)
                        .thenComparing(Order::getId)
                )
        );
    }
}
