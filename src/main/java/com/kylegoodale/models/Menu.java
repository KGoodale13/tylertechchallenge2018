package com.kylegoodale.models;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * The Menu class provides an easier way to access the input data and get prices for items
 */
public class Menu {

    private HashMap<String, Product> products = new HashMap<>();

    public Menu(InputFormat inputFormat){

        // Take all of our input arrays and add them to hashmaps indexed by their name for fast lookup

        HashMap<String, BigDecimal> drinkExtras = new HashMap<>();

        for(ItemAddon drinkExtra : inputFormat.DrinkExtras)
            drinkExtras.put(drinkExtra.Name, drinkExtra.Price);

        for(Drink drink : inputFormat.Drinks)
            products.put(drink.Name, new Product(flattenItemAddons(drink.Sizes), drinkExtras, false));

        for(Food food : inputFormat.Food)
            products.put(food.Name, new Product(flattenItemAddons(food.Sizes), flattenItemAddons(food.Extras), true));

    }

    /**
     * Attempts to find price information on the item given the name, size and any extras
     *
     */
    public PriceQuery queryPriceInfo(String item, String size, String[] extras){

        if(!products.containsKey(item))
            return new PriceQuery("Unable to find item: " + item);

        Product product = products.get(item);

        if(!product.priceBySize.containsKey(size))
            return new PriceQuery("Unable to find size: " + size + " for item: " + item);

        BigDecimal price = product.priceBySize.get(size);

        // Now add the price of any extras
        for(String extra: extras){
            if(!product.extras.containsKey(extra))
                return new PriceQuery("Unable to find extra: " + extra + " for item: " + item);

            price = price.add(product.extras.get(extra));
        }

        // We were able to find the item in that size and any extras passed as well so return the successful query
        return new PriceQuery(price, product.isFood);
    }


    // Flattens the item addon array into a hashmap of addon name -> price
    private HashMap<String, BigDecimal> flattenItemAddons(ItemAddon[] itemAddons){
        HashMap<String, BigDecimal> priceMap = new HashMap<>();

        for(ItemAddon addon: itemAddons)
            priceMap.put(addon.Name, addon.Price);

        return priceMap;
    }


    // This is an abstraction from food and drinks. This is used in the menu class to store both food and drink items in the same way
    private class Product {
        public HashMap<String, BigDecimal> priceBySize;
        public HashMap<String, BigDecimal> extras;

        public boolean isFood;

        // This constructor is used by food because
        public Product(HashMap<String, BigDecimal> sizes, HashMap<String, BigDecimal> extras, boolean isFood){
            this.priceBySize = sizes;
            this.extras = extras;
            this.isFood = isFood;
        }
    }

}
