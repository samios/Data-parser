
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sami on 22/04/16.
 */
public class Program{

    private String dir;
    private String last;
    private ArrayList<String> files;
    private ArrayList<ArrayList<String>> contents;


    /**
     * Program constructor
     *
     * @param dir
     *      absolute directory path where the files are located
     * @return a program object that contains all the important infos of the file transformation
     */

    Program(String dir) {
        this.dir=dir;
        this.files= new ArrayList<String>();
        this.contents= new ArrayList<ArrayList<String>>();
    }

    /**
     *  Basic Getters and Setters
     **/

    String getDir() {
        return dir;
    }

    String getLast() {
        return this.last;
    }

    ArrayList<String> getFiles() {
        return this.files;
    }

    ArrayList<ArrayList<String>> getContents() {
        return this.contents;
    }

    void setDir(String dir) {
        this.dir = dir;
    }

    void setLast(String last) {
        this.last = last;
    }

    void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    void setContents(ArrayList<ArrayList<String>> contents) {
        this.contents = contents;
    }

    void launchP(String dir) throws IOException {
        this.dir=dir;
        this.setFiles(Scraper.getFileList(dir));
        this.setContents(Scraper.getAllContents(dir));
    }

    public static void main(String [] args) throws IOException {
        Conf.Init("data-parser.conf");
        Program main=new Program(Conf.getImportDir());
    }


}

