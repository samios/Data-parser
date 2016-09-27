import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/**
 * Created by sami on 29/07/16.
 */

public class Main implements Runnable {

    ArrayList<String> fileList;
    int k;
    public Main(ArrayList<String> list,int j)
    {
        fileList = list;
        k=j;
    }
    public void run()
    {
        FileHandler fh = null;
        // This block configure the logger with handler and formatter
        try {
            fh = new FileHandler(Conf.getLogName(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        ArrayList<String> confList = Scraper.getFileList(Conf.getParserConf());
        for(String c:confList) {


            for (String f : fileList)
                if (f.split("-")[0].equals(c.split("-")[0])) {
                    Conf.getLogger().info("Log Start for file : " + f);
                    Conf.getLogger().addHandler(fh);
                    try {
                        Parser.parse(Conf.getParserConf()+"/" + c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Scraper.toJsonTest(Conf.getImportDir() + "/" + f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
        Conf.getLogger().info("Log end");

    }

    public static void main(String[] args) throws IOException {

        Conf.Init("data-parser.conf");

        ArrayList<String>[] fileList=new ArrayList[4];
System.out.println(Conf.getLogger());
        fileList[0]=new ArrayList<String>();
        fileList[1]=new ArrayList<String>();
        fileList[2]=new ArrayList<String>();
        fileList[3]=new ArrayList<String>();
        ArrayList<String> all=Scraper.getFileList(Conf.getImportDir());
        int i=0;
        for(String s:all)
        {
            if(i<2*all.size()/4)
            {
                fileList[0].add(s);
            }
            else if(i<4*all.size()/4)
            {
                fileList[1].add(s);
            }
        i++;
        }

        new Thread(new Main(fileList[0],0)).start();

        //Get the corresponding conf files




    }
}
