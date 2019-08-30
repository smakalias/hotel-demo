package com.hotelcorp.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private Byte rating;

    public Hotel(String name, String address, Byte rating) {
        this.name = name;
        this.address = address;
        setRating(rating);
    }

    private void validateRating(Byte rating) {
        if (rating == null) return;

        final int starValue = rating.intValue();
        if (starValue < 1 || starValue > 5) {
            throw new IllegalArgumentException("Invalid rating value: " + starValue + ". Use values [1-5]");
        }
    }

    protected Hotel() {
    }

    public void updateNonNullValues(Hotel source) {
        if (source.name != null) this.name = source.name;
        if (source.address != null) this.address = source.address;
        if (source.rating != null) this.rating = source.rating;
    }

    void setRating(Byte rating) {
        validateRating(rating);
        this.rating = rating;
    }
}
