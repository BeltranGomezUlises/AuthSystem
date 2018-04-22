package com.auth.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alonso --- alonso@kriblet.com
 */
public class UtilsDate {

    private static final SimpleDateFormat SDF_D_MM_YYYY = new SimpleDateFormat("d/MM/yyyy");
    private static final SimpleDateFormat SDF_D_MM_YYYY_HH_MM = new SimpleDateFormat("d/MM/yyyy HH:mm   ");
    private static final SimpleDateFormat SDF_HM = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat SDF_NDOW = new SimpleDateFormat("EEEE");
    private static final SimpleDateFormat SDF_YYYY_MM_DD_T_HH_MM_SS_sssssssX = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX");
    private static final SimpleDateFormat SDF_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    //Sumatoria de tiempo en formato HH:mm
    public static String sumatoriaDeTiempos(List<String> tiempos) {
        String res = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy HH:mm");
            SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm");
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(sdf.parse("1/01/2000 00:00:00"));
            for (String tiempo : tiempos) {
                String[] horasMinutos = tiempo.split(":");
                cal.add(Calendar.HOUR_OF_DAY, Integer.valueOf(horasMinutos[0]));
                cal.add(Calendar.MINUTE, Integer.valueOf(horasMinutos[1]));
            }

            res = sdfh.format(cal.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(UtilsDate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public static String format_D_MM_YYYY(Date date) {
        return SDF_D_MM_YYYY.format(date);
    }

    public static String format_D_MM_YYYY_HH_MM(Date date) {
        return SDF_D_MM_YYYY_HH_MM.format(date);
    }

    public static String format_HH_MM(Date date) {
        return SDF_HM.format(date);
    }

    public static String format_YYYY_MM_DD_T_HH_MM_SS_sssssss(Date date) {
        return SDF_YYYY_MM_DD_T_HH_MM_SS_sssssssX.format(date);
    }

    public static String sdfUTC(Date date) {
        return SDF_UTC.format(date);
    }

    public static String nameDayOfWeek(String date, String DateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        return nameDayOfWeek(sdf.parse(date));
    }

    public static String nameDayOfWeek(String date) throws ParseException {
        return nameDayOfWeek(SDF_NDOW.parse(date));
    }

    public static String nameDayOfWeek(Date date) {
        return SDF_NDOW.format(date);
    }

    public static Date lunesAnterior(String fecha) {
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(SDF_D_MM_YYYY.parse(fecha));
        } catch (Exception e) {
        }
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) { //mientras sea mayor que lunes           
            cal.add(Calendar.DATE, -1); //restar un dia
        }
        return cal.getTime();
    }

    public static Date lunesPosterior(String fecha) {
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(SDF_D_MM_YYYY.parse(fecha));
        } catch (Exception e) {
        }
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) { //mientras sea mayor que lunes           
            cal.add(Calendar.DATE, 1); //restar un dia
        }
        return cal.getTime();
    }

    public static Date lunesAnterior(Date fecha) {
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(fecha);
        } catch (Exception e) {
        }
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) { //mientras sea mayor que lunes           
            cal.add(Calendar.DATE, -1); //restar un dia
        }
        return cal.getTime();
    }

    public static Date domingoPosterior(Date fecha) {
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(fecha);
        } catch (Exception e) {
        }
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) { //mientras sea mayor que lunes           
            cal.add(Calendar.DATE, 1); //restar un dia
        }
        return cal.getTime();
    }

    public static boolean belongsDateToWeek(String date, String week) {
        boolean res = false;
        try {
            GregorianCalendar calDate = new GregorianCalendar();
            GregorianCalendar calWeek = new GregorianCalendar();

            calDate.setTime(SDF_D_MM_YYYY.parse(date));
            calWeek.setTime(SDF_D_MM_YYYY.parse(week));

            if (calDate.get(Calendar.WEEK_OF_YEAR) == calWeek.get(Calendar.WEEK_OF_YEAR)) {
                res = true;
            }
        } catch (Exception e) {
        }
        return res;
    }

    public static class DateClass {

        private final Date date;

        public Date getDate() {
            return date;
        }

        public DateClass() {
            this.date = new Date();
        }

        public DateClass(Date date) {
            this.date = date;
        }
    }

}
