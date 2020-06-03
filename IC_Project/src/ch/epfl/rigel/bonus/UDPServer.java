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
	private static final RightOpenInterval azInterval = RightOpenInterval.of(0,360);
	private static final RightOpenInterval altInterval = RightOpenInterval.of(-90,90);
	private final ViewingParametersBean vpBean;
	private final ObserverLocationBean olBean;
	private HorizontalCoordinates center;
	private double az;
	private double alt;
	private double lon;
	private double lat;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private double[] lastAzs = new double[10];
    private double[] lastAlts = new double [10];
    private int port ; 
    private String ip ; 
    private StringProperty status; 
    private ObjectProperty<Color> color; 
    private SimpleBooleanProperty mode; 
    
   
 
    public UDPServer(int port, ViewingParametersBean vp, ObserverLocationBean olBean) throws SocketException, UnknownHostException {
    	this.vpBean = vp;
    	this.olBean = olBean;
        socket = new DatagramSocket(port,InetAddress.getLocalHost());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
           this.port=port; 
           this.ip = InetAddress.getLocalHost().getHostAddress();
           status = new SimpleStringProperty("non connecté"); 
           socket.setSoTimeout(1000);
           color = new SimpleObjectProperty<Color>(Color.RED);
           this.mode = new SimpleBooleanProperty(false); 
    }
 
    public void run() {
        running = true;
        Runnable updateVp = new Runnable() {

            @Override
            public void run() {
                vpBean.setCenter(center);
            }
        };
        Runnable updateOl = new Runnable() {

            @Override
            public void run() {
                System.out.println("new geo");
                olBean.setCoordinates(GeographicCoordinates.ofDeg(lon, lat));
            }
        };
        
        while (running) {
            DatagramPacket  packet   = new DatagramPacket(buf, buf.length);
            try {
                
                //if no data received change status to "disconnected"
                try {
				socket.receive(packet);
                }catch(SocketTimeoutException e) {
                   status.setValue("non connecté");
                   color.setValue(Color.RED);
                   continue; 
                }
				//data received
				    status.setValue("connecté");
				    color.setValue(Color.GREEN);
				    InetAddress address = packet.getAddress();
	                int port = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, port);
				
				String received 
	              = new String(packet.getData(), 0, packet.getLength());
				
				updateData(received,updateVp,updateOl); // process received data
				socket.send(packet);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        socket.close();
    }
    private void updateData(String received, Runnable updateVp, Runnable updateOl) {
    	try {
    	    //process incoming geographic coordinates datas
    		String[] data = received.split(" ");
    		if (received.contains("geo")) {
    			this.lon = Double.parseDouble(data[1]);
    			this.lat = Double.parseDouble(data[2]);
    			Platform.runLater(updateOl);
    			
    		//process incoming azimuth and altitude datas	
    		}else if (received.contains("vp")) {
    		    if(mode.get()) {
    			this.az= Double.parseDouble(data[1])+90;
    			this.alt= Double.parseDouble(data[2])-90;
    			this.center = getRollingMean();    			
    			Platform.runLater(updateVp); //update center in UI
    		    }
    		}
    	}catch (Exception e) {
    		
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
    public String getIp() {
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
    /**Calculates the rolling mean of the last 10 received az and alt to create smooth animation and removes shaky screen effect 
     * @return calculated new coordinates
     */
    private HorizontalCoordinates getRollingMean() {
    	double[] azs = new double[lastAzs.length];
    	double[] alts = new double[lastAlts.length];
    	double azMean=0;
    	double azSumSin=0;
    	double azSumCos=0;
    	double altMean=0;
    	System.arraycopy(lastAzs, 1, azs, 0, lastAzs.length-1);
    	azs[lastAzs.length-1] = this.az;
    	System.arraycopy(lastAlts, 1, alts, 0, lastAlts.length-1);
    	alts[lastAlts.length-1] = altInterval.reduce(this.alt);
    	lastAzs=azs;
    	lastAlts=alts;
    	for(int i=0;i<lastAzs.length;++i ) {
    		azSumSin += Math.sin(Angle.ofDeg(lastAzs[i]))/lastAzs.length;
    		azSumCos += Math.cos(Angle.ofDeg(lastAzs[i]))/lastAzs.length;
    		
    		altMean += lastAlts[i]/lastAlts.length;
    	}
    	azMean = Angle.toDeg(Math.atan2(azSumSin, azSumCos));
    	return HorizontalCoordinates.ofDeg(azInterval.reduce(azMean), altInterval.reduce(altMean));
    }

    

}