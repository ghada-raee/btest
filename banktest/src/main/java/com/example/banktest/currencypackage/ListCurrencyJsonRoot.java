package com.example.banktest.currencypackage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListCurrencyJsonRoot {

    @ArraySchema(
            schema = @Schema(
                    example = "{\"USD\": \"United States Dollar\",\n \"EUR\": \"Euro\",\n \"JPY\": \"Japanese Yen\"}"
            )
    )
    private Map<String, String> currencies; //symbols
    private boolean success;
}
