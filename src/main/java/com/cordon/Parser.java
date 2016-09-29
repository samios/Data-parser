package com.cordon;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by sami on 20/07/16.
 */
public class Parser {
        private static Conf c= new Conf();


    /**
     * @param conf the conf file to be parsed
     * @throws IOException
     */
    public static void parse(String conf) throws IOException {
        Product p = new Product();
        String line;
        int l = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(conf),encoding(conf)));
        while ((line = reader.readLine())!=null)
        {

            if(l==0)
                Conf.setArchiveDir(line.split("\t")[1]);
            if(l==1)
                Conf.setFailArchiveDir(line.split("\t")[1]);
            if(l==2)
                Conf.setPreviewFile(line.split("\t")[1]);
            if(l==3)
                Conf.setColNumber(line.split("\t")[1]);
            if(l==4)
                Conf.setSeparator(line.split("\t")[1]);
            if(l==5)
                Conf.setLineFormat(line.split("\t")[1]);
            if(l==6)
                Conf.setStatusCol(line.split("\t")[1]);
            if(l==7)
                Conf.setLineStart(line.split("\t")[1]);
            if(l==8)
                Conf.setHeader(line.split("\t")[1]);
            if(l==9)
                Conf.setHeaderLine(line.split("\t")[1]);
            if(l==10)
                Conf.setStatusList(line.replace("StatusList\t","").split("\t"));
            if(l==11)
                p.setClient(line.split("\t")[1]);
            if(l==12)
                Conf.setWell(line.split("\t")[1]);

            if(l==13)
                p.setSerial(line.split("\t")[1]);
            if(l==14)
                Conf.setProductFamily(line.split("\t")[1]);
            if(l==15)
                p.getBenchTest().setDate(line.split("\t")[1]);
            if(l==16)
                p.getBenchTest().setDuration(line.split("\t")[1]);
            if(l==17)
                p.getBenchTest().setStatus(line.split("\t")[1]);
            if(l==18)
                p.getBenchTest().setPc(line.split("\t")[1]);
            if(l==19)
                p.getBenchTest().setFailedMessage(line.split("\t")[1]);
            if(l==20) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().addStep(tab[i]);
                }

            }

            if(l==21) {

                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++)
                    p.getBenchTest().getSteps().get(i-1).setDate(tab[i]);


            }
            if(l==22) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setStatus(tab[i]);
                }
            }
            if(l==23) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setMax(tab[i]);

                }

            }
            if(l==24) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setMin(tab[i]);

                }

            }
            if(l==25) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setUnit(tab[i]);

                }

            }
            if(l==26) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setValue(tab[i]);

                }
            }
            if(l==27) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setStepDuration(tab[i]);
                }
            }
            if(l==28) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.addParameter(tab[i]);
                }
            }
            if(l==29) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getParameters().get(i-1).setDate(tab[i]);
                }
            }
            if(l==30) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getParameters().get(i-1).setValue(tab[i]);
                }
            }
            if(l==31)
                Conf.setSubSeparator(line.split("\t")[1]);
            if(l==32)
                Conf.setRegex(line.split("\t")[1]);
            if(l==33)
                Conf.setStartKey(line.split("\t")[1]);
            if(l==34)
                Conf.setEndKey(line.split("\t")[1]);
            if(l==35)
                Conf.setStepLineTestStatus(line.split("\t")[1]);
            if(l==36)
                Conf.setInFileWell(line.split("\t")[1]);
            if(l==37)
                Conf.setConsideredStatus(line.split("\t")[1]);
            l++;
        }
        Conf.setProductData(p);
    }
    /**
     * @param path the file path
     * Get file encoding thanks to the juniversalchardet
     * @return the encoding of the file
     */


    public static String encoding(String path) throws IOException {
        String DEFAULT_ENCODING = "UTF-8";
        byte[] buf = new byte[4096];
        java.io.FileInputStream fis = new java.io.FileInputStream(path);
        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }

}


