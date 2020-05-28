package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;



/**
 * TimeAccelerator Interface
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
@FunctionalInterface
public interface TimeAccelerator {

    ZonedDateTime adjust(ZonedDateTime init, long delta);

    /**returns a continuous timeAccelerator
     * @param alpha : acceleration factor
     * @return continuous TimeAccelerator
     */
    public static TimeAccelerator continuous(int alpha) {
        return (init, delta) -> init.plusNanos(delta * alpha);

    }

    /**return a discrete timeAccelerator 
     * @param s : discrete step of time
     * @param v : time progression frequency
     * @return discrete timeAccelerator
     */
    public static TimeAccelerator discrete(Duration s, int v) {

        return (init, delta) -> init.plus(s.multipliedBy((long) (v * delta * 1e-9)));

    }

}
