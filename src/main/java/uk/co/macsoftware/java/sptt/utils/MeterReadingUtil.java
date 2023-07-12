package uk.co.macsoftware.java.sptt.utils;

import uk.co.macsoftware.java.sptt.model.MeterReadingRequest;
import uk.co.macsoftware.java.sptt.model.Reading;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class MeterReadingUtil {

    public static String validation(Reading oldReading, MeterReadingRequest newReading) {

        String validationErrors = "";

        LocalDate oldReadingDate = oldReading.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate newReadingDate = newReading.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        //Check for duplication
        if (oldReading.getMeterId().equals(newReading.getMeterId()) &&
                oldReading.getReading().equals(newReading.getReading()) &&
                oldReadingDate.equals(newReadingDate)) {
            validationErrors = String.format(
                    "New %s Meter Reading for id: %s, reading: %s & date: %s is a duplicate. Remove before resubmitting.",
                    newReading.getMeterType().getValue(),
                    newReading.getMeterId(),
                    newReading.getReading(),
                    newReading.getDate());
            return validationErrors;
        }

        //Check to see if it is a bigger reading
        if(oldReading.getReading() > newReading.getReading()){
            validationErrors = String.format(
                    "New %s Meter Reading for id: %s, reading: %s & date: %s is less than a previous reading. Remove before resubmitting.",
                    newReading.getMeterType().getValue(),
                    newReading.getMeterId(),
                    newReading.getReading(),
                    newReading.getDate());
            return validationErrors;
        }

        //Check to see if it is an older date
        if(oldReadingDate.isAfter(newReadingDate)){
            validationErrors = String.format(
                    "New %s Meter Reading for id: %s, reading: %s & date: %s is older than a previous reading. Remove before resubmitting.",
                    newReading.getMeterType().getValue(),
                    newReading.getMeterId(),
                    newReading.getReading(),
                    newReading.getDate());
            return validationErrors;
        }

        return validationErrors;
    }

    public static <T extends Reading> List<T> updateReadings(List<T> readings) {
        int usageSinceLastRead = 0;
        int lastUsage = 0;
        Date lastRead = new Date();

        for (T reading : readings) {

            usageSinceLastRead = lastUsage == 0 ? reading.getReading() : reading.getReading() - lastUsage;
            reading.setUsageSinceLastRead(usageSinceLastRead);

            int lastReadPeriod = lastUsage == 0 ? 0 : (int) ChronoUnit.DAYS.between(lastRead.toInstant(), reading.getDate().toInstant());
            reading.setPeriodSinceLastRead(lastReadPeriod);

            int avg = lastReadPeriod == 0 ? usageSinceLastRead : usageSinceLastRead / lastReadPeriod;
            reading.setAvgDailyUsage(avg);

            lastUsage = reading.getReading();
            lastRead = reading.getDate();
        }
        return readings;
    }



}
