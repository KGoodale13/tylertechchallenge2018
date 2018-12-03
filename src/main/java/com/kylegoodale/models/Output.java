package com.kylegoodale.models;

/**
 * Output format based on the specifications provided by the challenge. This is used when JSON encoding the result.
 */
public class Output {
    public Order[] Orders;
    public Output(){}
    public Output(Order[] orders){
        this.Orders = orders;
    }
}
