package uk.co.macsoftware.java.sptt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MeterReadingRequest {

    @Schema(description = "ID of the meter", example = "1")
    private Integer meterId;

    @Schema(description = "Type of the meter", example = "GAS")
    private MeterType meterType;

    @Schema(description = "Meter reading", example = "1000")
    private Integer reading;

    @Schema(description = "Date of the meter reading", example = "2023-07-14T00:00:00Z")
    private Date date;
}
