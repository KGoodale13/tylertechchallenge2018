package com.kylegoodale.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.kylegoodale.models.InputFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;

/**
 * The IO service is responsible for reading/writing and serializing/deserializing our data files
 */
public class IOService {

    public IOService(){}

    /**
     * Attempts to load the input file and deserialize its contents into a InputFormat object
     * @param inputFile - The file to load
     * @return Optiona<InputFormat> This will be defined if we succeed and empty otherwise
     */
    public Optional<InputFormat> loadInput(File inputFile){
        try {
            // Attempt to load the file
            JsonReader reader = new JsonReader(new FileReader(inputFile));
            // Attempt to deserialize the file
            try {
                Gson gson = new Gson();
                InputFormat inputFormat = gson.fromJson(reader, InputFormat.class);
                return Optional.of(inputFormat);
            } catch (JsonSyntaxException e) {
                System.out.println("Error: Unable to parse input file. File content is not consistent with the expected structure of a InputFormat");
                System.out.println(e.getMessage());
                return Optional.empty();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to load input file.");
            return Optional.empty();
        }
    }

}
