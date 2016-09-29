package com.cordon;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by sami on 29/07/16.
 */

public class Main implements Runnable {

    private ArrayList<String> fileList;
    private final int nt;

    private Main(ArrayList<String> list, int j) {
        fileList = list;
        nt = j;
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
        assert fh != null;
        fh.setFormatter(formatter);
        ArrayList<String> confList = Scraper.getFileList(Conf.getParserConf());
        for (String c : confList) {
            try {
                Parser.parse(Conf.getParserConf() + "/" + c);

            } catch (IOException e) {
                e.printStackTrace();
            }
            String regex = Conf.getRegex();

            for (String f : fileList) {

                File tmp = new File(Conf.getImportDir() + "/" + f);
                if (tmp.exists())
                    if (!regex.equals("null"))

                        if (Pattern.matches(regex, f)) {

                            Conf.getLogger().info("Log Start for file : " + f);
                            Conf.getLogger().addHandler(fh);
                            try {

                                Scraper.toJsonTest(Conf.getImportDir() + "/" + f);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (tmp.exists()) {
                                    Files.move(FileSystems.getDefault().getPath(Conf.getImportDir() + "/" + f), FileSystems.getDefault().getPath(Conf.getNonProcessedDir() + "/" + f), REPLACE_EXISTING);
                                    Conf.getLogger().info("File " + f + " moved to non processed folder");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

            }
            Conf.reset();
        }
        Conf.getLogger().info("Log end");
    }

    public static void main(String[] args) throws IOException {

        Conf.Init();
        int n = Integer.parseInt(Conf.getnThreads());
        ArrayList<String>[] afileList = new ArrayList[n];
        for (int i = 0; i < n; i++)
            afileList[i] = new ArrayList<String>();
        ArrayList<String> all = Scraper.getFileList(Conf.getImportDir());
        int i = 0, k = 0;
        for (String s : all) {
            afileList[k].add(s);
            if (i == (k + 1) * all.size() / n)
                k++;
            i++;
        }

        i=0;
        Thread[] t = new Thread[n];
        for (ArrayList<String> tl : afileList)
        {

            t[i] = new Thread(new Main(tl, i));
            t[i].start();

            i++;
        }        try {
            for (i = 0; i < n; i++) {

                t[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String f : all) {
            File tmp = new File(Conf.getImportDir() + "/" + f);
            try {
                if (tmp.exists()) {
                    Files.move(FileSystems.getDefault().getPath(Conf.getImportDir() + "/" + f), FileSystems.getDefault().getPath(Conf.getNonProcessedDir() + "/" + f), REPLACE_EXISTING);
                    Conf.getLogger().info("File " + f + " moved to non processed folder");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}