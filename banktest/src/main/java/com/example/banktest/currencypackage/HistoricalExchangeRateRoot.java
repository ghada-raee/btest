package com.example.banktest.currencypackage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalExchangeRateRoot extends ExchangeRateJsonRoot{
    @Schema(description = "date", example = "2023-7-15")
    protected String date;
    protected boolean historical;

    public HistoricalExchangeRateRoot(Map<String, Double> quotes, String source, boolean success,
                                      int timestamp, String date, boolean historical) {
        super(quotes, source, success, timestamp);
        this.date = date;
        this.historical = historical;
    }
}
