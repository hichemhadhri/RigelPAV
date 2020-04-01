package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;

/**
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public enum AsterismLoader implements StarCatalogue.Loader{
	INSTANCE;

	@Override
	public void load(InputStream inputStream, Builder builder) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.US_ASCII));
		String[] asterismElements;
		List<Star>stars=new ArrayList<Star>();
		while(reader.ready()) {
			asterismElements=reader.readLine().split(",");
			stars.clear();
			for(int i=0;i<asterismElements.length;++i) {
				stars.add(getStarByHippar(Integer.parseInt(asterismElements[i]),builder));
			}
			builder.addAsterism(new Asterism(stars));
		}
		reader.close();
	}
	
	private Star getStarByHippar(int hipparCosId,Builder builder) {
		Iterator<Star> iterator = builder.stars().iterator();
		Star next;
		while(iterator.hasNext()) {
			next=iterator.next();
			if(hipparCosId==next.hipparcosId())
				return next;
		}
		return null;
	}
}
