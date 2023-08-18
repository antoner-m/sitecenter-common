package org.sitecenter.common.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
public class LocalDateUtils {
    public static LocalDateTime getDate(String str) {
        if (str == null || str.length() == 0) return null;
        str = str.trim();
        LocalDateTime result = null;
        if (!str.contains("Z") && str.contains("T"))
            try {
                result = LocalDateTime.parse(str);
                return result;
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
        if (str.contains("Z") && str.contains("T"))
            try {
                ZonedDateTime zdt = ZonedDateTime.parse(str);
                result = zdt.toLocalDateTime();
                return result;
            } catch (Exception ex) {
                log.error("Error parsing date1:["+str+"]:",ex);
            }

        if (str.contains(".") && str.length() == 10) {
            String strDateFormatString = "yyyy.MM.dd";
            java.text.DateFormat df_date = new java.text.SimpleDateFormat(strDateFormatString);
            try {
                result = new java.sql.Timestamp(df_date.parse(str).getTime()).toLocalDateTime();
                return result;
            } catch (ParseException ex) {
                log.error("Error parsing date2:["+str+"]:",ex);
                ex.printStackTrace();
            }
        }
        if (str.contains("-") && str.length() == 10) {
            String strDateFormatString = "yyyy-MM-dd";
            java.text.DateFormat df_date = new java.text.SimpleDateFormat(strDateFormatString);
            try {
                result = new java.sql.Timestamp(df_date.parse(str).getTime()).toLocalDateTime();
                return result;
            } catch (ParseException ex) {
                log.error("Error parsing date3:["+str+"]:",ex);
            }
        }
        if (str.contains(" ") && str.length() == 19) {
            String strDateFormatString = "yyyy-MM-dd HH:mm:ss";
            java.text.DateFormat df_date = new java.text.SimpleDateFormat(strDateFormatString);
            try {
                result = new java.sql.Timestamp(df_date.parse(str).getTime()).toLocalDateTime();
                return result;
            } catch (ParseException ex) {
                log.error("Error parsing date4:["+str+"]:",ex);
            }
        }
        if (str.length() == 25 && str.contains("+") && str.contains("-") && str.contains("T")) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx"));
                return dateTime;
            } catch (Exception ex){
                log.error("Error parsing date5:["+str+"]:",ex);
            }
        }
        if (str.contains("+") && str.contains("-") && str.contains("T")) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
                return dateTime;
            } catch (Exception ex){
                log.error("Error parsing date6:["+str+"]:",ex);
            }
        }

        if (str.contains("+") && str.contains(".") && str.contains("T")) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ssZ"));
                return dateTime;
            } catch (Exception ex) {
                log.error("Error parsing date7:["+str+"]:",ex);
            }
        }

        if (str.contains("UTC")) {
                SimpleDateFormat df_date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                try {
                    result = new java.sql.Timestamp(df_date.parse(str).getTime()).toLocalDateTime();
                    return result;
                } catch (ParseException ex) {
                    log.error("Error parsing date8:["+str+"]:",ex);
                    ex.printStackTrace();
                }
//            try {
//                LocalDateTime dateTime = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy"));
//                return dateTime;
//            } catch (Exception ex) {
//                log.error("Error parsing date7:["+str+"]:",ex);
//            }
        }
        log.error("Unknown date format:["+str+"]");
        return null;
    }
    public static long getMilliseconds(LocalDateTime localDateTime) {
        if (localDateTime == null) return 0;
        return localDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

}
