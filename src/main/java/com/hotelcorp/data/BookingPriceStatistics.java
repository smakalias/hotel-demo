package com.hotelcorp.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingPriceStatistics {
    private String currency;
    private BigDecimal sumAmount;

    public BookingPriceStatistics(String currency, BigDecimal sumAmount) {
        this.currency = currency;
        this.sumAmount = sumAmount;
    }
}
