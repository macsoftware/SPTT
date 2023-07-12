package uk.co.macsoftware.java.sptt.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import uk.co.macsoftware.java.sptt.model.ElecReading;
import uk.co.macsoftware.java.sptt.model.GasReading;
import uk.co.macsoftware.java.sptt.model.MeterReadingRequest;
import uk.co.macsoftware.java.sptt.model.MeterType;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MeterReadingUtilTest {

    @Test
    void testValidationWithDifferentReading() {

        Date now = Date.from(Instant.now());

        ElecReading oldReading = ElecReading.builder()
                .id(1L)
                .meterId(123)
                .reading(100)
                .date(now)
                .build();

        MeterReadingRequest newReading = MeterReadingRequest.builder()
                .meterId(123)
                .meterType(MeterType.GAS)
                .reading(200)
                .date(now)
                .build();

        String validationErrors = MeterReadingUtil.validation(oldReading, newReading);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    void testValidationWithDuplicate() {

        Date now = Date.from(Instant.now());

        ElecReading oldReading = ElecReading.builder()

                .id(1L)
                .meterId(123)
                .reading(100)
                .date(now)
                .build();

        MeterReadingRequest newReading = MeterReadingRequest.builder()
                .meterId(123)
                .meterType(MeterType.GAS)
                .reading(100)
                .date(now)
                .build();

        String validationErrors = MeterReadingUtil.validation(oldReading, newReading);

        assertTrue(validationErrors.contains("is a duplicate"));
    }

    @Test
    void testValidationWithLessReading() {

        Date now = Date.from(Instant.now());

        ElecReading oldReading = ElecReading.builder()

                .id(1L)
                .meterId(123)
                .reading(100)
                .date(now)
                .build();

        MeterReadingRequest newReading = MeterReadingRequest.builder()
                .meterId(123)
                .meterType(MeterType.GAS)
                .reading(90)
                .date(now)
                .build();

        String validationErrors = MeterReadingUtil.validation(oldReading, newReading);

        assertTrue(validationErrors.contains("is less than a previous reading"));
    }

    @Test
    void testValidationWithOlderDate() {

        Date now = Date.from(Instant.now());
        Date olderDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        ElecReading oldReading = ElecReading.builder()

                .id(1L)
                .meterId(123)
                .reading(100)
                .date(now)
                .build();

        MeterReadingRequest newReading = MeterReadingRequest.builder()
                .meterId(123)
                .meterType(MeterType.GAS)
                .reading(100)
                .date(olderDate)
                .build();

        String validationErrors = MeterReadingUtil.validation(oldReading, newReading);

        assertTrue(validationErrors.contains("is older than a previous reading"));
    }

    @Test
    void updateReadings_noPriorReadings_updatesReadingsCorrectly() {
        Date now = new Date();
        GasReading gasReading = GasReading.builder()
                .reading(100)
                .date(now)
                .build();

        List<GasReading> readings = Arrays.asList(gasReading);

        List<GasReading> updatedReadings = MeterReadingUtil.updateReadings(readings);

        assertEquals(100, updatedReadings.get(0).getUsageSinceLastRead());
        assertEquals(0, updatedReadings.get(0).getPeriodSinceLastRead());
        assertEquals(100, updatedReadings.get(0).getAvgDailyUsage());
    }

    @Test
    void updateReadings_withPriorReadings_updatesReadingsCorrectly() {
        Date now = new Date();
        Date twoDaysAgo = new Date(now.getTime() - (1000 * 60 * 60 * 48));
        GasReading priorReading = GasReading.builder()
                .reading(100)
                .date(twoDaysAgo)
                .build();
        GasReading currentReading = GasReading.builder()
                .reading(200)
                .date(now)
                .build();

        List<GasReading> readings = Arrays.asList(priorReading, currentReading);

        List<GasReading> updatedReadings = MeterReadingUtil.updateReadings(readings);

        assertEquals(100, updatedReadings.get(0).getUsageSinceLastRead());
        assertEquals(0, updatedReadings.get(0).getPeriodSinceLastRead());
        assertEquals(100, updatedReadings.get(0).getAvgDailyUsage());

        assertEquals(100, updatedReadings.get(1).getUsageSinceLastRead());
        assertEquals(2, updatedReadings.get(1).getPeriodSinceLastRead());
        assertEquals(50, updatedReadings.get(1).getAvgDailyUsage());
    }

}
