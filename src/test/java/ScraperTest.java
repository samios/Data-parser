import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by sami on 22/04/16.
 */

public class ScraperTest {

    //If the directory doesn't exist
    @Test
    public void getWrongDir() throws Exception {
        ArrayList<String> tmp = new ArrayList<String>();
        assertEquals(tmp, Scraper.getFileList("/home/sami/IdeaProjects/Bubendorff/test/Wrong"));

    }

    //If the directory is empty
    @Test
    public void getEmptyFileList() throws Exception {
        ArrayList<String> tmp = new ArrayList<String>();
        assertEquals(tmp, Scraper.getFileList("/home/sami/IdeaProjects/Bubendorff/Integration/rapportsEmptydir"));

    }

    // get correct File list from directory
    @Test
    public void getFileList() throws Exception {
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add("RapportMCI4[1][27 02 2015].txt");
        assertEquals(tmp, Scraper.getFileList("/home/sami/IdeaProjects/Integration/rapports"));

    }

    // Get empty content content from empty file

    @Test
    public void getEmptyContents() throws Exception {
        ArrayList<ArrayList<String>> actual;
        ArrayList<String> files = new ArrayList<String>();
        files.add("/home/sami/IdeaProjects/Bubendorff/test/input/Emptyfile.txt");
        actual = Scraper.getAllContents("/home/sami/IdeaProjects/Bubendorff/test/input"); // Bubendorf file used for test
        ArrayList<ArrayList<String>> expected = new ArrayList<ArrayList<String>>();
        assertEquals(actual, expected);
    }

    // Get all correct content from a file
    @Test
    public void getAllContents() throws Exception {
        ArrayList<ArrayList<String>> actual;
        actual = Scraper.getAllContents("/home/sami/IdeaProjects/Integration/rapports/RapportMCI4[1][27 02 2015].txt"); // Bubendorf file used for test
        ArrayList<ArrayList<String>> expected = new ArrayList<ArrayList<String>>();
        expected.add(new ArrayList<String>(Arrays.asList("Passed;;27/02/2015;16:12:59;Num Serie;KIT1MOJM;Reference;41699;Nomenclature;N;Categorie;BT010;StationId;PTIZ569-01;NumeroPuits;1;Vérification cohérence SN;Passed;Présence varistance;Passed;Isolation Phase/Neutre;1120645.28;800000;1500000;".split(";"))));
        assertEquals(expected,actual);
    }

    // Show error message
    @Test
    public void getErrorContents() throws Exception {
        ArrayList<ArrayList<String>> actual;
        ArrayList<String> files = new ArrayList<String>();
        files.add("/home/sami/IdeaProjects/Bubendorff/test/input/Error.txt");
        actual = Scraper.getAllContents("/home/sami/IdeaProjects/Bubendorff/test/input"); // Bubendorf file used for test
        ArrayList<ArrayList<String>> expected = new ArrayList<ArrayList<String>>();
        assertEquals(actual, expected);
    }
}

