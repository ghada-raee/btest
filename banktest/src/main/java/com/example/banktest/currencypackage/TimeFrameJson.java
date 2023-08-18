package com.example.banktest.currencypackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrameJson {
    protected String end_date;
    protected Map<String, Map<String, Double>> quotes;
    protected String start_date;
    protected boolean timeframe;
    private String source;
    private boolean success;


}
