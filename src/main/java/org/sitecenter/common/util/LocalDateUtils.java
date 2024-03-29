package org.sitecenter.common.util;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.text.ParseException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LocalDateUtils {
    public static LocalDateTime getDate(String str) {
        if (str == null || str.isEmpty()) return null;
        str = str.trim();
        if (str.contains("z") || str.contains("t")) str = str.toUpperCase();
        LocalDateTime result = null;
        if (!str.contains("Z") && str.contains("T"))
            try {
                result = LocalDateTime.parse(str);
                return result;
            } catch (Exception ex) {
                log.debug("Error parsing date0:[" + str + "]:", ex);
            }
        if (str.contains("Z") && str.contains("T"))
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str);
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date1:[" + str + "]:", ex);
            }

        if (str.contains(".") && str.length() == 10) {
            String strDateFormatString = "yyyy.MM.dd";
            java.text.DateFormat df_date = new java.text.SimpleDateFormat(strDateFormatString);
            try {
                result = new java.sql.Timestamp(df_date.parse(str).getTime()).toLocalDateTime();
                return result;
            } catch (ParseException ex) {
                log.debug("Error parsing date2:[" + str + "]:", ex);
            }
        }
        if (str.contains("-") && str.length() == 10) {
            String strDateFormatString = "yyyy-MM-dd";
            java.text.DateFormat df_date = new java.text.SimpleDateFormat(strDateFormatString);
            try {
                result = new java.sql.Timestamp(df_date.parse(str).getTime()).toLocalDateTime();
                return result;
            } catch (ParseException ex) {
                log.debug("Error parsing date3:[" + str + "]:", ex);
            }
        }
        if (str.contains(" ") && str.length() == 19) {
            String strDateFormatString = "yyyy-MM-dd HH:mm:ss";
            java.text.DateFormat df_date = new java.text.SimpleDateFormat(strDateFormatString);
            try {
                result = new java.sql.Timestamp(df_date.parse(str).getTime()).toLocalDateTime();
                return result;
            } catch (ParseException ex) {
                log.debug("Error parsing date4:[" + str + "]:", ex);
            }
        }
        if (str.length() == 25 && str.contains("+") && str.contains("-") && str.contains("T")) {
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx"));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date5:[" + str + "]:", ex);
            }
        }
        if (str.contains("+") && str.contains("-") && str.contains("T")) {
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date6:[" + str + "]:", ex);
            }
        }

        if (str.contains("+") && str.contains(".") && str.contains("T")) {
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ssZ"));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date7:[" + str + "]:", ex);
            }
        }

        if (str.contains("-") && str.contains(":") && str.contains(".") && str.contains(" ")) {
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.nZ"));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date71:[" + str + "]:", ex);
            }
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.nX"));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date72:[" + str + "]:", ex);
            }
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.n"));
            } catch (Exception ex) {
                log.debug("Error parsing date73:[" + str + "]:", ex);
            }
        }
        //20240328
        if (str.length() == 8)
        try {
            int year = Integer.valueOf(str.substring(0,4));
            int month = Integer.valueOf(str.substring(4,6));
            if (year > 1900 && year < 2200 && month >0 && month <=12) {
                return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
            }
            year = Integer.valueOf(str.substring(4,8));
            if (year > 1900 && year < 2200) {
                return LocalDate.parse(str, DateTimeFormatter.ofPattern("ddMMyyyy")).atStartOfDay();
            }
        } catch (Exception ex) {
            log.debug("Error parsing date731:[" + str + "]:", ex);
        }

        if (str.contains("-") && str.contains(":") && str.contains(" ")) {
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ssZ"));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date81:[" + str + "]:", ex);
            }
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ssX"));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date82:[" + str + "]:", ex);
            }
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"));
            } catch (Exception ex) {
                log.debug("Error parsing date83:[" + str + "]:", ex);
            }

        }
        try {
            DateTime customDateTimeFromString = new DateTime(str);
            Instant instant = Instant.ofEpochMilli(customDateTimeFromString.getMillis());
            return LocalDateTime.ofInstant(instant, ZoneId.of(customDateTimeFromString.getZone().getID()));
        } catch (Exception ex) {
            log.debug("Error parsing date7 joda:[" + str + "]:", ex);
        }

        if (str.contains(" ") && str.contains("-") && str.contains(":")) {
            try {
//                String substring = str.substring(0,19);
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss z"));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date9:[" + str + "]:", ex);
            }
        }

        if (str.contains("+") && str.contains("-") && str.contains("Z")) {
            try {
                String substring = str.substring(0, 19);
                return LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            } catch (Exception ex) {
                log.debug("Error parsing date8:[" + str + "]:", ex);
            }
        }


        if (isDdMonWordYear(str)) {
            try {
                DateTimeFormatter dTF = new DateTimeFormatterBuilder().parseCaseInsensitive()
                        .appendPattern("dd-MMM-yyyy")
                        .toFormatter(Locale.ENGLISH);
                LocalDate dateTime = LocalDate.parse(str, dTF);
                return dateTime.atStartOfDay();
            } catch (Exception ex) {
                log.debug("Error parsing date101:[" + str + "]:", ex);
            }
        }


        if (str.contains(" ") && str.contains("#")) {
            try {
                LocalDate dateTime = LocalDate.parse(str.substring(0,8), DateTimeFormatter.ofPattern("yyyyMMdd"));
                return dateTime.atStartOfDay();
            } catch (Exception ex) {
                log.debug("Error parsing date102:[" + str + "]:", ex);
            }
        }

        if (str.contains(" ") && str.contains("/")  && str.contains(":")) {
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            } catch (Exception ex) {
                log.debug("Error parsing date102:[" + str + "]:", ex);
            }
        }

        if (str.contains(" ") && str.contains(".")  && str.contains(":")) {
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            } catch (Exception ex) {
                log.debug("Error parsing date1021:[" + str + "]:", ex);
            }
        }


        if (str.contains(" ") && str.contains("-") && str.contains(":")) {
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z",Locale.US));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date91:[" + str + "]:", ex);
            }
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss Z",Locale.US));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date91:[" + str + "]:", ex);
            }
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss O",Locale.US));
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneOffset.UTC);
            } catch (Exception ex) {
                log.debug("Error parsing date91:[" + str + "]:", ex);
            }
            try {
                String substring = str.substring(0, 19);
                LocalDateTime dateTime = LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"));
                log.warn("Unknown date format:[" + str + "]. Parsing as \"yyyy-MM-dd' 'HH:mm:ss\"");
                return dateTime;
            } catch (Exception ex) {
                log.debug("Error parsing date9:[" + str + "]:", ex);
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        if (str.contains(" ") && !str.contains(":"))
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US);

            try {
                LocalDate dateTime = LocalDate.parse(str, formatter);
                return dateTime.atStartOfDay();
            } catch (Exception ex) {
                log.debug("Error parsing date9:[" + str + "]:", ex);
            }
        }


        if (str.contains("T") && str.contains("-") && str.contains(":") && str.contains("Z")) {
            try {
                String substring = str.substring(0, 19);
                LocalDateTime dateTime = LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                log.warn("Unknown date format:[" + str + "]. Parsing as \"yyyy-MM-dd'T'HH:mm:ss\"");
                return dateTime;
            } catch (Exception ex) {
                log.debug("Error parsing date9:[" + str + "]:", ex);
            }
        }
        if (str.contains("T") && str.contains("-") && str.contains(":") && str.contains("UTC")) {
            try {
                String substring = str.substring(0, str.indexOf("UTC"));
                LocalDateTime dateTime = LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                log.warn("Unknown date format:[" + str + "]. Parsing as \"yyyy-MM-dd'T'HH:mm:ss\"");
                return dateTime;
            } catch (Exception ex) {
                log.debug("Error parsing date10:[" + str + "]:", ex);
            }
        }

        log.error("Unknown date format:[" + str + "]");
        return null;
    }
    public static long getMilliseconds(LocalDateTime localDateTime) {
        if (localDateTime == null) return 0;
        return localDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }
    public static boolean isDdMonWordYear(String input) {
        String regex = "^\\d{2}-(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)-\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input.toLowerCase());

        return matcher.matches();
    }

}
