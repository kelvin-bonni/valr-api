package com.valr.api.dto.trade;

import com.valr.api.model.order.CurrencyPair;
import com.valr.api.model.order.Side;
import com.valr.api.model.trade.Trade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class TradeDTO {
    private String id;
    private Long price;
    private Double quantity;
    private CurrencyPair currencyPair;
    private Instant tradedAt;
    private Side takerSide;
    private Long sequenceId;
    private Double quoteVolume;

    // fromTrade
    public static TradeDTO fromTrade(Trade trade) {
        return new TradeDTO(
                trade.getId(),
                trade.getPrice(),
                trade.getQuantity(),
                trade.getCurrencyPair(),
                trade.getTradedAt(),
                trade.getTakerSide(),
                trade.getSequenceId(),
                trade.getQuoteVolume()
        );
    }
}
