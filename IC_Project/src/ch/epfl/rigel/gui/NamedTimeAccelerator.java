package ch.epfl.rigel.gui;

import java.time.Duration;



/**NamedTimeAccelerator enum
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public enum NamedTimeAccelerator {
  TIMES_1("1x",TimeAccelerator.continuous(1)),
  TIMES_30("30x",TimeAccelerator.continuous(30)),
  TIMES_300("300x",TimeAccelerator.continuous(300)),
  TIMES_3000("3000x",TimeAccelerator.continuous(3000)),
  DAY("jour",TimeAccelerator.discrete(Duration.parse("PT24H00M0S"),60)),
  SIDEREAL_DAY("jour sidéral",TimeAccelerator.discrete(Duration.parse("PT23H56M4S"),60));
    
    private String nom  ;
    private TimeAccelerator accelerator;
    
    
    private  NamedTimeAccelerator(String nom,TimeAccelerator acc ) {
        this.nom=nom; 
        this.accelerator = acc; 
        
    }
    
    /**Name getter
     * @return name
     */
    public String  getName() {
        return this.nom; 
    }
    
    /**Accelerator getter
     * @return time Accelerator
     */
    public TimeAccelerator getAccelerator() {
        return this.accelerator;
    }
    
    
    
    
    
    @Override 
    public String toString() {
        return this.getName();
    }
}
