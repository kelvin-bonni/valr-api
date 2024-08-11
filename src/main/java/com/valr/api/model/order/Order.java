package com.valr.api.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String id;
    private Side side;
    private Double quantity;
    private Long price;
    private CurrencyPair currencyPair;
    private Instant timestamp = Instant.now();


    public Order(String id, Double quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Order(String id, Side side, Double quantity, Long price, CurrencyPair pair) {
        this.id = id;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.currencyPair = pair;
    }
}
