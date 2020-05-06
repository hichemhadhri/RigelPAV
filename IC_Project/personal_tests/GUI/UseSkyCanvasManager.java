package GUI;

import ch.epfl.rigel.gui.NamedTimeAccelerator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class UseSkyCanvasManager   {
	  
	      public static void main(String[] args) {
	          ObjectProperty<NamedTimeAccelerator> p1 =
	                  new SimpleObjectProperty<>(NamedTimeAccelerator.TIMES_1);
	                ObjectProperty<String> p2 =
	                  new SimpleObjectProperty<>();

	                p2.addListener((p, o, n) -> {
	                    System.out.printf("old: %s  new: %s%n", o, n);
	                  });

	                p2.bind(Bindings.select(p1, "name"));
	                p1.set(NamedTimeAccelerator.TIMES_30);
	        }
	      
	      
	  }

	     
	