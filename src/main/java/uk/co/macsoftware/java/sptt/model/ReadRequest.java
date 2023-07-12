package uk.co.macsoftware.java.sptt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ReadRequest {

    @Schema(description = "List of meter readings to be submitted")
    private List<MeterReadingRequest> meterReadings;
}
