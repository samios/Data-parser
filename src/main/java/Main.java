import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

/**
 * Created by sami on 29/07/16.
 */

public class Main implements Runnable {

    ArrayList<String> fileList;
    int k;

    public Main(ArrayList<String> list, int j) {
        fileList = list;
        k = j;
    }

    public void run() {
        FileHandler fh = null;
        // This block configure the logger with handler and formatter
        try {
            fh = new FileHandler(Conf.getLogName(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        ArrayList<String> confList = Scraper.getFileList(Conf.getParserConf());
        for (String c : confList) {
            try {
                Parser.parse(Conf.getParserConf() + "/" + c);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String regex = Conf.getRegex();

            for (String f : fileList)
                if (!regex.equals("null"))
                    if (Pattern.matches(regex, f)) {
                        Conf.getLogger().info("Log Start for file : " + f);
                        Conf.getLogger().addHandler(fh);
                        try {
                            Scraper.toJsonTest(Conf.getImportDir() + "/" + f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            Conf.reset();

        }
        Conf.getLogger().info("Log end");
    }

    public static void main(String[] args) throws IOException {

        Conf.Init("data-parser.conf");
        int n = Integer.parseInt(Conf.getnThreads());
        ArrayList<String>[] fileList = new ArrayList[n];
        for (int i = 0; i < n; i++)
            fileList[i] = new ArrayList<String>();
        ArrayList<String> all = Scraper.getFileList(Conf.getImportDir());
        int i = 0, k = 0;
        for (String s : all) {
            fileList[k].add(s);
            if (i == (k + 1) * n * all.size() / n)
                k++;
            i++;
        }

        new Thread(new Main(fileList[0], 0)).start();

    }
}
