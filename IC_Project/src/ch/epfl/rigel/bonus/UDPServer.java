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
import java.util.Timer;
import java.util.TimerTask;


import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.gui.ViewingParametersBean;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

public class UDPServer extends Thread {
	private static final ClosedInterval azInterval = ClosedInterval.of(0,360);
	private static final ClosedInterval altInterval = ClosedInterval.of(-90,90);
	private double az;
	private double alt;
	 private boolean first ;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
 
    public UDPServer(int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket(port,InetAddress.getLocalHost());
        System.out.println(InetAddress.getLocalHost().getHostAddress());

    }
 
    public void run() {
    	
        running = true;
        
 
        while (running) {
            DatagramPacket packet 
              = new DatagramPacket(buf, buf.length);
            try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if(!first) {	
        		first = true ;
        		continue ;
        	}
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            byte[] data = packet.getData();
            
            
            
            String received 
              ="";// new String(packet.getData(), 0, packet.getLength());
             
            //System.out.println(received);
            updateData(data);
            try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        socket.close();
    }
    private void updateData(byte[] data) {
    	try {
    		byte[] az_byte = Arrays.copyOfRange(data, 36, 40);
    		this.az = ByteBuffer.wrap(az_byte).order(ByteOrder.BIG_ENDIAN).getFloat();
    		byte[] roll_byte = Arrays.copyOfRange(data, 44, 48);
    		this.alt= ByteBuffer.wrap(roll_byte).order(ByteOrder.BIG_ENDIAN).getFloat();
    		if(this.alt<0)
    			this.alt= this.alt+360;
    		if(this.az<0)
    			this.az=this.az+360;
    		
    		
    	}catch (Exception e) {
    		
    	}
    }
    public HorizontalCoordinates getCoords() {
    	return HorizontalCoordinates.ofDeg(azInterval.clip(this.az), altInterval.clip(alt));
    }
}