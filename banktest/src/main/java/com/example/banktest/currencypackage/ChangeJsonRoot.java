package com.example.banktest.currencypackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeJsonRoot {
        private boolean change;
        private String end_date;
        private Map<String, ChangeJsonRoot.Quotes> quotes;
        private String source;
        private String start_date;
        private boolean success;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Quotes{
        private double change;
        private double change_pct;
        private double end_rate;
        private double start_rate;
    }

}
