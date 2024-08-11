package com.valr.api.model.trade;

import com.valr.api.model.order.CurrencyPair;
import com.valr.api.model.order.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Trade {
    private String id;
    private Long price;
    private Double quantity;
    private CurrencyPair currencyPair;
    private Instant tradedAt;
    private Side takerSide;
    private Long sequenceId;
    private Double quoteVolume;
}
