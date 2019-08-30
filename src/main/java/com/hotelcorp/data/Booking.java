package com.hotelcorp.data;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    @Column(name = "CUSTOMER_LASTNAME")
    private String customerLastName;

    @Column(name = "PAX_NUMBER")
    private Short numberOfPax;

    @Column(scale = 3)
    private BigDecimal price;

    @Column(length = 3)
    private String currency;

    @ManyToOne(optional = false)
    @JoinColumn(name = "HOTEL_ID", nullable = false)
    private Hotel hotel;

    protected Booking() {
    }

    public Booking(String customerName,
                   String customerLastName,
                   Short numberOfPax,
                   BigDecimal price,
                   String currency,
                   Hotel hotel) {
        this.customerName = customerName;
        this.customerLastName = customerLastName;
        this.numberOfPax = numberOfPax;
        this.price = price;
        this.currency = currency;
        this.hotel = hotel;
    }

    public void updateNonNullValues(Booking source) {
        if (source.customerName != null) this.customerName = source.customerName;
        if (source.customerLastName != null) this.customerLastName = source.customerLastName;
        if (source.numberOfPax != null) this.numberOfPax = source.numberOfPax;
        if (source.price != null) this.price = source.price;
        if (source.currency != null) this.currency = source.currency;
        if (source.hotel != null) this.hotel = source.hotel;
    }
}
