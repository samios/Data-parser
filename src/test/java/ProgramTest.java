import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by sami on 22/04/16.
 */
public class ProgramTest {
    @Test
    public void getDir() throws Exception {
        Program P=new Program("test");
        assertEquals("test",P.getDir());
    }



    @Test
    public void getLast() throws Exception {
        Program P=new Program("test");
        P.setLast("test");
        assertEquals("test",P.getLast());

    }

    @Test
    public void getFiles() throws Exception {
        Program P=new Program("test");
        ArrayList<String> a=new ArrayList<String>();
        a.add("test");
        P.setFiles(a);
        assertEquals(a,P.getFiles());

    }

    @Test
    public void setDir() throws Exception
    {
        Program P=new Program("test");
        P.setDir("test2");
        assertEquals("test2",P.getDir());
    }

    @Test
    public void getContents() throws Exception {
        Program P=new Program("test");
        ArrayList<String> a=new ArrayList<String>();
        a.add("test");
        ArrayList<ArrayList<String>> c= new ArrayList<ArrayList<String>>();
        c.add(a);
        P.setContents(c);
        assertEquals(c,P.getContents());
    }

    @Test
    public void get() throws Exception {
        Program P=new Program("/home/sami/IdeaProjects/Integration/src/test/files/list/rapport.txt");
        P.launchP("/home/sami/IdeaProjects/Integration/src/test/files/list");
        ArrayList<String> expected1=new ArrayList<String>();
        expected1.add("rapport.txt");
        expected1.add("empty.txt");
        ArrayList<ArrayList<String>> expected2=new ArrayList<ArrayList<String>>();
        expected2.add(new ArrayList<String>(Arrays.asList("Passed;;27/02/2015;16:12:59;Num Serie;KIT1MOJM;Reference;41699;Nomenclature;N;Categorie;BT010;StationId;PTIZ569-01;NumeroPuits;1;Vérification cohérence SN;Passed;Présence varistance;Passed;Isolation Phase/Neutre;1120645.28;800000;1500000;".split(";"))));
        assertEquals("/home/sami/IdeaProjects/Integration/src/test/files/list",P.getDir());
        assertEquals(expected1,P.getFiles());
        assertEquals(expected2,Scraper.getAllContents("/home/sami/IdeaProjects/Integration/src/test/files/list/rapport.txt"));
    }
    @Test
    public void getEmptyDir() throws Exception {
        Program P=new Program("/home/sami/IdeaProjects/Integration/src/test/files/test.txt");
        P.launchP("/home/sami/IdeaProjects/Integration/src/test/files/emptydir");
        ArrayList<String> expected1=new ArrayList<String>();
        ArrayList<ArrayList<String>> expected2=new ArrayList<ArrayList<String>>();
        assertEquals("/home/sami/IdeaProjects/Integration/src/test/files/emptydir",P.getDir());
        assertEquals(expected1,P.getFiles());
        assertEquals(expected2,P.getContents());
    }
}
