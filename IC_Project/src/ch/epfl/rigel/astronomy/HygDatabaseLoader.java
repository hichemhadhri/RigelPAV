package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**HygDatabaseLoader class 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {
	INSTANCE;
	
	private enum DatabaseColumn{
		ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
		RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
		RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
		COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
	}

	@Override
	public void load(InputStream inputStream, Builder builder) throws IOException {
		
		String[] starElements;
		Star star;
		String starName;
		int starHippar;
		EquatorialCoordinates coord;
		double magnitude;
		double bvColor;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.US_ASCII))){
			reader.readLine(); //Skipping the first line that contains the column names
			while(reader.ready()) {
				starElements = reader.readLine().split(",");
				starHippar = Integer.parseInt(replaceEmpty(starElements[DatabaseColumn.HIP.ordinal()],"0"));
				starName = replaceEmpty(starElements[DatabaseColumn.PROPER.ordinal()],
						replaceEmpty(starElements[DatabaseColumn.BAYER.ordinal()],"?")+" "+starElements[DatabaseColumn.CON.ordinal()]);
				coord = EquatorialCoordinates.of(Double.parseDouble(starElements[DatabaseColumn.RARAD.ordinal()]), Double.parseDouble(starElements[DatabaseColumn.DECRAD.ordinal()]));
				magnitude = Double.parseDouble(replaceEmpty(starElements[DatabaseColumn.MAG.ordinal()],"0"));
				bvColor = Double.parseDouble(replaceEmpty(starElements[DatabaseColumn.CI.ordinal()],"0"));
				star = new Star(starHippar,starName,coord,(float)magnitude,(float)bvColor);
				builder.addStar(star);
			}
			
		}catch(IOException exception) {
			exception.printStackTrace();
		}
	}
	private String replaceEmpty(String original,String replacement) {
		return original.length() == 0 ? replacement : original;
	}

}
