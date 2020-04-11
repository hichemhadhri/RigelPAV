package ch.epfl.rigel.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

public  class BlackBodyColor {
    private static String  STREAM_NAME ="/bbr_color.txt";
    private static  Map<Integer,Color> map =initializeMap();
    private static ClosedInterval tempInterval = ClosedInterval.of(1000, 40000);
    
    private BlackBodyColor(){
        
    }
    
    public static Color colorForTemperature(double temp) {
      
      Preconditions.checkInInterval(tempInterval, temp);
        
        int approxTemp = (int)(Math.round(((int)temp)*0.01)*100);
       return map.get(approxTemp);

   }
   
    private static Map<Integer,Color> initializeMap(){
        Map<Integer,Color> map = new HashMap<>();
        try (InputStream inputStream = BlackBodyColor.class.getResourceAsStream(STREAM_NAME)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.US_ASCII));
            String line;
            String tempValue; 
            String colorValue;
            while(reader.ready()) {
                line = reader.readLine();
                if(line.charAt(0)=='#')
                    continue; 
                else if(line.contains("2deg"))
                    continue; 
                else {
                  tempValue=line.substring(1, 6).trim();
                  colorValue=line.substring(80,87).trim();
                  map.put(Integer.valueOf(tempValue), Color.web(colorValue));         
                }
            }
            reader.close();
            }catch(IOException e){
                throw new UncheckedIOException(e);
            }
        return map;
    }
    
}
