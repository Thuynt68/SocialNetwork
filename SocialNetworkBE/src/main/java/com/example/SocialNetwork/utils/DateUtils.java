package com.example.SocialNetwork.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static Date addSecondsToDate(Date currentDate, long seconds) {
        return new Date(currentDate.toInstant().plus(Duration.ofSeconds(seconds)).toEpochMilli());
    }

    public static String reFormatDateTime(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy ");
        return dateTime.format(formatter);
    }

}
