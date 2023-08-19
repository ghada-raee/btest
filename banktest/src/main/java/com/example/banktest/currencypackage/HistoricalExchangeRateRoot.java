package com.example.banktest.currencypackage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalExchangeRateRoot extends ExchangeRateJsonRoot{
    @Schema(description = "date", example = "2023-7-15")
    protected String date;
    protected boolean historical;
}
