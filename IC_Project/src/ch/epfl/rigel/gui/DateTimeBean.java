package ch.epfl.rigel.gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * DateTimeBean class
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public final class DateTimeBean {

    private ObjectProperty<LocalDate> date;
    private ObjectProperty<LocalTime> time;
    private ObjectProperty<ZoneId> zone;

    /**
     * DateTimeBean Constructor
     * 
     */
    public DateTimeBean() {
        this.date = new SimpleObjectProperty<>();
        this.time = new SimpleObjectProperty<>();
        this.zone = new SimpleObjectProperty<>();
    }

    /**
     * date property getter
     * 
     * @return date property
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return this.date;
    }

    /**
     * date getter
     * 
     * @return
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * date setter
     * 
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    /**
     * time property getter
     * 
     * @return time property
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return this.time;
    }

    /**
     * time getter
     * 
     * @return time
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     * time setter
     * 
     * @param time
     */
    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    /**
     * zone property getter
     * 
     * @return zone property
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return this.zone;
    }

    /**
     * zone getter
     * 
     * @return
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     * zone setter
     * 
     * @param zone
     */
    public void setZone(ZoneId zone) {
        this.zone.set(zone);
    }

    /**
     * returns a zonedDateTime
     * 
     * @return zonedDateTime
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }

    /**
     * set a new zonedDatetTime
     * 
     * @param time
     *            : zonedDateTime to set
     */
    public void setZonedDateTime(ZonedDateTime time) {
        setDate(time.toLocalDate());
        setTime(time.toLocalTime());
        setZone(time.getZone());
    }
}
