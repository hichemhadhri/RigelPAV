package ch.epfl.rigel.bonus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;


import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.gui.ViewingParametersBean;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.application.Platform;

public class UDPServer extends Thread {
	private static final RightOpenInterval azInterval = RightOpenInterval.of(0,360);
	private static final RightOpenInterval altInterval = RightOpenInterval.of(-90,90);
	private final ViewingParametersBean vpBean;
	private HorizontalCoordinates center;
	private double az;
	private double alt;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private double[] lastAzs = new double[10];
    private double[] lastAlts = new double [10];
 
    public UDPServer(int port, ViewingParametersBean vp) throws SocketException, UnknownHostException {
    	this.vpBean = vp;
        socket = new DatagramSocket(port,InetAddress.getLocalHost());
        System.out.println(InetAddress.getLocalHost().getHostAddress());

    }
 
    public void run() {
        running = true;
        Runnable updater = new Runnable() {

            @Override
            public void run() {
                vpBean.setCenter(center);
            }
        };
        while (running) {
            DatagramPacket packet 
              = new DatagramPacket(buf, buf.length);
            try {
				socket.receive(packet);
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, port);
				byte[] data = packet.getData();
				/*for(int i=0;i<data.length;++i) {
					System.out.print(data[i]+" ");
				}*/
				String received 
	              = new String(packet.getData(), 0, packet.getLength());
				//System.out.println(received);
				updateData(received); // process received data
				Platform.runLater(updater); //update center in UI
				//socket.send(null);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        socket.close();
    }
    private void updateData(String received) {
    	try {
    		String[] data = received.split(" ");
    		
    		//data comes in shape of a 6 * 3 * 4 array (6 sensors, each sensor has 3 axis, and each axis (float) requires 4 bytes (32 bits) according to the IEEE 754 norm 
    		/*byte[] az_byte = Arrays.copyOfRange(data, 36, 40);
    		this.az = ByteBuffer.wrap(az_byte).order(ByteOrder.BIG_ENDIAN).getFloat();*/
    		this.az= Double.parseDouble(data[0])+90;
    		/*byte[] roll_byte = Arrays.copyOfRange(data, 44, 48);
    		this.alt= ByteBuffer.wrap(roll_byte).order(ByteOrder.BIG_ENDIAN).getFloat();*/
    		//this.center = HorizontalCoordinates.ofDeg(azInterval.reduce(this.az), altInterval.clip(this.alt));
    		this.alt= Double.parseDouble(data[2])-90;
    		this.center = getRollingMean();
    	}catch (Exception e) {
    		
    	}
    }
    
    private HorizontalCoordinates getRollingMean() {
    	double[] azz = new double[lastAzs.length];
    	double[] altt = new double[lastAlts.length];
    	double azMean=0;
    	double altMean=0;
    	System.arraycopy(lastAzs, 1, azz, 0, lastAzs.length-1);
    	azz[lastAzs.length-1] = azInterval.reduce(this.az);
    	System.arraycopy(lastAlts, 1, altt, 0, lastAlts.length-1);
    	altt[lastAlts.length-1] = altInterval.reduce(this.alt);
    	lastAzs=azz;
    	lastAlts=altt;
    	for(int i=0;i<lastAzs.length;++i ) {
    		azMean += lastAzs[i]/lastAzs.length;
    		altMean += lastAlts[i]/lastAlts.length;
    	}
    	
    	return HorizontalCoordinates.ofDeg(azInterval.reduce(azMean), altInterval.reduce(altMean));
    }
    

}