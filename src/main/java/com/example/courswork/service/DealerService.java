
package com.example.courswork.service;

import com.example.courswork.model.Dealer;
import java.util.ArrayList;
import java.util.List;

public class DealerService {

    private List<Dealer> dealers = new ArrayList<>();

    public void loadFromFile(String filePath) {

    }

    public List<Dealer> getAllDealers() {
        return dealers;
    }


    public List<Dealer> selectRandomDealers() {

        return null;
    }
}