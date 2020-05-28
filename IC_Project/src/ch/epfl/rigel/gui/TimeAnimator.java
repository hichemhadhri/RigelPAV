package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TimeAnimator Class
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 */
public final class TimeAnimator extends AnimationTimer {

    private SimpleBooleanProperty running;
    private ObjectProperty<TimeAccelerator> accelerator;
    private long res;
    private DateTimeBean dateTb;

    /**
     * TimeAnimator Constructor
     * 
     * @param db
     */
    public TimeAnimator(DateTimeBean db) {
        running = new SimpleBooleanProperty();
        accelerator = new SimpleObjectProperty<>();
        dateTb = db;

    }

    /**
     * running Property
     * 
     * @return runnin property
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return this.running;
    }

    /**
     * running getter
     * 
     * @return running value
     */
    public boolean isRunning() {
        return this.running.get();
    }

    /**
     * accelerator property
     * 
     * @return
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return this.accelerator;
    }

    /**
     * Accelerator getter
     * 
     * @return accelerator
     */
    public TimeAccelerator getAccelerator() {
        return this.accelerator.get();
    }

    /**
     * Accelerator setter
     * 
     * @param ta
     *            : timeAccelerator to set
     */
    public void setAccelerator(TimeAccelerator ta) {
        this.accelerator.set(ta);
    }

    /**
     * DateTimeBean getter
     * 
     * @return dateTb
     */
    public DateTimeBean getDateTimeBean() {
        return dateTb;
    }

    @Override
    public void handle(long now) {
        if (res == 0)
            res = now;
        else {

            if (running.get()) {
                dateTb.setZonedDateTime(this.getAccelerator().adjust(dateTb.getZonedDateTime(), now - res));
            }

            res = now;
        }

    }

    @Override
    public void start() {

        super.start();
        running.setValue(true);
        ;
        res = 0;

    }

    @Override
    public void stop() {
        super.stop();
        running.setValue(false);

    }

}
