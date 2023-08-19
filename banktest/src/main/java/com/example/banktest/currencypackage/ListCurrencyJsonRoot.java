package com.example.banktest.currencypackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListCurrencyJsonRoot {
    private Map<String, String> currencies; //symbols
    private boolean success;
}
