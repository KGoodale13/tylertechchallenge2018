package com.kylegoodale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kylegoodale.services.IOService;
import com.kylegoodale.services.OrderService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    // This is used to extract the input file number from the path. (The X in ./inputs/input.X.json)
    private static final Pattern inputFilePattern = Pattern.compile("input\\.([0-9]+)\\.json");

    public static void main(String[] args){

        if(args.length == 0)
            fatalError("Expected input file path as first argument but no argument was passed!");

        String filePath = args[0];


        // Attempt to extract the input file number using our regrex
        Matcher filePathMatcher = inputFilePattern.matcher(filePath);
        if(!filePathMatcher.find())
            fatalError("Unable to find input file number. (Is the input file in the format input.x.json?)");

        // Extract the file number for when we attempt to output it
        String fileNumber = filePathMatcher.group(1);
        File inputFile = new File(filePath);

        // Now we need to make sure the file exists
        if(!inputFile.exists() || !inputFile.canRead())
            fatalError("File: " + filePath + " not found or is unreadable.");

        System.out.println("Attempting to load input file...");

        // If we were able to load a menu, we will pass it to the order service to collect and process the orders
        IOService ioService = new IOService();

        // Attempt to load the inputfile as a menu through our io service. If successful we will hand it off to the
        // order service to collect orders and process them
        ioService
            .loadInput(inputFile) // Load an parse the input file
            .map(OrderService::new)
            .map(OrderService::collectOrders) // Start collecting orders from stdin and process them into our 'Output' class
            .ifPresent(output -> { // Finally, Serialize our 'Output' to json and attempt to write it to disk
                try(Writer writer = new FileWriter("./outputs/KYLE_GOODALE." + fileNumber + ".json")){
                    Gson gson = new GsonBuilder().create();
                    gson.toJson(output, writer);
                } catch(IOException e){
                    System.out.println("Unable to write output file. IOException encountered");
                    System.out.println(e.getMessage());
                }
            });
    }


    // Helper function for terminating on all the fatal errors
    private static void fatalError(String error){
        System.out.println("[Error] " + error);
        System.exit(1);
    }

}
