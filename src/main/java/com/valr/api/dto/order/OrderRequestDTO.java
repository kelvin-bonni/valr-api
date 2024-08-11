package com.valr.api.dto.order;

import com.valr.api.model.order.CurrencyPair;
import com.valr.api.model.order.Order;
import com.valr.api.model.order.Side;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class OrderRequestDTO {
    @NotNull(message = "Side must be specified")
    private Side side;

    @Min(0)
    private double quantity;

    @Min(0)
    private long price;

    @NotNull
    private CurrencyPair pair;

    // toOrder method
    public Order toOrder() {
        return new Order(
                UUID.randomUUID().toString(),
                side,
                quantity,
                price,
                pair,
                Instant.now()
        );
    }
}
