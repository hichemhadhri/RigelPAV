package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;

/**
 * AsterismLoader enum
 * 
 * @author Mohamed Hichem Hadhri (300434)
 * @author Khalil Haroun Achache (300350)
 *
 */
public enum AsterismLoader implements StarCatalogue.Loader {
	INSTANCE;

	@Override
	public void load(InputStream inputStream, Builder builder) throws IOException{
		Map<Integer, Star> starById = starById(builder);
		String[] asterismElements;
		List<Star> stars = new ArrayList<Star>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))){
			do {
				asterismElements = reader.readLine().split(",");
				stars.clear();
				
				for (int i = 0; i < asterismElements.length; ++i) {
					stars.add(getStarByHippar(Integer.parseInt(asterismElements[i]), starById));
				}
				builder.addAsterism(new Asterism(stars));
			} while (reader.ready());
		}catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private Star getStarByHippar(Integer hipparCosId, Map<Integer, Star> map) {
		return map.get(hipparCosId);
	}

	private Map<Integer, Star> starById(Builder builder) {
		Map<Integer, Star> result = new HashMap<Integer, Star>();
		for (Star star : builder.stars()) {
			result.put(star.hipparcosId(), star);
		}
		return result;
	}
}
