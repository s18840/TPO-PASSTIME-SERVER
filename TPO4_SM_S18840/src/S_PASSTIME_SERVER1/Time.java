/**
 *
 *  @author Sroczyński Mikołaj S18840
 *
 */

package S_PASSTIME_SERVER1;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Time {
    public static String passed(String from, String to){
        String strDate="";
        String timePattern = "d MMMM yyyy (EEEE) 'godz.' HH:mm";
        String datePattern = "d MMMM yyyy (EEEE)";
        double godziny = 24.0;
        double tygodnie =7.0;

        try {
                //wzgledem czasu
                if(from.contains("T") && to.contains("T")) {

                    LocalDateTime dateFrom = LocalDateTime.parse(from);
                    LocalDateTime dateTo = LocalDateTime.parse(to);
                    LocalDate localDate, localDate1;
                    LocalTime localTime, localTime1;

                    ZonedDateTime zonedFrom = ZonedDateTime.of(dateFrom, ZoneId.of("Europe/Warsaw"));
                    ZonedDateTime zonedTo = ZonedDateTime.of(dateTo, ZoneId.of("Europe/Warsaw"));

                    //DateTimeFormatter dtf =new DateTimeFormatter();
                    LocalDateTime localFirstDate = LocalDateTime.parse(from);
                    LocalDateTime localSecondDate = LocalDateTime.parse(to);


                    strDate = strDate + "Od " + dateFrom.format(DateTimeFormatter.ofPattern(timePattern)) +
                            " do " + dateTo.format(DateTimeFormatter.ofPattern(timePattern)) + "\n" +
                            " - mija: " + Math.round(ChronoUnit.HOURS.between(zonedFrom, zonedTo) / godziny) + " " +
                            (Math.round(ChronoUnit.HOURS.between(zonedFrom, zonedTo) / godziny) != 1 ? "dni" : "dzień") +
                            ", tygodni " + Math.round(Math.round(ChronoUnit.HOURS.between(zonedFrom, zonedTo) / godziny) / tygodnie * 100) / 100.0 + "\n" +
                            " - godzin: " + ChronoUnit.HOURS.between(zonedFrom, zonedTo) +
                            ", minut: " + ChronoUnit.MINUTES.between(zonedFrom, zonedTo) + "\n";

                    strDate = strDate + getDaysCalender(dateFrom.toLocalDate(), dateTo.toLocalDate());
                //bez czasu
                }else{

                    LocalDate localDate, localDate1;
                    LocalDate dateFrom = LocalDate.parse(from);
                    LocalDate dateTo = LocalDate.parse(to);
                    //DateTimeFormatter dtf =new DateTimeFormatter();


                    strDate = strDate +"Od "+dateFrom.format( DateTimeFormatter.ofPattern(datePattern))+
                            " do "+dateTo.format( DateTimeFormatter.ofPattern(datePattern))+"\n" +
                            "- mija: "+ ChronoUnit.DAYS.between(dateFrom,dateTo)+" "+
                            (ChronoUnit.DAYS.between(dateFrom,dateTo)!=1?"dni":"dzień")+
                            ", tygodni "+Math.round(ChronoUnit.DAYS.between(dateFrom,dateTo)/tygodnie*100)/100.0+"\n"
                            +getDaysCalender(dateFrom,dateTo);

                }
        }catch(Exception e){
            return "***"+e.toString();
        }

        return strDate;
    }
    private static String getDaysCalender(LocalDate dateFrom,LocalDate dateTo){
        if(ChronoUnit.DAYS.between(dateFrom,dateTo)>=1) {
            String days="";
            String months="";
            String years="";

            Period p = Period.between(dateFrom,dateTo);
            //oblicznie lat
            switch (p.getYears()){
                case 1:
                    years="1 rok";
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    years=p.getYears()+" lata";
                    break;
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    years=p.getYears()+" lat";
                    break;
            }
            if(years!="")
                months+=", ";
            switch (p.getMonths()) {
                case 1:
                    months += "1 miesiąc";
                    break;
                case 2:
                case 3:
                case 4:
                    months += p.getMonths() + " miesiące";
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    months += p.getMonths() + " miesiecy";
                    break;
            }
            if(years!="" && months!="")
                days+=", ";
            switch (p.getDays()) {
                case 1:
                    days += " 1 dzień";
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                    days+=p.getDays()+" dni";
                    break;

            }
            String periodString =" - kalendarzowo: "+years+months+days;

            return periodString;
        }
        return "";
    }
}

