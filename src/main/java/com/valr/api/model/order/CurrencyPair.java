package com.valr.api.model.order;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CurrencyPair {

    BTCUSDC("BTCUSDC"),
    BTCZAR("BTCZAR"),
    ETHUSDC("ETHUSDC"),
    XRPUSDC("XRPUSDC"),
    ETHZAR("ETHZAR"),
    ADABTC("ADABTC"),
    ADAETH("ADAETH");

    private String name;

    CurrencyPair(String name) {
        this.name = name;
    }

    public static CurrencyPair fromString(String value) {
        return Arrays.stream(CurrencyPair.values())
                .filter(currency -> currency.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}
