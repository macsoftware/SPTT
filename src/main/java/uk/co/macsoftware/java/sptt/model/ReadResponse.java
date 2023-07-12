package uk.co.macsoftware.java.sptt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ReadResponse {

    @Schema(description = "Account ID", example = "123456")
    private Long accountId;

    @Schema(description = "List of gas readings for the account")
    private List<GasReading> gasReadings;

    @Schema(description = "List of electric readings for the account")
    private List<ElecReading> elecReadings;

    @Schema(description = "Comparison value for gas usage", example = "50")
    private int gasComparison;

    @Schema(description = "Comparison value for electric usage", example = "100")
    private int elecComparison;
}
