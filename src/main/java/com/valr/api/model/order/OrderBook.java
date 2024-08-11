package com.valr.api.model.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Setter
@Getter
@NoArgsConstructor
public class OrderBook {

    private Set<Order> asks = new HashSet<>();
    private Set<Order> bids = new HashSet<>();
    private Instant lastChange = Instant.now();
    private Long sequenceNumber = 0L;

    public OrderBook(Set<Order> asks, Set<Order> bids) {
        this.asks = asks;
        this.bids = bids;
        this.lastChange = Instant.now();
        this.sequenceNumber = Instant.now().toEpochMilli();
    }

    public <E> OrderBook(Set<Order> asks, Instant lastChange, Long sequenceNumber) {
        this.asks = asks;
        this.lastChange = lastChange;
        this.sequenceNumber = sequenceNumber;
    }
}
