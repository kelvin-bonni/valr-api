package com.valr.api.dto.order;

import com.valr.api.model.order.OrderBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class OrderBookDTO {
    private Set<OrderDTO> asks;
    private Set<OrderDTO> bids;
    private Instant lastChange;
    private Long sequenceNumber;

    // fromOrderBook
    public static OrderBookDTO fromOrderBook(OrderBook orderBook) {
        Set<OrderDTO> asksDTO = orderBook.getAsks().stream()
                .map(OrderDTO::fromOrder)
                .collect(Collectors.toSet());

        Set<OrderDTO> bidsDTO = orderBook.getBids().stream()
                .map(OrderDTO::fromOrder)
                .collect(Collectors.toSet());

        return new OrderBookDTO(
                asksDTO,
                bidsDTO,
                orderBook.getLastChange(),
                orderBook.getSequenceNumber()
        );
    }
}
