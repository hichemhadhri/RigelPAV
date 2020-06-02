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
	private static final ClosedInterval altInterval = ClosedInterval.of(-90,90);
	private final ViewingParametersBean vpBean;
	private HorizontalCoordinates center;
	private double az;
	private double alt;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
 
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
				
				updateData(data); // process received data
				Platform.runLater(updater); //update center in UI
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        socket.close();
    }
    private void updateData(byte[] data) {
    	try {
    		
    		//data comes in shape of a 6 * 3 * 4 array (6 sensors, each sensor has 3 axis, and each axis (float) requires 4 bytes (32 bits) according to the IEEE 754 norm 
    		byte[] az_byte = Arrays.copyOfRange(data, 36, 40);
    		this.az = ByteBuffer.wrap(az_byte).order(ByteOrder.BIG_ENDIAN).getFloat();
    		byte[] roll_byte = Arrays.copyOfRange(data, 44, 48);
    		this.alt= ByteBuffer.wrap(roll_byte).order(ByteOrder.BIG_ENDIAN).getFloat();
    		this.center = HorizontalCoordinates.ofDeg(azInterval.reduce(this.az), altInterval.clip(alt));
    	}catch (Exception e) {
    		
    	}
    }

}