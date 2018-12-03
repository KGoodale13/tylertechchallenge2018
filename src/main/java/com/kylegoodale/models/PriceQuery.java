package com.kylegoodale.models;

import java.math.BigDecimal;

/**
 * This class represents the results from item price lookups in the menu.
 * It serves to relay error messages back to the caller without the need for exceptions.
 * On successful lookups it contains only the needed information for order price totaling
 * which is whether it is a Food or Drink (for meal discounts) and Price.
 */
public class PriceQuery implements Comparable<PriceQuery> {

    // The price and item info (if found)
    private boolean isFood;
    private BigDecimal price;

    // Were we able to get a price for this item?
    private boolean priceFound;
    // ^ If not why?
    private String error;

    // Constructor for price found case
    PriceQuery(BigDecimal price, boolean isFood){
        this.price = price;
        this.isFood = isFood;
        this.priceFound = true;
    }

    // Constructor for error case
    PriceQuery(String error){
        this.error = error;
        this.priceFound = false;
        this.price = BigDecimal.ZERO;
    }

    @Override
    public int compareTo(PriceQuery other){
        return this.price.subtract(other.getPrice()).intValue();
    }

    public boolean isFood(){ return this.isFood; }
    public boolean isDrink(){ return !this.isFood; }


    public BigDecimal getPrice(){ return this.price; }

    // Whether or not we found a price, if this is false there will be an error
    public boolean priceFound(){ return this.priceFound; }

    // If we were unable to find a price, the reason why can be retrieved here
    public String getError(){ return this.error; }
}
