package com.machineAdmin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsDate {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("d/MM/yyyy");
    private static final SimpleDateFormat SDFHM = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat SDFNDOW = new SimpleDateFormat("EEEE");
    private static final SimpleDateFormat SDFFULL = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX");
    private static final SimpleDateFormat SDFUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
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

    public static String sdf(Date date) {
        return SDF.format(date);
    }

    public static String sdfHM(Date date) {
        return SDFHM.format(date);
    }
    
    public static String sdfFull(Date date){
        return SDFFULL.format(date);
    }
    
    public static String sdfUTC(Date date){
        return SDFUTC.format(date);
    }

    public static String nameDayOfWeek(String date, String DateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        return nameDayOfWeek(sdf.parse(date));
    }

    public static String nameDayOfWeek(String date) throws ParseException {
        return nameDayOfWeek(SDF.parse(date));
    }

    public static String nameDayOfWeek(Date date) {
        return SDFNDOW.format(date);
    }

    public static void BubbleOrderStringOfDates(ArrayList<String> lista) {
        ArrayList<Date> dates = new ArrayList<>();
        for (String s : lista) {
            try {
                dates.add(SDF.parse(s));
            } catch (ParseException ex) {
            }
        }
        for (int i = 0; i < dates.size() - 1; i++) {
            for (int j = 0; j < dates.size() - 1 - i; j++) {
                if (dates.get(j).after(dates.get(j + 1))) {
                    //para la lista de fechas
                    Date aux = dates.get(j + 1);
                    dates.set(j + 1, dates.get(j));
                    dates.set(j, aux);
                    //su equivalente de string
                    String auxS = lista.get(j + 1);
                    lista.set(j + 1, lista.get(j));
                    lista.set(j, auxS);
                }
            }
        }

    }

    public static void QSortStringDates(ArrayList<String> dates) throws ParseException {
        ArrayList<Date> miDates = new ArrayList<>();
        for (String date : dates) {
            miDates.add(SDF.parse(date));
        }
        QuickSortStringOfDates(miDates, 0, miDates.size() - 1);
    }

    public static void QuickSortStringOfDates(ArrayList<Date> lista, int izq, int der) throws ParseException {
        int i = izq;
        int j = der;
        Date x = lista.get((izq + der) / 2);
        Date aux;
        do {
            while ((lista.get(i).compareTo(x) < 0) && (j <= der)) {
                i++;
            }
            while ((lista.get(j).compareTo(x) > 0) && (j > izq)) {
                j--;
            }
            if (i <= j) {
                aux = SDF.parse(SDF.format(lista.get(i)));
                lista.set(i, lista.get(j));
                lista.set(j, aux);
                i++;
                j--;
            }
        } while (i <= j);
        if (izq < j) {
            QuickSortStringOfDates(lista, izq, j);
        }
        if (i < der) {
            QuickSortStringOfDates(lista, i, der);
        }
    }

    public static Date lunesAnterior(String fecha) {
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(SDF.parse(fecha));
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
            cal.setTime(SDF.parse(fecha));
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

            calDate.setTime(SDF.parse(date));
            calWeek.setTime(SDF.parse(week));

            if (calDate.get(Calendar.WEEK_OF_YEAR) == calWeek.get(Calendar.WEEK_OF_YEAR)) {
                res = true;
            }
        } catch (Exception e) {
        }
        return res;
    }

}
