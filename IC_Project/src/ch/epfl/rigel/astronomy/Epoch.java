package ch.epfl.rigel.astronomy;

import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate; 
import java.time.LocalTime; 
import java.time.ZoneOffset;


/**
 * Epoch enum (J200 and J2010)
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public enum Epoch {
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),LocalTime.of(12,0),ZoneOffset.UTC)),
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),LocalTime.of(0,0),ZoneOffset.UTC)); 
    
    private final ZonedDateTime date; 
    private Epoch(ZonedDateTime date) {
        this.date=date; 
    }
    
    /**Returns number of days between two dates
     * @param when  
     * @return number of days between the current Epoch and when 
     */
    public double daysUntil(ZonedDateTime  when) {
        double res= date.until(when, ChronoUnit.MILLIS);
        return res / 8.64e+7;
    }
    
    /**Returns number of julian centuries between two dates
     * @param when
     * @return number of julian centuries between the current Epoch and when 
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        double res = date.until(when, ChronoUnit.MILLIS);
        return (res / 8.64e+7 ) / 36525;
    }
}
