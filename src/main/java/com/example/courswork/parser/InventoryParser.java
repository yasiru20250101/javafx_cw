package com.example.courswork.parser;

import com.example.courswork.model.part;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryParser {

    public static List<part> parseParts(String filePath) {
        List<part> parts = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = line.split("[|,;]", -1);

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                if (fields.length < 8) {
                    continue;
                }

                try {

                    String partId = fields[0];
                    String partName = fields[1];

                    String brand = "";
                    brand = fields.length > 2 ? fields[2] : "";
                    String rawPrice = fields[3];
                    String cleanedPrice = rawPrice;

                    if (cleanedPrice.toLowerCase().startsWith("rs.")) {
                        cleanedPrice = cleanedPrice.substring(3);
                    } else if (cleanedPrice.toLowerCase().startsWith("rs")) {
                        cleanedPrice = cleanedPrice.substring(2);
                    }
                    cleanedPrice = cleanedPrice.trim();

                    double price = Double.parseDouble(cleanedPrice);
                    int quantity = Integer.parseInt(fields[4]);

                    String category = "";
                    category = fields.length > 5 ? fields[5] : "";

                    String dateAdded = "";
                    dateAdded = fields.length > 6 ? fields[6] : "";

                    String image = "";
                    image = fields.length > 7 ? fields[7] : "";

                    part newPart = new part(
                            partId,
                            partName,
                            brand,
                            price,
                            quantity,
                            category,
                            dateAdded,
                            image
                    );

                    parts.add(newPart);

                } catch (Exception e) {
                    System.out.println("Skipping invalid line: " + line);
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return parts;
    }
}