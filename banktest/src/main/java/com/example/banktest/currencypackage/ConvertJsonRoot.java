package com.example.banktest.currencypackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertJsonRoot {
    private String date;
    private boolean historical;
    private Info info;
    private Query query;
    private double result;
    private boolean success;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Query{
        private double amount;
        private String from;

        private String to;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Info{
        private double quote;
        private int timestamp;
    }


}
