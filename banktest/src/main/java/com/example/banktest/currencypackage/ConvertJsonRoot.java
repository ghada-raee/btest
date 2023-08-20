package com.example.banktest.currencypackage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertJsonRoot {
    @Schema(description = "date", example = "2023-7-15")
    private String date;
    private boolean historical;
    private Info info;
    private Query query;
    @Schema(description = "result", example = "5.1961")
    private double result;
    private boolean success;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Query{
        @Schema(description = "amount", example = "10")
        private double amount;
        @Schema(description = "from", example = "USD")
        private String from;
        @Schema(description = "to", example = "GBP")

        private String to;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        @Schema(description = "quote", example = "0.51961")
        private double quote;
        @Schema(description = "timestamp", example = "1104623999")
        private int timestamp;
    }


}
