package com.example.courswork.parser;

import com.example.courswork.model.Dealer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class dealerParser {

    public static List<Dealer> parseDealers(String filePath) {

        List<Dealer> dealers = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = line.split("[|,;]");

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                if (fields.length < 4) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                try {

                    String dealerId = fields[0];
                    String dealerName = fields[1];
                    String dealerPhone = fields[2];
                    String dealerLocation = fields[3];

                    Dealer dealer = new Dealer(
                            dealerId,
                            dealerName,
                            dealerPhone,
                            dealerLocation
                    );

                    dealers.add(dealer);

                } catch (Exception e) {
                    System.out.println("Skipping invalid line: " + line);
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return dealers;
    }
}