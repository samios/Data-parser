import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by sami on 22/04/16.
 */

public class Scraper {


    static final int BUFFER = 2048;
    public static int counter = 0;
    public static String jsonFolder=Conf.getJsonFolder();

    /**
     *
     */
    public static String statusDb(String status, String[] statusList) {
        String tmp = "";
        for (String s : statusList) {
            if (s.split("=")[0].equals(status)) {
                tmp = s.split("=")[1];
                break;
            }
        }
        if (tmp.equals("Passed"))
            return "P";
        if (tmp.equals("Failed"))
            return "F";
        if (tmp.equals("Error"))
            return "E";
        else
            return "S";
    }


    /**
     * Returns the list of all files in a given directory
     *
     * @param path absolute directory path where the files are located
     * @return the list of files as a string array with each filename as a table value
     */

    //TODO: take only the regex into account
    public static ArrayList<String> getFileList(String path) {
        ArrayList<String> tmp = new ArrayList<String>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null)
            for (File listOfFile : listOfFiles)
                if (listOfFile.isFile())
                    tmp.add(listOfFile.getName());
        return tmp;
    }

    /**
     * Returns the header line
     *
     * @param filepath File path
     * @return a String representing the header
     */
    public static String scrapHeader(String filepath) throws IOException {
        int l = 0;
        String line = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), Parser.encoding(filepath)))) {
            while (l != Integer.parseInt(Conf.getHeaderLine())) {
                line = br.readLine();
                l++;
            }
        }
        return line;
    }

    /**
     * Returns the header line
     *
     * @param filepath File path
     * @return the creation date of a file
     */

    public static String getCreationTime(String filepath) throws IOException {
        Path file = Paths.get(filepath);
        BasicFileAttributes attr =
                Files.readAttributes(file, BasicFileAttributes.class);
        String us = attr.creationTime().toString().replace("T", " ").replace("Z", "");
        String year = attr.creationTime().toString().replace("T", " ").replace("Z", "").substring(0, 4);
        String month = attr.creationTime().toString().replace("T", " ").replace("Z", "").substring(5, 7);
        String day = attr.creationTime().toString().replace("T", " ").replace("Z", "").substring(8, 10);
        return us.replace(year + "-" + month + "-" + day, day + "-" + month + "-" + year);
    }

    /**
     * Returns the header line
     *
     * @param filepath File path
     * @return a String representing the header
     */
    public static void zip(String filepath, String destZip) throws IOException {
        byte[] buffer = new byte[1024];

        try{
            FileOutputStream fos = new FileOutputStream(destZip);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze= new ZipEntry(filepath.split("/")[filepath.split("/").length-1]);
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(filepath);

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
            zos.closeEntry();

            //remember to close it
            zos.close();

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }


    /**
     * Returns the content of all files in parameters
     *
     * @param filepath File path
     * @return an array of String arrays representing the content of the files with each String array representing a line
     */

    public static ArrayList<ArrayList<String>> getAllContents(String filepath) {
        ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), Parser.encoding(filepath)))) {
            String line;
            int i = 1;

            while ((line = br.readLine()) != null) {
                if (i >= Integer.parseInt(Conf.getLineStart()))
                    content.add(new ArrayList<String>(Arrays.asList(line.split(Conf.getSeparator()))));
                else
                    i++;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossible de lire ou d'ouvrir le fichier " + filepath);
        }

        return content;
    }


    /**
     * Returns the content of all files in parameters
     *
     * @param filepath File path
     * @return voidFailed
     */

    static void toJsonTest(String filepath) throws IOException {


        File fj = new File(jsonFolder+"/"+filepath.split("/")[filepath.split("/").length-1].split("\\.")[0] + ".json");
        if(fj.exists()) {
            Conf.getLogger().info(filepath + " already exists in Json folder ");

        }
        else {
            // Our JSON structures

            ArrayList<ArrayList<String>> content = getAllContents(filepath);
            JsonArrayBuilder stepListBuilder = Json.createArrayBuilder();
            JsonArrayBuilder statusListBuilder = Json.createArrayBuilder();
            JsonArrayBuilder listBuilder = Json.createArrayBuilder();
            JsonArrayBuilder paramListBuilder = Json.createArrayBuilder();
            JsonObjectBuilder testBuilder = Json.createObjectBuilder();
            JsonObjectBuilder globalBuilder = Json.createObjectBuilder();
            JsonObjectBuilder statusPassedBuilder = Json.createObjectBuilder();
            JsonObjectBuilder statusFailedBuilder = Json.createObjectBuilder();
            JsonObjectBuilder statusErrorBuilder = Json.createObjectBuilder();

            //If there is a header just extract the header line for further use later
            String header = "";
            if (Conf.getHeader().equals("Yes"))
                header = scrapHeader(filepath);

            //  Il n'y uniquement que trois états possibles !!!!!!!!

            String passed = "";
            String failed = "";
            String error = "";
            String skipped = "";
            Product p = Conf.getProductData();
            for (String s : Conf.getStatusList()) {
                if (s.contains("Passed")) {
                    statusPassedBuilder.add("Passed", s.split("=")[0]);
                    passed = s.split("=")[0];
                }
            }
            for (String s : Conf.getStatusList()) {

                if (s.contains("Failed")) {
                    statusFailedBuilder.add("Failed", s.split("=")[0]);
                    failed = s.split("=")[0];
                }
            }
            for (String s : Conf.getStatusList()) {

                if (s.contains("Error")) {
                    statusErrorBuilder.add("Error", s.split("=")[0]);
                    error = s.split("=")[0];
                }
            }
            for (String s : Conf.getStatusList()) {

                if (s.contains("Skipped")) {
                    statusErrorBuilder.add("Skipped", s.split("=")[0]);
                }
            }

            statusListBuilder.add(statusPassedBuilder);
            statusListBuilder.add(statusFailedBuilder);
            statusListBuilder.add(statusErrorBuilder);
            globalBuilder.add("StatusList", statusListBuilder);
            globalBuilder.add("File", filepath.split("")[filepath.split("/").length-1]);
            //Extract info from each line
            int j=0;
            File f=null;
            BufferedWriter writer = null;


            for (ArrayList<String> line : content) {
                boolean b=false;
                if(!Conf.getConsideredStatus().equals("null"))
                {
                    if(!Conf.getConsideredStatus().contains(line.get(Integer.parseInt(p.getBenchTest().getStatus()))))
                    {
                            b = true;

                            if(f==null) {

                                //TODO:see again for the fail files
                                f = new File(Conf.getImportDir()+ "/Passed/" + filepath.split("/")[filepath.split("/").length - 1] );

                                writer = new BufferedWriter(new FileWriter(f));
                                if (Conf.getHeader().equals("Yes")) {
                                    writer.write(header);
                                    writer.newLine();
                                }
                            }


                            for(String c:line) {
                                if(f.toString().equals("/home/data/BT010-01/MI2/Passed/MI2-Report_MI2_20151109.csv"))
                                writer.write(c);
                                writer.write(";");
                            }
                            writer.newLine();
                    }

                }
                if(b)
                    continue;
                String failstep = "";
                try {
                    //Checks if a line which has a passed status is larger than the normal column number
                    //If it is the file is corrupted or invalid
                    j++;
                     if (line.get(Integer.parseInt(p.getBenchTest().getStatus())).equals(passed) && !(line.size() == Integer.parseInt(Conf.getColNumber()))) {// Json creation/ data extraction failed
                        Conf.getLogger().info(filepath + " failed : Nombre de colonnes pour un test passed different de celui indiqué dans le fichier de conf");
                        zip(filepath,Conf.getFailArchiveDir()+"/"+filepath.split("/")[filepath.split("/").length-1].split("\\.")[0]+".zip");
                        return;
                    } else {
                        testBuilder.add("SN", line.get(Integer.parseInt(p.getSerial())));
                        //adds the file path to the json for further informations

                        // Içi on effectue l'ajout des steps d'une ligne

                        JsonObjectBuilder stepBuilder = Json.createObjectBuilder();

                        for (Step step : p.getBenchTest().getSteps()) {
                            try {
                                if(line.size()-1>=Integer.parseInt(step.getName().split(":")[0].replace("h", ""))) {
                                    {
                                        if (step.getName().split(":")[0].contains("h")) {
                                            if (line.size() - 1 >= Integer.parseInt(step.getName().split(":")[0].replace("h", ""))) {
                                                stepBuilder.add("Name", header.split(Conf.getSeparator())[Integer.parseInt(step.getName().split(":")[0].replace("h", ""))]);
                                            } else
                                                break;
                                        } else if (!line.get(Integer.parseInt(step.getName().split(":")[0])).equals(step.getName().split(":")[1])) {
                                            Conf.getLogger().info(filepath + " failed : Une des colonnes renseignée pour le stepname ne correspond pas");
                                            zip(filepath, Conf.getFailArchiveDir() + "/" + filepath.split("/")[filepath.split("/").length - 1].split("\\.")[0] + ".zip");
                                            return;
                                        } else
                                            stepBuilder.add("Name", line.get(Integer.parseInt(step.getName().split(":")[0])));
                                        if (!step.getDate().equals("null")) {
                                            if (line.size() - 1 >= Integer.parseInt(step.getDate()))
                                                stepBuilder.add("Date", line.get(Integer.parseInt(step.getDate())));
                                        } else
                                            stepBuilder.add("Date", "");
                                        //Min
                                        if (!step.getMin().equals("null"))
                                            if (line.size() - 1 >= Integer.parseInt(step.getMin()))
                                                stepBuilder.add("Min", line.get(Integer.parseInt(step.getMin())));
                                            else
                                                stepBuilder.add("Min", "");
                                        else
                                            stepBuilder.add("Min", "");
                                        //Maxl
                                        if (!step.getMax().equals("null"))
                                            if (line.size() - 1 >= Integer.parseInt(step.getMax()))
                                                stepBuilder.add("Max", line.get(Integer.parseInt(step.getMax())));
                                            else
                                                stepBuilder.add("Max", "");
                                        else
                                            stepBuilder.add("Max", "");
                                        //Unit
                                        if (!step.getUnit().equals("null"))
                                            if (line.size() - 1 >= Integer.parseInt(step.getUnit()))
                                                stepBuilder.add("Unit", line.get(Integer.parseInt(step.getUnit())));
                                            else
                                                stepBuilder.add("Unit", "");
                                        else
                                            stepBuilder.add("Unit", "");
                                        //Value can't be null
                                        if (!step.getValue().equals("null")) {
                                            if (line.size() - 1 >= Integer.parseInt(step.getValue()))
                                                stepBuilder.add("Value", line.get(Integer.parseInt(step.getValue())));
                                            else
                                                stepBuilder.add("Value", "");
                                        }//Duration
                                        if (!step.getStepDuration().equals("null"))
                                            if (line.size() - 1 >= Integer.parseInt(step.getStepDuration()))
                                                stepBuilder.add("StepDuration", line.get(Integer.parseInt(step.getStepDuration())));
                                            else
                                                stepBuilder.add("StepDuration", "");
                                        else
                                            stepBuilder.add("StepDuration", "");

                                        //Test Status
                                        if (!step.getStatus().equals("null")) {
                                            if (line.size() - 1 >= Integer.parseInt(step.getStatus())) {
                                                stepBuilder.add("Status", statusDb(line.get(Integer.parseInt(step.getStatus())), Conf.getStatusList()));
                                            } else
                                                break;
                                        }
                                        //Si le statut du test n'est pas donné directement
                                        else {
                                            ArrayList tmp = p.getBenchTest().getSteps();
                                            String tmpn=((Step)(tmp.get(tmp.indexOf(step)+1))).getName();
                                            if(statusDb(line.get(Integer.parseInt(p.getBenchTest().getStatus())), Conf.getStatusList()).equals("P"))
                                                stepBuilder.add("Status", "P");
                                            else {
                                                try {
                                                    if (line.get(Integer.parseInt(tmpn.split(":")[0].replace("h", ""))) == null)
                                                        stepBuilder.add("Status", "F");

                                                    else
                                                        stepBuilder.add("Status", "P");
                                                }
                                                catch(Exception e1)
                                                {
                                                    stepBuilder.add("Status", "F");
                                                }

                                            }
                                        }
                                    }
                                }
                                else
                                    break;
                                } catch (Exception e) {
                                e.printStackTrace();
                                Conf.getLogger().info(filepath + " erreur sur la ligne "+j);
                                break;
                            }
                            stepListBuilder.add(stepBuilder);
                        }

                        //recupere le statut d'un test depuis la ligne

                        if (!p.getBenchTest().getStatus().equals("null"))
                            testBuilder.add("Status", statusDb(line.get(Integer.parseInt(p.getBenchTest().getStatus())), Conf.getStatusList()));


                        //Si le statut final du test n'est pas renseigné on le déduit des statuts de tous les tests

                        else {
                            Boolean status = true;
                            String stepName = "";
                            for (Step step : p.getBenchTest().getSteps()) {
                                if (statusDb(line.get(Integer.parseInt(step.getStatus())), Conf.getStatusList()).equals("F") || statusDb(line.get(Integer.parseInt(step.getStatus())), Conf.getStatusList()).equals("E")) {
                                    status = false;
                                    stepName = line.get(Integer.parseInt(step.getName().split(":")[1]));
                                    break;
                                }
                            }
                            if (status)
                                testBuilder.add("Status", "P");
                            else {
                                testBuilder.add("Status", "F");
                                if (!p.getBenchTest().getFailedMessage().equals("null"))
                                    failstep= stepName;
                            }
                        }

                        //Test duration
                        if (!p.getBenchTest().getDuration().equals("null"))
                            if(line.size()-1>=Integer.parseInt(p.getBenchTest().getDuration()))
                                testBuilder.add("TestDuration", line.get(Integer.parseInt(p.getBenchTest().getDuration())));
                            else
                                testBuilder.add("TestDuration", "");
                        else
                            testBuilder.add("TestDuration", "");

                        // Si le fail step est renseigné ou pas
                        if (!p.getBenchTest().getFailedMessage().equals("null"))
                            if(line.size()-1>=Integer.parseInt(p.getBenchTest().getFailedMessage()))
                                testBuilder.add("FailStep", line.get(Integer.parseInt(p.getBenchTest().getFailedMessage())));
                            else
                                testBuilder.add("FailStep", "");
                        else
                            testBuilder.add("FailStep", failstep);

                        String date = "";

                        if (Conf.getProductData().getBenchTest().getDate().contains("+")) {
                            for (int i = 0; i < p.getBenchTest().getDate().split("\\+").length; i++)
                                if (line.size() - 1 >= Integer.parseInt(p.getBenchTest().getDate().split("\\+")[i]))
                                    date = date + " " + line.get(Integer.parseInt(p.getBenchTest().getDate().split("\\+")[i]));
                            testBuilder.add("Date", date);
                        } else {
                            if (!p.getBenchTest().getDate().equals("null"))
                                testBuilder.add("Date", line.get(Integer.parseInt(p.getBenchTest().getDate())));
                        }
                        //Product family

                        if (!Conf.getProductFamily().equals("null"))
                                testBuilder.add("ProductFamily",Conf.getProductFamily());
                        else
                            testBuilder.add("ProductFamily", "");

                        //Pc can't be null or there is a big problem in the Config file or program or even parser

                        testBuilder.add("Pc", line.get(Integer.parseInt(p.getBenchTest().getPc())));

                        //Well
                        if (!Conf.getWell().equals("null"))
                            if(line.size()-1>=Integer.parseInt(p.getWell()))
                                testBuilder.add("Well", p.getWell());
                            else
                                testBuilder.add("Well", "");
                        else {
                            if(!Conf.getInFileWell().equals("null"))
                                testBuilder.add("Well", line.get(Integer.parseInt(Conf.getInFileWell())));
                            else
                                testBuilder.add("Well", "");

                        }

                        // Adding the parameters to the builder

                        JsonObjectBuilder paramBuilder = Json.createObjectBuilder();

                        if(p.getParameters().size()>0)
                        for (Parameter param : p.getParameters()) {
                            if (param.getName().contains("h"))
                            {

                                if(line.size()-1>=Integer.parseInt(param.getName().split(":")[0].replace("h", "")))
                                {
                                    paramBuilder.add("Name", header.split(Conf.getSeparator())[Integer.parseInt(param.getName().replace("h", ""))]);
                                }
                                else
                                    break;
                            }
                            else if (!param.getName().equals("null"))
                                paramBuilder.add("Name", line.get(Integer.parseInt(param.getName())));
                            if (!param.getDate().equals("null"))
                                paramBuilder.add("Date", line.get(Integer.parseInt(param.getDate())));
                            if (!param.getValue().equals("null"))
                                paramBuilder.add("Value", line.get(Integer.parseInt(param.getValue())));

                            paramListBuilder.add(paramBuilder);
                        }
                        if (paramBuilder != null) {

                            testBuilder.add("Param", paramListBuilder);
                        } else
                            testBuilder.add("Param", "");




                        // Adding the steps to the builder

                        if (stepBuilder != null) {
                            testBuilder.add("Step", stepListBuilder);
                        } else
                            testBuilder.add("Step", "");


                    }

                }

                // Json creation/ data extraction failed
                catch (Exception e) {
                    e.printStackTrace();
                    Conf.getLogger().info(filepath + " Erreur sur la ligne "+j+". Probleme de configuration d'une des lignes.");
                    Conf.getLogger().info(filepath + " failed : ");
                    zip(filepath,Conf.getFailArchiveDir()+"/"+filepath.split("/")[filepath.split("/").length-1].split("\\.")[0]+".zip");
                    return;
                }
                //On ajoute le builder du test au builder principal
                globalBuilder.add("Tests",testBuilder);
                listBuilder.add(globalBuilder);
            }
            if(writer!=null)
                writer.close();

        //write to file

            OutputStream os = new FileOutputStream(jsonFolder + "/" + filepath.split("/")[filepath.split("/").length - 1].split("\\.")[0] + ".json"); //TODO : Check if file exists
            JsonArray listObject = listBuilder.build();

        /*
        * !!!!
        * !!!! TODO : Specify whether the json file should be pretty printed or not
        * !!!!
        * */

            Map<String, Boolean> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory jwf = Json.createWriterFactory(config);
            JsonWriter jsonWriter = jwf.createWriter(os);
            jsonWriter.writeArray(listObject);
            jsonWriter.close();
            counter++;

            // Json creation/ data extraction succeeded

            zip(filepath, Conf.getArchiveDir() + "/" + filepath.split("/")[filepath.split("/").length - 1].split("\\.")[0] + ".zip");
        }
    }

    static public String[] getDirList(Conf c) {
        File file = new File(c.getImportDir());
        String regex = c.getRegex();
        String[] tmp = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        ArrayList<String> buff = new ArrayList<>();

        if (!regex.equals("null")) {

            for (String s : tmp) {
                if (Pattern.matches(regex, s)) {
                    buff.add(s);
                }
            }
            String[] tab = new String[buff.size()];
            for (int i = 0; i < buff.size(); i++) {
                tab[i] = buff.get(i);
            }
            return tab;
        } else
            return tmp;
    }


}
