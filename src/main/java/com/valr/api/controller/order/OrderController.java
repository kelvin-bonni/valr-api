package com.valr.api.controller.order;

import com.valr.api.dto.ErrorResponse;
import com.valr.api.dto.order.OrderBookDTO;
import com.valr.api.dto.order.OrderRequestDTO;
import com.valr.api.dto.order.OrderResponseDTO;
import com.valr.api.dto.trade.TradeDTO;
import com.valr.api.model.order.CurrencyPair;
import com.valr.api.model.order.Order;
import com.valr.api.model.order.OrderBook;
import com.valr.api.model.trade.Trade;
import com.valr.api.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "OrderBookController", description = "The controller for managing the book orders")
@RequestMapping("/api/v1")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(description = "Get the list of orders of books containing buy and sell orders for a given currency pair.")
    @GetMapping(value = "/{currency}/orderbook", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOrderBook(@Parameter(description = "Currency pair", required = true)
                                              @PathVariable("currency") String currency) {
        CurrencyPair currencyPair = CurrencyPair.fromString(currency);
        if (currencyPair == null) {
            return badCurrencyPairResponse(currency);
        }
        OrderBook orderBook = orderService.getOrderBooks(currencyPair);
        return ResponseEntity.ok(OrderBookDTO.fromOrderBook(orderBook));
    }

    @Operation(description = "Get the list of most recent trades executed for a given currency pair.")
    @GetMapping("/{currency}/tradehistory")
    public ResponseEntity<?> getTradeHistory(@Parameter(description = "Currency pair", required = true)
                                                 @PathVariable("currency") String currency) {
        CurrencyPair currencyPair = CurrencyPair.fromString(currency);
        if (currency == null) {
            return badCurrencyPairResponse(currency);
        }
        List<Trade> trades = orderService.getTradeHistory(currencyPair);
        List<TradeDTO> tradeDTOs = trades.stream()
                .map(TradeDTO::fromTrade)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tradeDTOs);
    }

    @Operation(description = "Place a limit order for a particular book.")
    @PostMapping("/orders/limit")
    public ResponseEntity<?> limitOrder(@Parameter(description = "Order request", required = true) @RequestBody OrderRequestDTO orderRequest) {
        Order order = orderRequest.toOrder();
        Order placedOrder = orderService.limitOrder(order);
        return ResponseEntity.ok(OrderResponseDTO.fromOrder(placedOrder));
    }

    public ResponseEntity<ErrorResponse> badCurrencyPairResponse(String currencyPair){
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to parse currency pair :"+currencyPair));
    }
}
