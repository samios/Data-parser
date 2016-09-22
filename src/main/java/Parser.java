import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by sami on 20/07/16.
 */
public class Parser {
    public static Conf parse(String conf) throws IOException {
        Product p = new Product();
        Conf c= new Conf();
        String line = "";
        int l = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(conf),encoding(conf)));
        while ((line = reader.readLine())!=null)
        {

            if(l==0)
                c.setArchiveDir(line.split("\t")[1]);
            if(l==1)
                c.setFailArchiveDir(line.split("\t")[1]);
            if(l==2)
                c.setDirectory(line.split("\t")[1]);
            if(l==3)
                c.setPreviewFile(line.split("\t")[1]);
            if(l==4)
                c.setColNumber(line.split("\t")[1]);
            if(l==5)
                c.setSeparator(line.split("\t")[1]);
            if(l==6)
                c.setLineFormat(line.split("\t")[1]);
            if(l==7)
                c.setStatusCol(line.split("\t")[1]);
            if(l==8)
                c.setLineStart(line.split("\t")[1]);
            if(l==9)
                c.setHeader(line.split("\t")[1]);
            if(l==10)
                c.setHeaderLine(line.split("\t")[1]);
            if(l==11)
                c.setStatusList(line.replace("StatusList\t","").split("\t"));
            //TODO : Consider the pass array for l =8
            if(l==12)
                p.setClient(line.split("\t")[1]);
            if(l==13)
                c.setWell(line.split("\t")[1]);

            if(l==14)
                p.setSerial(line.split("\t")[1]);
            if(l==15)
                c.setProductFamily(line.split("\t")[1]);
            if(l==16)
                p.getBenchTest().setDate(line.split("\t")[1]);
            if(l==17)
                p.getBenchTest().setDuration(line.split("\t")[1]);
            if(l==18)
                p.getBenchTest().setStatus(line.split("\t")[1]);
            if(l==19)
                p.getBenchTest().setPc(line.split("\t")[1]);
            if(l==20)
                p.getBenchTest().setFailedMessage(line.split("\t")[1]);
            if(l==21) {
                String s = "";
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().addStep(tab[i]);
                }

            }

            if(l==22) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++)
                    p.getBenchTest().getSteps().get(i-1).setDate(tab[i]);


            }
            if(l==23) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setStatus(tab[i]);
                }
            }
            if(l==24) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setMax(tab[i]);

                }

            }
            if(l==25) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setMin(tab[i]);

                }

            }
            if(l==26) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setUnit(tab[i]);

                }

            }
            if(l==27) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setValue(tab[i]);

                }
            }
            if(l==28) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getBenchTest().getSteps().get(i-1).setStepDuration(tab[i]);
                }
            }
            if(l==29) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.addParameter(tab[i]);
                }
            }
            if(l==30) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getParameters().get(i-1).setDate(tab[i]);
                }
            }
            if(l==31) {
                String[] tab=line.split("\t");
                for (int i = 1; i <tab.length ; i++) {
                    p.getParameters().get(i-1).setValue(tab[i]);
                }
            }
            if(l==32)
                c.setSubSeparator(line.split("\t")[1]);
            if(l==33)
                c.setRegex(line.split("\t")[1]);
            if(l==34)
                c.setStartKey(line.split("\t")[1]);
            if(l==35)
                c.setEndKey(line.split("\t")[1]);
            if(l==36)
                c.setStepLineTestStatus(line.split("\t")[1]);
            if(l==37)
                c.setInFileWell(line.split("\t")[1]);
            if(l==38)
                c.setConsideredStatus(line.split("\t")[1]);
            l++;
        }
        c.setProductData(p);
        return c;
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


