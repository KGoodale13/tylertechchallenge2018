package com.kylegoodale.services;

import com.kylegoodale.models.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

/**
 * The OrderService class is responsible for collecting orders from STDIN and processing them
 * This is the "meat and bones" of the application
 */
public class OrderService {

    private Menu menu;

    // This is the percentage discount to apply to meals. Value should be a decimal between 0 and 1
    private static BigDecimal MEAL_DISCOUNT = BigDecimal.valueOf(0.1);

    public OrderService(InputFormat inputFormat){
        // Create a menu handler that will take the raw input format and turn it into something more usable
        this.menu = new Menu(inputFormat);
    }

    // Collect the order from STDIN, processes them and returns the output result
    public Output collectOrders(){
        System.out.println("Ready to accept orders. Please enter orders one line at a time. When you are finished, enter an empty line");

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        // Create a stream of user input to process orders line by line and then returning the resulting array of processed orders
        return new Output(
            input.lines()
                .map(String::trim) // Get rid of any whitespace padding
                .takeWhile(s -> !s.equals("")) // Stop collecting orders when we receive an empty line
                .filter(line -> line.startsWith("order")) // Only process lines starting with 'order' skip over invalid input
                .map(line -> line.substring(6)) // Cut off the 'order' prefix on the string leaving us with just the order parameters
                .map(this::processOrder) // Attempt to process the order, this transforms it to an OrderResult
                .toArray(Order[]::new)
        );
    }

    // This function handles the logic for processing an individual order from a string.
    // It will return an OrderResult which will contain either the total order price or an error message
    private Order processOrder(String orderString){
        // Split the string by "," to get or individual orders
        String[] items = orderString.split(",");

        // Now that we have our orders we will attempt to get a price for the item of the specified size and all the extras
        // In order to easily combine these items as "meals" and to discount the most expensive items first after we will
        // group our price queries into separate lists for food and drinks. This we can sort the food and drink items by price and then
        // pop them off in pairs

        ArrayList<BigDecimal> foodPrices = new ArrayList<>();
        ArrayList<BigDecimal> drinkPrices = new ArrayList<>();

        for(String item: items){
            // Split the order by spaces to get the different parts. Orders should take the form:
            // [item] [size] [extra1]...[extraN]
            String[] orderComponents = item.trim().split(" ");
            if(orderComponents.length < 2)
                return new Order("Invalid input. Expected item and size but got fewer than 2 parameters.");

            String itemName = orderComponents[0];
            String size = orderComponents[1];
            String[] extras = Arrays.copyOfRange(orderComponents, 2, orderComponents.length);

            // Attempt to find a matching price for this order from the menu
            PriceQuery priceQuery = this.menu.queryPriceInfo(itemName, size, extras);

            // There was an issue with this item so we are not going to be able to give a total for this order
            if(!priceQuery.priceFound())
                return new Order(priceQuery.getError());

            // Place the result into the correct category
            if(priceQuery.isFood())
                foodPrices.add(priceQuery.getPrice());
            else
                drinkPrices.add(priceQuery.getPrice());

        }

        // Note: I would have preferred to do this whole thing in a stream. But Java's Stream library lacks a zip function

        // Sort our food and drink items by price so we can create meals out of the most expensive pairs
        foodPrices.sort(Collections.reverseOrder());
        drinkPrices.sort(Collections.reverseOrder());

        // Loop through them in pairs and apply discounts.
        // NOTE: I was unsure if the discount should apply to both items or only the drink. So I applied it to both
        for(int i=0; i < foodPrices.size() && i < drinkPrices.size(); i++){
            // Don't apply meal discounts if one of the items wasn't purchased i.e water.
            if(foodPrices.get(i).compareTo(BigDecimal.ZERO) == 0 || drinkPrices.get(i).compareTo(BigDecimal.ZERO) == 0)
                break;

            // Apply the discounts (Price * (1-discount))
            foodPrices.set(i, foodPrices.get(i).multiply(BigDecimal.ONE.subtract(MEAL_DISCOUNT)));
            drinkPrices.set(i, drinkPrices.get(i).multiply(BigDecimal.ONE.subtract(MEAL_DISCOUNT)));
        }

        // Combine both food and drink as a stream and then sum them up using reduce
        return new Order(
            Stream.concat(foodPrices.stream(), drinkPrices.stream()).reduce(BigDecimal.ZERO, BigDecimal::add)
        );

    }

}
