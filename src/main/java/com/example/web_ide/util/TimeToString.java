package com.example.web_ide.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeToString {
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return  localDateTime==null?null:localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
