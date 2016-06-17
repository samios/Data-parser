package com.cordon.DBJava;

import org.postgresql.pljava.annotation.Function;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * Created by sami on 07/06/16.
 */
public class Server {

    @Function(trust=Function.Trust.UNSANDBOXED)
    public static String add() throws SQLException {
        ArrayList<String> tmp = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        File folder = new File("/home/sami/IdeaProjects/DBJava/json"); // TODO : répertoire à modifiermail
        File[] listOfFiles = folder.listFiles();
        String family = "family";

        if (listOfFiles != null) { // Do nothing if the directory is empty

            for (File listOfFile : listOfFiles)
                if (listOfFile.isFile())
                    tmp.add(listOfFile.getName());
            try {
                Class.forName("org.postgresql.Driver");

                System.out.println("Connecting to database...");

                conn = DriverManager
                        .getConnection("jdbc:postgresql://localhost:5432/cordon", "sami", "reploidx"); // TODO: change the connection parameters
                conn.setAutoCommit(false);
                stmt = conn.createStatement();

                stmt.execute(" set search_path to 'bubendorff'"); // to do for each schema of the database

                for (String json : tmp) {
                    JSONParser parser = new JSONParser();
                    try {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = (JSONArray) parser.parse(new FileReader("/home/sami/IdeaProjects/DBJava/json/" + json));
                        } catch (IOException e) {
                            e.printStackTrace();
                            return e.getMessage();
                        }
                        for (Object obj : jsonArray) {

                            JSONObject line = (JSONObject) obj;

                            try {


                                //stmt.executeUpdate("insert into  product values(default,'MCI3',1, true, true  )");


                                int stepId, productId, testNb, parameterId, failMessageId, benchId, failId;

                                // Check if the product is already in the Db if yes we have to update if not then we have to insert
                                rs = stmt.executeQuery("select * from product where serialnumber='" + line.get("SN") + "'");

                                if (rs.next()) //if the productid already exists
                                {
                                    productId = rs.getInt("productId");
                                    /*****************product*********************/
                                    stmt.executeUpdate("update product set lastStatus='" + line.get("Status") + "' where productId=" + productId);

                                    /*****************failmessage*********************/
                                    // Check if the fail message is already in the Db if yes we have to update if not then we have to insert
                                    failMessageId = -1;
                                    rs = stmt.executeQuery("select * from failmessage where failMessage='" + line.get("FailMsg") + "'");

                                    if (!rs.next()) //if the message doesn't exist
                                    {
                                        failMessageId = stmt.executeUpdate("insert into failmessage values(Default,'" + line.get("FailMsg") + "')", Statement.RETURN_GENERATED_KEYS);
                                    } else {
                                        //do nothing
                                    }
                                    for (Object param : (JSONArray) ((JSONObject) obj).get("Param")) {

                                        JSONObject field = (JSONObject) param;

                                        /*****************parametername*********************/
                                        // Check if the parameter name is already in the Db if yes we have to update if not then we have to insert
                                        rs = stmt.executeQuery("select * from parametername where name='" + field.get("Name") + "'");

                                        if (!rs.next()) //if the parameter name doesn't exist
                                        {
                                            parameterId = stmt.executeUpdate("insert into parametername values(Default,'" + field.get("Name") + "')", Statement.RETURN_GENERATED_KEYS);

                                            if (field.get("Date") != null)
                                                stmt.executeUpdate("insert into parameter values(" + productId + "," + parameterId + ",'" + field.get("Date") + "','" + field.get("Value") + "')");
                                            else
                                                stmt.executeUpdate("insert into parameter values(" + productId + "," + parameterId + ",'1000-01-01 00:00:00','" + field.get("Value") + "')");

                                        } else {
                                            //do nothing
                                        }
                                    }
                                    /*****************testbench*********************/
                                    // Check if the benchid is already in the Db if yes we have to update if not then we have to insert
                                    rs = stmt.executeQuery("select * from testbench where pc='" + line.get("Pc") + "'");

                                    if (!rs.next()) //if the parameter name doesn't exist
                                    {
                                        benchId = stmt.executeUpdate("insert into testbench values(Default,'" + line.get("Pc") + "',)", Statement.RETURN_GENERATED_KEYS);
                                    } else {
                                        benchId = rs.getInt("benchid");
                                    }

                                    /*****************test*********************/
                                    // Check if the test is already in the Db if yes we have to update if not then we have to insert
                                    rs = stmt.executeQuery("select * from test where fk_productid=" + productId + " and testid=1");


                                    if (!rs.next()) //if the test doesn't exist
                                    {
                                        if (failMessageId == -1)
                                            testNb = stmt.executeUpdate("insert into test values(" + productId + ",default, '" + line.get("Date") + "','" + line.get("Status") + "'," + line.get("Date") + "," + benchId + ")", Statement.RETURN_GENERATED_KEYS);
                                        else {
                                            rs = stmt.executeQuery("select * from failmessage where failmessage='" + line.get("FailMsg") + "'");
                                            testNb = stmt.executeUpdate("insert into test values(" + productId + ",default, '" + line.get("Date") + "','" + line.get("Status") + "'," + failMessageId + "," + benchId + ")", Statement.RETURN_GENERATED_KEYS);
                                        }


                                        // Check if the stepname is already in the Db if yes we have to update if not then we have to insert

                                        stepId = -1;
                                        for (Object step : (JSONArray) ((JSONObject) obj).get("Step")) {

                                            JSONObject field = (JSONObject) step;

                                            rs = stmt.executeQuery("select * from stepname where name='" + field.get("Name") + "'");

                                            if (rs.next()) //if the stepname already exists
                                            {


                                                /*****************stepname*********************/
                                                // No need to add anything since the database already contains the corresponding stepname
                                                stepId = rs.getInt("stepId");
                                                /*****************step*********************/
                                                stmt.executeQuery("select * from step where stepid=" + stepId + " and date=''");
                                                if (!rs.next()) // if the step doesn't exist
                                                {
                                                    if (field.get("Date") != null)
                                                        stepId = stmt.executeUpdate("insert into step values(Default,'" + field.get("Date") + "','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )", Statement.RETURN_GENERATED_KEYS);
                                                    else
                                                        stepId = stmt.executeUpdate("insert into step values(Default,'1000-01-01 00:00:00','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )", Statement.RETURN_GENERATED_KEYS);

                                                } else // if the step exists
                                                {
                                                    //do nothing
                                                }


                                            } else // if the stepname doesn't exist
                                            {

                                                /*****************stepname*********************/
                                                stepId = stmt.executeUpdate("Insert into stepname values(Default,'" + field.get("Name") + "')", Statement.RETURN_GENERATED_KEYS);
                                                /*****************Step*********************/
                                                // Check if the test is already in the Db if yes we have to update if not then we have to insert
                                                if (field.get("Date") != null)
                                                    stmt.executeUpdate("insert into step values(" + stepId + ",'" + field.get("Date") + "','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )");
                                                else
                                                    stmt.executeUpdate("insert into step values(" + stepId + ",'1000-01-01 00:00:00','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )");

                                            }
                                            /*****************Measure*********************/
                                            if (field.get("Date") != null)
                                                stmt.executeUpdate("insert into measure values(" + productId + "," + stepId + "," + testNb + ",'" + field.get("Date") + "','" + field.get("Status") + "','OK',)");
                                            else
                                                stmt.executeUpdate("insert into measure values(" + productId + "," + stepId + "," + testNb + ",'" + line.get("Date") + "','','')");

                                        }

                                    } else//if the test already exists
                                    {
                                    }
                                } else // if the product isn't in the database
                                {

                                    /*****************product*********************/
                                    productId = stmt.executeUpdate("insert into product values(default,'" + line.get("SN") + "',1,'" + line.get("Status") + "','" + line.get("Status") + "')", Statement.RETURN_GENERATED_KEYS);
                                    System.out.println(productId);

                                    /*****************productfamily*********************/
                                    stmt.executeUpdate("insert into productfamily values(" + productId + ",'" + family + "')");

                                    /*****************failmessage*********************/
                                    // Check if the fail message is already in the Db if yes we have to update if not then we have to insert
                                    failMessageId = -1;
                                    rs = stmt.executeQuery("select * from failmessage where failmessage='" + line.get("FailMsg") + "'");

                                    if (!rs.next()) //if the message doesn't exist
                                    {
                                        failMessageId = stmt.executeUpdate("insert into failmessage values(Default,'Message')", Statement.RETURN_GENERATED_KEYS);
                                    } else {
                                        //do nothing
                                    }
                                    for (Object param : (JSONArray) ((JSONObject) obj).get("Param")) {

                                        JSONObject field = (JSONObject) param;

                                        /*****************parametername*********************/
                                        // Check if the parameter name is already in the Db if yes we have to update if not then we have to insert
                                        rs = stmt.executeQuery("select * from parametername where name='" + field.get("Name") + "'");

                                        if (!rs.next()) //if the parameter name doesn't exist
                                        {
                                            parameterId = stmt.executeUpdate("insert into parametername values(default,'" + field.get("Name") + "')", Statement.RETURN_GENERATED_KEYS);

                                            stmt.executeUpdate("insert into parameter values(" + productId + "," + parameterId + ",'2008-11-11 13:23:44','" + field.get("Value") + "')");
                                        } else {
                                            //do nothing
                                        }
                                    }
                                    /*****************testbench*********************/
                                    // Check if the benchid is already in the Db if yes we have to update if not then we have to insert
                                    rs = stmt.executeQuery("select * from testbench where pc='" + line.get("Pc") + "'");
                                    if (!rs.next()) //if the parameter name doesn't exist
                                    {
                                        benchId = stmt.executeUpdate("insert into testbench values(Default,'" + line.get("Pc") + "'," + line.get("Well") + ")", Statement.RETURN_GENERATED_KEYS);
                                    } else {
                                        benchId = rs.getInt("benchid");
                                    }

                                    for (Object step : (JSONArray) ((JSONObject) obj).get("Step")) {

                                        JSONObject field = (JSONObject) step;
                                        /*****************stepname*********************/
                                        rs = stmt.executeQuery("select * from stepname where name='step'");

                                        if (!rs.next()) //if the stepname doesn't exist
                                        {
                                            stepId = stmt.executeUpdate("Insert into stepname values(default,'" + ((String) (field.get("Name"))).replace("'", "") + "')", Statement.RETURN_GENERATED_KEYS);
                                        } else {
                                            stepId = rs.getInt("fk_stepid");

                                            /*****************step*********************/
                                            rs = stmt.executeQuery("select * from step where stepid='" + stepId + "'");

                                            if (rs.next()) //if the stepname already exists
                                            {

                                                if (field.get("Date") != null)
                                                    stmt.executeUpdate("insert into step values(" + stepId + ",'" + field.get("Date") + "','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )", Statement.RETURN_GENERATED_KEYS);
                                                else
                                                    stmt.executeUpdate("insert into step values(" + stepId + ",'1000-01-01 00:00:00','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )", Statement.RETURN_GENERATED_KEYS);
                                            }

                                            /*****************test*********************/
                                            // Check if the test is already in the Db if yes we have to update if not then we have to insert
                                            if (failMessageId == -1)
                                                testNb = stmt.executeUpdate("insert into test values(" + productId + ",default, '" + line.get("Date") + "','" + line.get("Status") + "'," + line.get("Date") + "," + benchId + ")", Statement.RETURN_GENERATED_KEYS);
                                            else {
                                                rs = stmt.executeQuery("select * from failmessage where failmessage='" + line.get("FailMsg") + "'");
                                                testNb = stmt.executeUpdate("insert into test values(" + productId + ",default, '" + line.get("Date") + "','" + line.get("Status") + "'," + failMessageId + "," + benchId + ")", Statement.RETURN_GENERATED_KEYS);


                                                /*****************Measure*********************/
                                                if (field.get("Date") != null)
                                                    stmt.executeUpdate("insert into measure values(" + productId + "," + stepId + "," + testNb + ",'" + field.get("Date") + "','" + field.get("Status") + "','OK',)");
                                                else
                                                    stmt.executeUpdate("insert into measure values(" + productId + "," + stepId + "," + testNb + ",'" + line.get("Date") + "','','')");

                                            }
                                        }

                                    }

                                }
                                conn.commit();
                            } catch (SQLException e) {
                                //Handle errors for JDBC
                                System.out.println("Connection failed! Check output console");
                                System.out.println("Rolling back data here....");
                                try {
                                    // If there is an error then rollback the changes.
                                    if (conn != null)
                                        conn.rollback();
                                } catch (SQLException se2) {
                                    se2.printStackTrace();
                                }
                                e.printStackTrace();
                                return e.getMessage();
                            } catch (Exception e) { // sometimes the driver may not be found so we need to throw an exception and catch it
                                e.printStackTrace();
                                System.exit(99);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return e.getMessage();
                    }

                }
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            finally
            {
                //finally block used to close resources
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException se2) {
                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return "JSON INSERT SUCCESS";
    }

    public static void main(String [] args)
    {
        try {
            Server.add();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

