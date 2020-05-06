package ch.epfl.rigel.gui;

import java.time.ZonedDateTime;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class TimeAnimator extends AnimationTimer {

    private SimpleBooleanProperty running; 
    private ObjectProperty<TimeAccelerator> accelerator ;
    private long res ; 
    private DateTimeBean d;
    
    public TimeAnimator(DateTimeBean db) {
        running=new SimpleBooleanProperty(); 
        accelerator=new SimpleObjectProperty<>();
        d=db;
        
        
    }
    
    public ReadOnlyBooleanProperty runningProperty() {
        return this.running;
    }
    public boolean isRunning() {
        return this.running.get();
    }
    
    public ObjectProperty<TimeAccelerator> acceleratorProperty(){
        return this.accelerator;
    }
    
    public TimeAccelerator getAccelerator() {
        return this.accelerator.get();
    }
    
    public void setAccelerator(TimeAccelerator ta) {
        this.accelerator.set(ta);
    }
    
    public DateTimeBean getDateTimeBean() {
       return d; 
    }
    
    @Override
    public void handle(long now) {
        if(res==0)
            res=now;
        else 
        {
       
        
        if(running.get()) {
            d.setZonedDateTime(this.getAccelerator().adjust(d.getZonedDateTime(), now-res));
        }
        
        res= now ;
        }
        
        
    }
    @Override
    public void start() {
        
        super.start();
        running.setValue(true);;
        res=0;
        
    }
    @Override 
    public void stop() {
        super.stop();
        running.setValue(false);
        
        
    }
    
    

}
