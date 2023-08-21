package com.example.banktest.currencypackage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeJsonRoot {
        private boolean change;
        @Schema(description = "end_date", example = "2023-7-15")
        private String end_date;
        private Map<String, ChangeJsonRoot.Quotes> quotes;
        @Schema(description = "source", example = "USD")
        private String source;

        @Schema(description = "end_date", example = "2023-7-14")
        private String start_date;
        private boolean success;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Quotes{
        @Schema(description = "change", example = "-0.1726")
        private double change;
        @Schema(description = "change_pct", example = "-13.4735")
        private double change_pct;
        @Schema(description = "end_rate", example = "1.108609")
        private double end_rate;
        @Schema(description = "start_rate", example = "1.281236")
        private double start_rate;
    }

}
