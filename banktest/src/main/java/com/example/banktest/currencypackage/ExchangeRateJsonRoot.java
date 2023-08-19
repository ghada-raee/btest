package com.example.banktest.currencypackage;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateJsonRoot {

    @ArraySchema(
            schema = @Schema(
                    example = "{\"USDAUD\": 1.278342,\n \"USDEUR\": 1.278342,\n \"USDGBP\": 0.908019}"
            )
    )
    private Map<String, Double> quotes;
    @Schema(description = "source", example = "USD")
    private String source;
    private boolean success;
    @Schema(description = "timestamp", example = "1432400348")
    private int timestamp;

}

