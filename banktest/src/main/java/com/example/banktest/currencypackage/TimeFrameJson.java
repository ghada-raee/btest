package com.example.banktest.currencypackage;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrameJson {

    @Schema(description = "end_date", example = "2023-7-15")
    protected String end_date;

    protected Map<String, Map<String, Double>> quotes;

    @Schema(description = "end_date", example = "2023-7-14")
    protected String start_date;
    protected boolean timeframe;
    @Schema(description = "source", example = "USD")
    private String source;
    private boolean success;


}
