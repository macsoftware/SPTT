package uk.co.macsoftware.java.sptt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "meter_id")
    @Schema(description = "ID of the meter", example = "1")
    private Integer meterId;

    @Column(name = "reading")
    @Schema(description = "Meter reading", example = "1000")
    private Integer reading;

    @Column(name = "date")
    @Schema(description = "Meter reading", example = "1000")
    private Date date;

    @Transient
    @Schema(description = "Usage since last reading", example = "100")
    private Integer usageSinceLastRead;

    @Transient
    @Schema(description = "Period since last reading in days", example = "30")
    private Integer periodSinceLastRead;

    @Transient
    @Schema(description = "Average daily usage", example = "3")
    private Integer avgDailyUsage;

}
