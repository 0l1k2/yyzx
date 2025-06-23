package com.cy.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author
 * @version 1.0
 * @data 9:17
 */
//@Component
//public class DateConverter implements Converter<String, Date> {
//    private static final List<String> formarts = new ArrayList<>(4);
//        static {
//            formarts.add("yyyy-MM-dd HH:mm:ss");
//            formarts.add("yyyy-MM-dd HH:mm");
//            formarts.add("yyyy-MM-dd");
//            formarts.add("yyyy/MM");
//        }
//
//    @Override
//    public Date convert(String source){
//        String value = source.trim();
//        if ("".equals(value)) {
//            return null;
//        }
//        if (source.matches("^\\d{4}-\\d{1,2}$")) {
//            return parseDate(source, formarts.get(0));
//        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
//            return parseDate(source, formarts.get(1));
//        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
//            return parseDate(source, formarts.get(2));
//        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
//            return parseDate(source, formarts.get(3));
//        } else {
//            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
//        }
//    }
//    private Date parseDate(String source, String format) {
//         Date date = null;
//         try {
//             DateFormat dateFormat=new SimpleDateFormat(format);
//             date=dateFormat.parse(dataStr);
//         } catch (ParseException e) {
//             e.printStackTrace();
//         }
//         return date;
//        }
@Component
public class DateConverter implements Converter<String, Date> {
    private static final List<String> formats = new ArrayList<>(4);
    static {
        formats.add("yyyy-MM-dd HH:mm:ss");  // For full datetime
        formats.add("yyyy-MM-dd HH:mm");     // For datetime without seconds
        formats.add("yyyy-MM-dd");           // For date only
        formats.add("yyyy/MM");              // For year/month (slash-separated)
    }

    @Override
    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        // Check patterns from most specific to least specific
        if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formats.get(0));  // yyyy-MM-dd HH:mm:ss
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formats.get(1));  // yyyy-MM-dd HH:mm
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formats.get(2));  // yyyy-MM-dd
        } else if (source.matches("^\\d{4}/\\d{1,2}$")) {
            return parseDate(source, formats.get(3));  // yyyy/MM
        } else {
            throw new IllegalArgumentException("Invalid date value '" + source + "'");
        }
    }

    private Date parseDate(String source, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(false); // Strict parsing
            return dateFormat.parse(source);  // Fixed variable name
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format for '" + source + "'");
        }
    }
}
