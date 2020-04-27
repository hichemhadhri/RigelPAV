package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

import ch.epfl.rigel.math.Angle;

@FunctionalInterface
public interface TimeAccelerator {
    
    ZonedDateTime adjust(ZonedDateTime init, long delta); 
    
    public static TimeAccelerator continuous(int alpha) {
        return  (init,delta) -> init.plusNanos(delta*alpha);
        
        
        }
    
public static TimeAccelerator discrete(Duration s, int v) {
        
        return (init,delta) -> init.plus(s.multipliedBy((long) (v*delta*1e-9)));
        
        }
    
}
