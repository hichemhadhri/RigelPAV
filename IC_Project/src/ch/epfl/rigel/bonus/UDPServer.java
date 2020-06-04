package ch.epfl.rigel.bonus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.gui.ObserverLocationBean;
import ch.epfl.rigel.gui.ViewingParametersBean;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

/**UDP Server Class 
 *  @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public class UDPServer extends Thread {
	private static final RightOpenInterval AZINTERVAL = RightOpenInterval.of(0,360);
	private static final ClosedInterval ALTINTERVAL = ClosedInterval.of(-90,90);
	private static final int SMOOTHINGFACTOR = 10;
	private final ViewingParametersBean vpBean;
	private final ObserverLocationBean olBean;
	private final Runnable updateVp;
	private final Runnable updateOl;
	private HorizontalCoordinates vpCenter;
	private GeographicCoordinates olCenter;
    private DatagramSocket socket;
    private boolean running;
    private double[] lastAzs = new double[SMOOTHINGFACTOR];
    private double[] lastAlts = new double [SMOOTHINGFACTOR];
    private int port ; 
    private String ip ; 
    private StringProperty status; 
    private ObjectProperty<Color> color; 
    private SimpleBooleanProperty mode; 
    
   
 
    public UDPServer(int port, ViewingParametersBean vp, ObserverLocationBean ol) throws SocketException, UnknownHostException {
    	this.vpBean = vp;
    	this.olBean = ol;
    	String OS = System.getProperty("os.name").toLowerCase();
    	this.port=port; 
    	if ((OS.indexOf("mac") >= 0)) {
    		this.ip = InetAddress.getLocalHost().getHostAddress();
    	}else {
    		try(final DatagramSocket dummy = new DatagramSocket()){
    			dummy.connect(InetAddress.getByName("8.8.8.8"), 10002);
    			this.ip = dummy.getLocalAddress().getHostAddress();
    		}
    		
    	}

    	socket = new DatagramSocket(port,InetAddress.getByName(this.ip));
        
        status = new SimpleStringProperty("Non connecté"); 
        socket.setSoTimeout(1000);
        color = new SimpleObjectProperty<Color>(Color.RED);
        this.mode = new SimpleBooleanProperty(false); 
        updateVp = new Runnable() {

            @Override
            public void run() {
                vpBean.setCenter(vpCenter);
            }
        };
        updateOl = new Runnable() {

            @Override
            public void run() {
                olBean.setCoordinates(olCenter);
            }
        };
        
    }
 
    public void run() {
        running = true;
        byte[] buf = new byte[256];
        while (running) {
            DatagramPacket  packet   = new DatagramPacket(buf, buf.length);
                //if no data received change status to "disconnected"
                try {
				socket.receive(packet);
                }catch(SocketTimeoutException e) {
                   status.setValue("Non connecté");
                   color.setValue(Color.RED);
                   continue; 
                }catch(IOException e) {
                	continue;
                }
				//data received
                status.setValue("Connecté");
                color.setValue(Color.GREEN);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, port);
				
				String received = new String(packet.getData(), 0, packet.getLength());
				
				updateData(received); // process received data
				try {
					socket.send(packet);
				}catch(IOException e) {
					continue;
				}

        }
        socket.close();
    }
    private void updateData(String received) {
    	
	    //process incoming geographic coordinates datas
		String[] data = received.split(" ");
		if (received.contains("geo")) {
			double lonDeg = Double.parseDouble(data[1]);
			double latDeg = Double.parseDouble(data[2]);
			this.olCenter = GeographicCoordinates.ofDeg(lonDeg, latDeg);
			Platform.runLater(updateOl);
			
		//process incoming azimuth and altitude datas	
		}else if (received.contains("vp")) {
		    if(mode.get()) {
			double az= Double.parseDouble(data[1])+90;
			double alt= Double.parseDouble(data[2])+90;
			this.vpCenter = getRollingMean(az, alt);    			
			Platform.runLater(updateVp); //update center in UI
		    }
		}
    	
    }
    
    /**port getter
     * @return port
     */
    public int getPort() {
        return port; 
    }
    
    /**Ip adress getter
     * @return Ip
     */
    public String getIP() {
        return ip; 
    }
    
    /**statusProperty getter 
     * @return statusproperty (connecté ou non connecté)
     */
    public StringProperty statusProperty() {
        return this.status; 
    }
    /**ColorProperty getter
     * @return colorProperty (red or green) 
     */
    public ObjectProperty<Color> colorProperty(){
        return color; 
    }
    
    /**modeProperty getter 
     * @return modeproperty (true when navigation mode is set on "telephone"
     */
    public SimpleBooleanProperty modeProperty() {
       
        return this.mode;
    }
    /**Calculates the rolling mean of the last number SMOOTHINGFACTOR received az and alt to create smooth animation and removes shaky screen effect 
     * @return calculated new coordinates
     */
    private HorizontalCoordinates getRollingMean(double az, double alt) {
    	double[] azs = new double[lastAzs.length];
    	double[] alts = new double[lastAlts.length];
    	double azMean=0;
    	double azSumSin=0;
    	double azSumCos=0;
    	double altMean=0;
    	//Shifting the arrays
    	System.arraycopy(lastAzs, 1, azs, 0, lastAzs.length-1);
    	azs[lastAzs.length-1] = az;
    	System.arraycopy(lastAlts, 1, alts, 0, lastAlts.length-1);
    	alts[lastAlts.length-1] = ALTINTERVAL.clip(alt);
    	lastAzs=azs;
    	lastAlts=alts;
    	
    	//Calculating mean
    	for(int i=0;i<lastAzs.length;++i ) {
    		//Circular mean for azimuth
    		azSumSin += Math.sin(Angle.ofDeg(lastAzs[i]))/lastAzs.length;
    		azSumCos += Math.cos(Angle.ofDeg(lastAzs[i]))/lastAzs.length;
    		//Regular mean for altitude
    		altMean += lastAlts[i]/lastAlts.length;
    	}
    	azMean = Angle.toDeg(Math.atan2(azSumSin, azSumCos));
    	return HorizontalCoordinates.ofDeg(AZINTERVAL.reduce(azMean), ALTINTERVAL.clip(altMean));
    }

    

}