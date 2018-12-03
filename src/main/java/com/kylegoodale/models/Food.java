package com.kylegoodale.models;

// Input format for food items. Used when JSON decoding the input file
public class Food {
    public String Name;
    public ItemAddon[] Sizes;
    public ItemAddon[] Extras;

    public Food(){}
}
