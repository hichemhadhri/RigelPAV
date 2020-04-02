package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.Test;

class MyHygDatabaseLoaderTest {
    private static final String HYG_CATALOGUE_NAME =
      "/hygdata_v3.csv";
    private final static String AST_CATALOGUE_NAME = "/asterisms.txt";

    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
      try (InputStream hygStream = getClass()
         .getResourceAsStream(HYG_CATALOGUE_NAME)) {
        assertNotNull(hygStream);
      }
    }

    @Test
    void hygDatabaseContainsRigel() throws IOException {
      try (InputStream hygStream = getClass()
         .getResourceAsStream(HYG_CATALOGUE_NAME)) {
          try(InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME)){
        StarCatalogue catalogue = new StarCatalogue.Builder()
      .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
      .loadFrom(astStream, AsterismLoader.INSTANCE)
      .build();
//        Star rigel = null;
//        for (Star s : catalogue.stars()) {
//      if (s.name().equalsIgnoreCase("rigel"))
//        rigel = s;
//         break ; 
//        }
//        assertNotNull(rigel);
        
        int i = 0;
        for(Star star : catalogue.stars()) {
         if (star.name().charAt(0) == '?') {
          i = 1;
          
          assertEquals(' ', star.name().charAt(1));}}
          assertEquals(1,i);
          Queue<Asterism> a = new ArrayDeque<>();
          Star beltegeuse = null;
          for (Asterism ast : catalogue.asterisms()) {
          for (Star s : ast.stars()) {if (s.name().equalsIgnoreCase("Rigel")) { a.add(ast);}}}
      int astCount = 0;
      for (Asterism ast : a) {
           ++astCount;
          for (Star s : ast.stars()) {if (s.name().equalsIgnoreCase("Betelgeuse")) { beltegeuse = s; }}}
      assertNotNull(beltegeuse);
      assertEquals(2,astCount);
      }
      }
    }
    
    
    

    
private static final String ASTERISM_CATALOGUE_NAME =
        "/asterisms.txt";
@Test
void variousTestsAndReadablePrintfOnCompletelyFinishedStarCatalogue() throws IOException {
    try (InputStream hygStream = getClass()
            .getResourceAsStream(HYG_CATALOGUE_NAME)) {
        InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
        StarCatalogue catalogue = new StarCatalogue.Builder()
                .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                .build();
        Star rigel = null;
        for (Star s : catalogue.stars()) {
            if (s.name().equalsIgnoreCase("rigel"))
                rigel = s;
        }
        assertNotNull(rigel);

        List<Star> allStar = new ArrayList<Star>();
       // allStar.clear();
        allStar.addAll(catalogue.stars());

        System.out.println("LIST OF STARS :");
        for(Star s : allStar){
            System.out.println(s.hipparcosId() + " ");
        } //should print out the same star IDS as in the fichier (check visually)
        System.out.println();
        System.out.println();

        System.out.println("ASTERISMS : ");
        int i;

        //vérifier visuellement en utilisant CTRL-F que les astérismes contenu dans ASTERISMS sont bien les memes
        //flemme de coder une méthode qui vérifie automatiquement
        Iterator<Asterism> asterismsIterator = catalogue.asterisms().iterator();
        Asterism asterism; 
        while(asterismsIterator.hasNext()) {
            System.out.println("hello ");
            asterism = asterismsIterator.next();
          
            List<Integer> cAstInd = catalogue.asterismIndices(asterism);
            
            System.out.println(cAstInd);
            System.out.println(cAstInd.get(0));
            i = 0;
            for(Star star : asterism.stars()){
                System.out.println(i);
                System.out.print("Hip : ");
                System.out.print(star.hipparcosId());
                System.out.print("  foundHipparcos : ");
                System.out.print(catalogue.stars().get(cAstInd.get(i)).hipparcosId());

                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                l'index stoqué dans l'astérisme voulu : */
                assertEquals(allStar.get(cAstInd.get(0)).hipparcosId(), asterism.stars().get(0).hipparcosId());
                System.out.print(" ||| ");
                i++;
            }
            System.out.println();
        }
    }
}
  }