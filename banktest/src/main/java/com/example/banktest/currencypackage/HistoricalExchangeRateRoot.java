package com.example.banktest.currencypackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalExchangeRateRoot extends ExchangeRateJsonRoot{
    protected String date;
    protected boolean historical;
}
