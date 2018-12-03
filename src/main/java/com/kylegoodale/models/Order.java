package com.kylegoodale.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Order {
    public BigDecimal Price;
    public String Error;
    public Order(){}

    // Constructor for successfully processed orders (no error)
    public Order(BigDecimal price){
        this.Price = price.setScale(2, RoundingMode.FLOOR).stripTrailingZeros();
    }

    // Constructor for orders that could not be processed because of an error (no price)
    public Order(String error){
        this.Error = error;
    }
}

