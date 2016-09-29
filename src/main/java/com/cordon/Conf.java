package com.cordon;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by sami on 23/06/16.
 *
 * @author sami
 */
public class Conf {


    /**
     * Folder where the conf files are loaded
     */

    private static String parserConf;

    /**
     * Log name
     */

    private static String logName;

    /**
     * Import dir for the files
     */

    private static String importDir;

    /**
     * Folder for the non processed files
     */

    private static String nonProcessedDir;


    /**
     * Number of threads to process the files
     */

    private static String nThreads;


    /**
     * logger for the programm log file
     */

    private static Logger logger ;


    /**
     * Folder of the jsons
     */

    private static String jsonFolder ;


    /**
     * New conf or modify mode
     */

    private static String Mode;

    /**
     * Config file path
     */

    private static String configFile;
    /**
     * Client Name
     */

    private static String Client;
    /**
     * ProductFamily
     */

    private static String ProductFamily;

    /**
     * Directory where the integrated files will be archived
     */

    private static String ArchiveDir ;
    /**
     * Directory where the failed integrated files will be archived
     */

    private static String FailArchiveDir;
    /**
     * Config file name
     */

    private static String ConfigName;

    /**
     * How each line will be parsed
     */

    private static String LineFormat;

    /**
     * Regex to be used
     */

    private static String Regex;
    /**
     * Start the the data extraction starting from the n-th line
     */

    private static String LineStart = "1";

    /**
     * Indicates whether there is a header or not
     */

    private static String Header;


    /**
     * Header Line
     */

    private static String HeaderLine;


    /**
     * Status equivalence array
     */

    private static String[] StatusList;

    /**
     * Column number of the whole test
     */

    private static String StatusCol = "1";


    /**
     * Data about the product
     */

    private static Product ProductData;

    /**
     * Number of columns in each file
     */

    private static String ColNumber;

    /**
     * File to be used as preview in the wizard
     */

    private static String PreviewFile;

    /**
     * Line separator
     */

    private static String Separator;

    /**
     * Line sub separator
     */
    private static String SubSeparator;

    /**
     * If test fxml was visited
     */

    private static Boolean VisitedMode;


    /**
     * If test fxml bis was visited
     */

    private static Boolean VisitedTestBis;


    /**
     * If step fxml bis was visited
     */

    private static Boolean VisitedStepBis;


    /**
     * Start keyword for Step line
     */

    private static String StartKey;

    /**
     * Ending keyword for Step line
     */

    private static String EndKey;

    /**
     * Starting keyword line
     */

    private static String StartKeyLine;

    /**
     * Ending keyword line
     */

    private static String EndKeyLine;


    /**
     * Well number
     */

    private static String Well;

    /**
     * Passed test line
     */

    private static String PassedLine;

    /**
     * Where to find the test status when the lines are steps, either on start or end
     */

    private static String StepLineTestStatus;

    /**
     * Where to find the test status when the lines are steps, either on start or end
     */

    private static String InFileWell;

    /**
     * Whether the conf is valid only for certain status
     */

    private static String ConsideredStatus;


    /**
     *  Loads the program configuration file
     */

    public static void Init() throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("data-parser.conf"),Parser.encoding("data-parser.conf")));
        line = reader.readLine();
        setParserConf(line.split("\t")[1]);
        line = reader.readLine();
        setJsonFolder(line.split("\t")[1]);
        line = reader.readLine();
        setImportDir(line.split("\t")[1]);
        line = reader.readLine();
        setNonProcessedDir(line.split("\t")[1]);
        line = reader.readLine();
        setLogName(line.split("\t")[1]);
        setLogger(Logger.getLogger(getLogName()));
        line = reader.readLine();
        setnThreads(line.split("\t")[1]);
    }

    /**
     * Constructor
     */

    public Conf() {
        ProductData = new Product();
        VisitedMode=false;
        VisitedStepBis=false;
        VisitedTestBis=false;
    }

    /**
     * Constructor
     */

    public Conf(String mode, String client, String archiveDir, String failArchiveDir, String configDir, String lineParse, String lineStart, String[] status) {

        Mode = mode;
        Client = client;
        ArchiveDir = archiveDir;
        FailArchiveDir = failArchiveDir;
        ConfigName = configDir;
        LineFormat = lineParse;
        LineStart = lineStart;
        StatusList = status;
        VisitedMode=false;
        VisitedTestBis=false;
    }

    /**
     * Getter
     */

    public static String getParserConf() {
        return parserConf;
    }

    /**
     * Setter
     */

    private static void setParserConf(String parserConf) {
        Conf.parserConf = parserConf;
    }

    /**
     * Getter
     */

    public static String getLogName() {
        return logName;
    }

    /**
     * Setter
     */

    private static void setLogName(String logName) {
        Conf.logName = logName;
    }

    /**
     * Getter
     */

    public static String getImportDir() {
        return importDir;
    }

    /**
     * Setter
     */

    private static void setImportDir(String importDir) {
        Conf.importDir = importDir;
    }

    /**
     * Getter
     */

    public static String getJsonFolder() {
        return jsonFolder;
    }

    /**
     * Setter
     */

    private static void setJsonFolder(String jsonFolder) {
        Conf.jsonFolder = jsonFolder;
    }

    /**
     * Getter
     */

    public static String getNonProcessedDir() {
        return nonProcessedDir;
    }

    /**
     * Setter
     */

    private static void setNonProcessedDir(String nonProcessedDir) {
        Conf.nonProcessedDir = nonProcessedDir;
    }

    /**
     * Getter
     */

    public static String getnThreads() {
        return nThreads;
    }

    /**
     * Setter
     */

    private static void setnThreads(String nThreads) {
        Conf.nThreads = nThreads;
    }

    /**
     * Getter
     */

    public static String getMode() {
        return Mode;
    }

    /**
     * Setter
     */

    public static void setMode(String mode) {
        Mode = mode;
    }

    /**
     * Getter
     */

    public static String getConfigFile() {
        return configFile;
    }

    /**
     * Setter
     */

    public static void setConfigFile(String configfile) {
        configFile = configfile;
    }

    /**
     * Getter
     */


    public static String getClient() {
        return Client;
    }

    /**
     * Setter
     */

    private static void setClient(String client) {
        Client = client;
    }

    /**
     * Getter
     */


    public static String getProductFamily() {
        return ProductFamily;
    }

    /**
     * Setter
     */


    public static void setProductFamily(String productFamily)
    {

            ProductFamily = productFamily;
    }

    /**
     * Getter
     */

    public static String getArchiveDir() {
        return ArchiveDir;
    }

    /**
     * Setter
     */

    public static void setArchiveDir(String archiveDir) {
        ArchiveDir = archiveDir;
    }

    /**
     * Getter
     */

    public static String getFailArchiveDir() {
        return FailArchiveDir;
    }

    /**
     * Setter
     */

    public static void setFailArchiveDir(String failArchiveDir) {
        FailArchiveDir = failArchiveDir;
    }

    /**
     * Getter
     */

    public static String getConfigDir() {
        return ConfigName;
    }

    /**
     * Setter
     */

    public static void setConfigDir(String configDir) {
        ConfigName = configDir;
    }

    /**
     * Getter
     */

    public static String getLineFormat() {
        return LineFormat;
    }

    /**
     * Setter
     */

    public static void setLineFormat(String lineFormat) {
        LineFormat = lineFormat;
    }

    /**
     * Getter
     */

    public static String getLineStart() {
        return LineStart;
    }

    /**
     * Setter
     */

    public static void setLineStart(String lineStart) {
        LineStart = lineStart;
    }

    /**
     * Getter
     */

    public static String[] getStatusList() {
        return StatusList;
    }

    /**
     * Setter
     */

    public static void setStatusList(String[] status) {
        StatusList = status;
    }

    /**
     * Getter
     */

    public static String getStatusCol() {
        return StatusCol;
    }

    /**
     * Setter
     */

    public static void setStatusCol(String statusCol) {
        StatusCol = statusCol;
    }

    /**
     * Getter
     */

    public static String getRegex() {       return Regex;    }

    /**
     * Setter
     */


    public static void setRegex(String regex) {

            Regex = regex;
    }

    /**
     * Getter
     */

    public static Product getProductData() {
        return ProductData;
    }

    /**
     * Setter
     */

    public static void setProductData(Product productData) {
        ProductData = productData;
    }

    /**
     * Getter
     */

    public static String getColNumber() {
        return ColNumber;
    }

    /**
     * Setter
     */

    public static void setColNumber(String colNumber) {
        ColNumber = colNumber;
    }

    /**
     * Getter
     */

    public static String getSeparator() {
        return Separator;
    }


    /**
     * Setter
     */

    public static void setSeparator(String separator) {
        Separator = separator;
    }

    /**
     * Getter
     */

    public static String getPreviewFile() {
        return PreviewFile;
    }

    /**
     * Setter
     */

    public static void setPreviewFile(String previewFile) {
        PreviewFile = previewFile;
    }

    /**
     * Getter
     */

    public static String getConfigName() {
        return ConfigName;
    }

    /**
     * Setter
     */

    public static void setConfigName(String configName) {
        ConfigName = configName;
    }

    /**
     * Getter
     */

    public static String getWell() {
        return Well;
    }

    /**
     * Setter
     */
    public static void setWell(String well) {

            Well=well;
    }

    /**
     * Getter
     */

    public static String getHeader() {
        return Header;
    }

    /**
     * Setter
     */

    public static void setHeader(String header) {            Header = header;}

    /**
     * Getter
     */

    public static String getHeaderLine() {
        return HeaderLine;
    }

    /**
     * Setter
     */

    public static void setHeaderLine(String headerLine) {


            HeaderLine = headerLine;
    }

    /**
     * Getter
     */

    public static Boolean getVisitedTestBis() {
        return VisitedTestBis;
    }

    /**
     * Setter
     */

    public static void setVisitedTestBis(Boolean visited3) {
        VisitedTestBis = visited3;
    }

    /**
     * Getter
     */

    public static String getSubSeparator() {
        return SubSeparator;
    }

    /**
     * Setter
     */

    public static void setSubSeparator(String subSeparator) {


            SubSeparator = subSeparator;
    }

    /**
     * Getter
     */

    public static Boolean getVisitedMode() {
        return VisitedMode;
    }

    /**
     * Setter
     */

    public static void setVisitedMode(Boolean visitedMode) {
        VisitedMode = visitedMode;
    }

    /**
     * Getter
     */

    public static Boolean getVisitedStepBis() {
        return VisitedStepBis;
    }

    /**
     * Setter
     */

    public static void setVisitedStepBis(Boolean visitedStepBis) {
        VisitedStepBis = visitedStepBis;
    }

    /**
     * Getter
     */

    public static String getStartKey() {
        return StartKey;
    }

    /**
     * Setter
     */

    public static void setStartKey(String startKey) {

            StartKey = startKey;
    }

    /**
     * Getter
     */

    public static String getEndKey() {
        return EndKey;
    }

    /**
     * Setter
     */

    public static void setEndKey(String endKey) {
            EndKey = endKey;
    }

    /**
     * Getter
     */

    public static String getPassedLine() {
        return PassedLine;
    }

    /**
     * Setter
     */

    public static void setPassedLine(String passedLine) {
        PassedLine = passedLine;
    }

    /**
     * Getter
     */

    public static String getStartKeyLine() {
        return StartKeyLine;
    }

    /**
     * Setter
     */

    public static void setStartKeyLine(String startKeyLine) {
        StartKeyLine = startKeyLine;
    }

    /**
     * Getter
     */

    public static String getEndKeyLine() {
        return EndKeyLine;
    }

    /**
     * Setter
     */

    public static void setEndKeyLine(String endKeyLine) {
        EndKeyLine = endKeyLine;
    }

    /**
     * Getter
     */

    public static String getStepLineTestStatus() {
        return StepLineTestStatus;
    }

    /**
     * Setter
     */

    public static void setStepLineTestStatus(String stepLineTestStatus) {
        StepLineTestStatus = stepLineTestStatus;
    }

    /**
     * Getter
     */

    public static String getInFileWell() {
        return InFileWell;
    }

    /**
     * Setter
     */

    public static void setInFileWell(String inFileWell) {
        InFileWell = inFileWell;
    }

    /**
     * Getter
     */

    public static Logger getLogger() {
        return logger;
    }

    /**
     * Setter
     */

    private static void setLogger(Logger logger) {
        Conf.logger = logger;
    }

    /**
     * Getter
     */

    public static String getConsideredStatus() {
        return ConsideredStatus;
    }

    /**
     * Setter
     */

    public static void setConsideredStatus(String consideredStatus) {
        ConsideredStatus = consideredStatus;
    }


    /**
     * Checks if the file is a valid configuration file
     * @param file the conf file to check
     * @return boolean true if success false if failed to create
     */

    public boolean checkConf(String file) throws IOException {
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/Test.conf"),Parser.encoding("src/main/resources/Test.conf")));
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),Parser.encoding(file)))) {
            String line;
            String line2;
            while ((line2 = br.readLine()) != null && (line = br2.readLine()) != null) {

                if (!line.split("\t")[0].equals(line2.split("\t")[0])) {
                    throw new Exception("Mauvais fichier");
                }
            }
            br.close();
            br2.close();
        } catch (IOException e1) {
            e1.printStackTrace();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Avertissement");
            alert.setHeaderText("Mauvais fichier de config");
            alert.setContentText("Veuillez choisir un fichier de config valide");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Loads the conf data
     * @param file the conf file name to load
     */

    public void loadConf(String file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),Parser.encoding(file)));
        String line;
        ArrayList<String> tmp = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            tmp.add(line.substring(line.indexOf("\t") + 1, line.length()));
        }
        setArchiveDir("");
        setFailArchiveDir(tmp.get(1));
        setColNumber(tmp.get(3));
        setSeparator(tmp.get(4));
        setLineFormat(tmp.get(5));
        setLineStart(tmp.get(6));
        setHeader(tmp.get(8));
        setHeaderLine(tmp.get(9));

        int l = tmp.get(10).split("\t").length;


        String status[] = new String[l];
        for (int i = 0; i < l; i++) {
            status[i] = tmp.get(10).split("\t")[i];
        }

        setStatusList(status);
        setClient(tmp.get(11));
        setWell(tmp.get(12));
        setProductFamily(tmp.get(14));

        ArrayList<Step> steps = new ArrayList<Step>();

        for (String s : tmp.get(20).split("\t")) {

            steps.add(new Step(tmp.get(20), tmp.get(21), tmp.get(22), tmp.get(23), tmp.get(24), tmp.get(25), tmp.get(26),tmp.get(27)));
        }
        Test test = new Test(steps, tmp.get(15), tmp.get(16), tmp.get(17), tmp.get(18),tmp.get(19));
        ArrayList<Parameter> params = new ArrayList<Parameter>();
        for (String s : tmp.get(28).split("\t")) {
            params.add(new Parameter(tmp.get(28), tmp.get(29), tmp.get(30)));
        }

        setSubSeparator(tmp.get(31));
        setRegex(tmp.get(32));
        setStartKey(tmp.get(33));
        setEndKey(tmp.get(34));
        setStepLineTestStatus(tmp.get(35));
        setInFileWell(tmp.get(36));
        setConsideredStatus(tmp.get(37));
        setProductData(new Product(tmp.get(13), test, params));
        br.close();

    }
    /**
     * resets the conf structure
     */
    public static void reset()
    {
        setArchiveDir("");
        setFailArchiveDir("");
        setColNumber("");
        setSeparator("");
        setLineFormat("");
        setLineStart("");
        setHeader("");
        setHeaderLine("");
        setStatusList(null);
        setClient("");
        setWell("");
        setProductFamily("");
        setSubSeparator("");
        setRegex("");
        setStartKey("");
        setEndKey("");
        setStepLineTestStatus("");
        setInFileWell("");
        setConsideredStatus("");
        Product tmp=new Product();
        setProductData(tmp);

    }
}


