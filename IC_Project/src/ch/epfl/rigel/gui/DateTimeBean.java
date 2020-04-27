package ch.epfl.rigel.gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class DateTimeBean {

    private ObjectProperty<LocalDate> date ; 
    private ObjectProperty<LocalTime> time  ;
    private ObjectProperty<ZoneId> zone ;
    
    public DateTimeBean() {
        this.date=new SimpleObjectProperty<>();
        this.time=new SimpleObjectProperty<>();
        this.zone=new SimpleObjectProperty<>();
    }
    
    
    public ObjectProperty<LocalDate> dateProperty(){
        return this.date;
    }
    
    public LocalDate getDate() {
        return date.get();
    }
    
    public  void setDate(LocalDate date) {
        this.date.set(date);
    }
    
    public ObjectProperty<LocalTime> timeProperty(){
        return this.time;
    }
    
    
    public LocalTime getTime() {
        return time.get();
    }
    
    public  void setTime(LocalTime time) {
        this.time.set(time);
    }
    
    public ObjectProperty<ZoneId> zoneProperty(){
        return this.zone;
    }
    
    
    public ZoneId getZone() {
        return zone.get();
    }
    
    public  void setZone(ZoneId zone) {
        this.zone.set(zone);
    }
    
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }
    
    public void setZonedDateTime(ZonedDateTime time) {
        setDate(time.toLocalDate());
        setTime(time.toLocalTime());
        setZone(time.getZone());
    }
}
