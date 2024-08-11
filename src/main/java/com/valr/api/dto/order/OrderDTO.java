package com.valr.api.dto.order;

import com.valr.api.model.order.CurrencyPair;
import com.valr.api.model.order.Order;
import com.valr.api.model.order.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class OrderDTO {
    private String id;
    private Side side;
    private Long price;
    private Double quantity;
    private CurrencyPair currencyPair;
    private Instant timestamp;

    // fromOrder
    public static OrderDTO fromOrder(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getSide(),
                order.getPrice(),
                order.getQuantity(),
                order.getCurrencyPair(),
                order.getTimestamp()
        );
    }
}
