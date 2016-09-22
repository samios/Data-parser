import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
            fh = new FileHandler("LogFile.log",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        ArrayList<String> confList = Scraper.getFileList("/home/vnc/Conversion/Conf");
        for(String c:confList) {


            for (String f : fileList)
                if (f.split("-")[0].equals(c.split("-")[0])) {
                    Conf.getLogger().info("Log Start for file : " + f);
                    Conf.getLogger().addHandler(fh);
                    try {
                        Parser.parse("/home/vnc/Conversion/Conf/" + c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Scraper.toJsonTest(Conf.getDirectory() + "/" + f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
        System.out.println("thread"+k);

        Conf.getLogger().info("Log end");
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String>[] fileList=new ArrayList[4];
       fileList[0]=new ArrayList<String>();
       fileList[1]=new ArrayList<String>();
       fileList[2]=new ArrayList<String>();
       fileList[3]=new ArrayList<String>();
        ArrayList<String> all=Scraper.getFileList("/home/data/BT010-01/RI3");
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

        }
        new Thread(new Main(fileList[0],0)).start();
        new Thread(new Main(fileList[1],1)).start();

        //Get the corresponding conf files




    }
}
