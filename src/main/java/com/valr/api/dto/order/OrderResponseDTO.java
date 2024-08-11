package com.valr.api.dto.order;

import com.valr.api.model.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDTO {
    private String id;

    // fromOrder method
    public static OrderResponseDTO fromOrder(Order order) {
        return new OrderResponseDTO(
                UUID.randomUUID().toString()
        );
    }
}
