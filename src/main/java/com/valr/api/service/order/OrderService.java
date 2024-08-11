package com.valr.api.service.order;

import com.valr.api.model.order.CurrencyPair;
import com.valr.api.model.order.Order;
import com.valr.api.model.order.OrderBook;
import com.valr.api.model.trade.Trade;

import java.util.List;

public interface OrderService {
    OrderBook getOrderBooks(CurrencyPair pair);
    List<Trade> getTradeHistory(CurrencyPair pair);
    Order limitOrder(Order order);
}
