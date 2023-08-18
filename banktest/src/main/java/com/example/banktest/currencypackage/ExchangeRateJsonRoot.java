package com.example.banktest.currencypackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateJsonRoot {

    private Map<String, Double> quotes;
    private String source;
    private boolean success;
    private int timestamp;

}

