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

    @Function(trust = Function.Trust.UNSANDBOXED)
    public static String add() throws SQLException {
        ArrayList<String> tmp = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs;
        // TODO : repertoire a modifier mail
        File folder = new File("/home/sami/IdeaProjects/Integration/json");
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
                        .getConnection("jdbc:postgresql://43.198.130.47:5432/cordon", "vnc", "reploidx"); // TODO: change the connection parameters
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                ResultSet seqKey;
                stmt.execute(" set search_path to 'bubendorff'"); // to do for each schema of the database

                for (String json : tmp) {
                    JSONParser parser = new JSONParser();
                    try {
                        JSONArray jsonArray;

                        try {
                            jsonArray = (JSONArray) parser.parse(new FileReader("/home/sami/IdeaProjects/Integration/json/" + json));
                        } catch (IOException e) {
                            e.printStackTrace();
                            return e.getMessage();
                        }

                        for (Object obj : jsonArray) {

                            JSONObject line = (JSONObject) obj;

                            try {


                                //stmt.executeUpdate("insert into  product values(default,'MCI3',1, true, true  )");


                                int stepId, productId, testNb, parameterId, failMessageId, benchId;

                                // Check if the product is already in the Db if yes we have to update if not then we have to insert
                                rs = stmt.executeQuery("select * from product where serialnumber='" + line.get("SN") + "'");

                                if (rs.next()) //if the productid already exists
                                {

                                    productId = rs.getInt("productId");
                                    rs = stmt.executeQuery("select * from test where fk_productid=" + productId + "and date_part('epoch', timestamp '" + line.get("Date") + "' - date)=0");

                                    if (!rs.next()) //if the test doesn't exist
                                    {

                                        /*****************product*********************/
                                        rs = stmt.executeQuery("select count(*) as testnb from test where fk_productid='" + productId + "'");
                                        rs.next();

                                        stmt.executeUpdate("update product set lastStatus='" + line.get("Status") + "', totaltest=" + rs.getInt("testnb") + "+1 where productId=" + productId);

                                        /*****************failmessage*********************/

                                        // Check if the fail message is already in the Db if yes we have to update if not then we have to insert
                                        failMessageId = -1;
                                        if (!line.get("FailStep").equals("")) {
                                            rs = stmt.executeQuery("select * from failmessage where failmessage='" + ((String) (line.get("FailMsg"))).replace("'", "") + "'");
                                            if (!rs.next()) //if the message doesn't exist
                                            {

                                                stmt.executeUpdate("insert into failmessage values(Default,'" + ((String) (line.get("FailMsg"))).replace("'", "") + "')", Statement.RETURN_GENERATED_KEYS);
                                                seqKey = stmt.getGeneratedKeys();
                                                seqKey.next();
                                                failMessageId = seqKey.getInt(1);
                                            } else {
                                                failMessageId = rs.getInt("failmessageid");

                                                //do nothing
                                            }
                                        }
                                        for (Object param : (JSONArray) ((JSONObject) obj).get("Param")) {

                                            JSONObject field = (JSONObject) param;

                                            /*****************parametername*********************/
                                            // Check if the parameter name is already in the Db if yes we have to update if not then we have to insert
                                            rs = stmt.executeQuery("select * from parametername where name='" + field.get("Name") + "'");

                                            if (!rs.next()) //if the parameter name doesn't exist
                                            {

                                                stmt.executeUpdate("insert into parametername values(Default,'" + field.get("Name") + "')", Statement.RETURN_GENERATED_KEYS);
                                                seqKey = stmt.getGeneratedKeys();
                                                parameterId = seqKey.getInt(1);
                                                if (field.get("Date") != null)
                                                    stmt.executeUpdate("insert into parameter values(" + productId + "," + parameterId + ",'" + field.get("Date") + "','" + field.get("Value") + "')");
                                                else
                                                    stmt.executeUpdate("insert into parameter values(" + productId + "," + parameterId + ",'1000-01-01 00:00:00','" + field.get("Value") + "')");

                                            }
                                        }
                                        /*****************testbench*********************/
                                        // Check if the benchid is already in the Db if yes we have to update if not then we have to insert
                                        rs = stmt.executeQuery("select * from testbench where pc='" + line.get("Pc") + "'");

                                        if (!rs.next()) //if the parameter name doesn't exist
                                        {
                                            stmt.executeUpdate("insert into testbench values(Default,'" + line.get("Pc") + "',)", Statement.RETURN_GENERATED_KEYS);
                                            seqKey = stmt.getGeneratedKeys();
                                            seqKey.next();
                                            benchId = seqKey.getInt(1);


                                        } else {
                                            benchId = rs.getInt("benchid");
                                        }

                                        /*****************test*********************/
                                        // Check if the test is already in the Db if yes we have to update if not then we have to insert
                                        /*****************test*********************/
                                        // Check if the test contains a fail message
                                        if (failMessageId == -1) {

                                            stmt.executeUpdate("insert into test values(" + productId + ",default, '" + line.get("Date") + "','" + line.get("Status") + "',null," + benchId + ")", Statement.RETURN_GENERATED_KEYS);
                                            seqKey = stmt.getGeneratedKeys();
                                            seqKey.next();
                                            testNb = seqKey.getInt(2);

                                        } else {
                                            stmt.executeUpdate("insert into test values(" + productId + ",default, '" + line.get("Date") + "','" + line.get("Status") + "'," + failMessageId + "," + benchId + ")", Statement.RETURN_GENERATED_KEYS);
                                            seqKey = stmt.getGeneratedKeys();
                                            seqKey.next();
                                            testNb = seqKey.getInt(2);

                                        }


                                        // Check if the stepname is already in the Db if yes we have to update if not then we have to insert
                                        if (!line.get("Step").toString().equals("[{}]"))
                                            for (Object step : (JSONArray) ((JSONObject) obj).get("Step")) {

                                                JSONObject field = (JSONObject) step;

                                                rs = stmt.executeQuery("select * from stepname where name='" + ((String) (field.get("Name"))).replace("'", "") + "'");

                                                if (rs.next()) //if the stepname already exists
                                                {


                                                    /*****************stepname*********************/
                                                    // No need to add anything since the database already contains the corresponding stepname
                                                    stepId = rs.getInt("stepId");
                                                    /*****************step*********************/
                                                    rs = stmt.executeQuery("select * from step where fk_stepid=" + stepId);
                                                    if (!line.get("Step").equals("[{}]"))
                                                        if (!rs.next()) // if the step doesn't exist
                                                        {
                                                            if (!field.get("Date").equals("")) {
                                                                stmt.executeUpdate("insert into step values(" + stepId + ",'" + field.get("Date") + "','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )", Statement.RETURN_GENERATED_KEYS);
                                                            } else {
                                                                stmt.executeUpdate("insert into step values(" + stepId + ",'1000-01-01 00:00:00','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )", Statement.RETURN_GENERATED_KEYS);

                                                            }
                                                        }  // if the step exists


                                                } else // if the stepname doesn't exist
                                                {

                                                    /*****************stepname*********************/
                                                    stmt.executeUpdate("Insert into stepname values(Default,'" + field.get("Name") + "')", Statement.RETURN_GENERATED_KEYS);
                                                    seqKey.next();
                                                    seqKey = stmt.getGeneratedKeys();
                                                    stepId = seqKey.getInt(1);
                                                    /*****************Step*********************/
                                                    // Check if the test is already in the Db if yes we have to update if not then we have to insert
                                                    if (field.get("Date") != null) {
                                                        stmt.executeUpdate("insert into step values(" + stepId + ",'" + field.get("Date") + "','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )");

                                                    } else {
                                                        stmt.executeUpdate("insert into step values(" + stepId + ",'1000-01-01 00:00:00','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )");
                                                    }
                                                }


                                            }
                                        if (!line.get("Step").toString().equals("[{}]"))

                                            for (Object step : (JSONArray) ((JSONObject) obj).get("Step")) {

                                            JSONObject field = (JSONObject) step;
                                            rs = stmt.executeQuery("select * from stepname where name='" + ((String) (field.get("Name"))).replace("'", "") + "'");
                                            rs.next();
                                            stepId = rs.getInt("stepid");
                                            rs = stmt.executeQuery("select MAX(measurenb) as measurenb from measure where fk_productid=" + productId + " and fk_stepid=" + stepId + " and fk_testid=" + testNb + "");
                                            String stepDate;
                                            /*****************Measure*********************/


                                                stepDate = (String) line.get("Date");

// TODO : Check the delay
                                            if (!rs.next())
                                                stmt.executeUpdate("insert into measure values(" + productId + "," + stepId + "," + testNb + ", TIMESTAMP '" + stepDate + "' ," + (rs.getInt("measurenb") + 1) + ",'" + field.get("Status") + "','" + field.get("Value") + "',null)");

                                            else {
                                                stmt.executeUpdate("insert into measure values(" + productId + "," + stepId + "," + testNb + ", TIMESTAMP '" + line.get("Date") + "' ," + (rs.getInt("measurenb") + 1) + ",'" + field.get("Status") + "','" + field.get("Value") + "',null)");
                                            }
                                        }

                                    }


                                } else // if the product isn't in the database
                                {

                                    /*****************product*********************/
                                    stmt.executeUpdate("insert into product values(default,'" + line.get("SN") + "',1,'" + line.get("Status") + "','" + line.get("Status") + "')", Statement.RETURN_GENERATED_KEYS);


                                    seqKey = stmt.getGeneratedKeys();
                                    seqKey.next();
                                    productId = seqKey.getInt(1);
                                    /*****************productfamily*********************/
                                    stmt.executeUpdate("insert into productfamily values(" + productId + ",'" + family + "')");

                                    /*****************failmessage*********************/
                                    // Check if the fail message is already in the Db if yes we have to update if not then we have to insert
                                    failMessageId = -1;
                                    if (!line.get("FailStep").equals("")) {
                                        rs = stmt.executeQuery("select * from failmessage where failmessage='" + ((String) (line.get("FailMsg"))).replace("'", "") + "'");
                                        if (!rs.next()) //if the message doesn't exist
                                        {

                                            stmt.executeUpdate("insert into failmessage values(Default,'" + ((String) (line.get("FailMsg"))).replace("'", "") + "')", Statement.RETURN_GENERATED_KEYS);
                                            seqKey = stmt.getGeneratedKeys();
                                            seqKey.next();
                                            failMessageId = seqKey.getInt(1);
                                        } else {
                                            failMessageId = rs.getInt("failmessageid");

                                            //do nothing
                                        }
                                    }
                                    for (Object param : (JSONArray) ((JSONObject) obj).get("Param")) {

                                        JSONObject field = (JSONObject) param;

                                        /*****************parametername*********************/
                                        // Check if the parameter name is already in the Db if yes we have to update if not then we have to insert
                                        rs = stmt.executeQuery("select * from parametername where name='" + field.get("Name") + "'");

                                        if (!rs.next()) //if stepIthe parameter name doesn't exist
                                        {
                                            stmt.executeUpdate("insert into parametername values(default,'" + field.get("Name") + "')", Statement.RETURN_GENERATED_KEYS);

                                            seqKey = stmt.getGeneratedKeys();
                                            seqKey.next();
                                            parameterId = seqKey.getInt(1);
                                            stmt.executeUpdate("insert into parameter values(" + productId + "," + parameterId + ",'2008-11-11 13:23:44','" + field.get("Value") + "')");
                                        }
                                    }
                                    /*****************testbench*********************/
                                    // Check if the benchid is already in the Db if yes we have to update if not then we have to insert
                                    rs = stmt.executeQuery("select * from testbench where pc='" + line.get("Pc") + "'");
                                    if (!rs.next()) //if the parameter name doesn't exist
                                    {
                                        if (!line.get("Well").equals(""))
                                        stmt.executeUpdate("insert into testbench values(Default,'" + line.get("Pc") + "'," + line.get("Well") + ")", Statement.RETURN_GENERATED_KEYS);
                                        else
                                            stmt.executeUpdate("insert into testbench values(Default,'" + line.get("Pc") + "'," + "NULL" + ")", Statement.RETURN_GENERATED_KEYS);

                                        seqKey = stmt.getGeneratedKeys();
                                        seqKey.next();
                                        benchId = seqKey.getInt(1);
                                    } else {
                                        benchId = rs.getInt("benchid");
                                    }

                                    if (!line.get("Step").toString().equals("[{}]"))
                                        for (Object step : (JSONArray) ((JSONObject) obj).get("Step")) {

                                            JSONObject field = (JSONObject) step;
                                            /*****************stepname*********************/

                                            rs = stmt.executeQuery("select * from stepname where name='" + ((String) (field.get("Name"))).replace("'", "") + "'");

                                            if (!rs.next()) //if the stepname doesn't exist
                                            {
                                                stmt.executeUpdate("Insert into stepname values(default,'" + ((String) (field.get("Name"))).replace("'", "") + "')", Statement.RETURN_GENERATED_KEYS);

                                                seqKey = stmt.getGeneratedKeys();
                                                seqKey.next();

                                            } else // if the stepname exists
                                            {
                                                stepId = rs.getInt("stepid");
                                                //TODO : check if the step already exists
                                                /*****************step*********************/
                                                if (!field.get("Date").equals(""))
                                                    rs = stmt.executeQuery("select * from step where fk_stepid='" + stepId + "' and date=" + field.get("Date") + "");
                                                else
                                                    rs = stmt.executeQuery("select * from step where fk_stepid='" + stepId + "' and date=" + "TIMESTAMP '" + line.get("Date") + "'");

                                                if (rs.next()) //if the step already exists
                                                {

                                                    if (field.get("Date") != null)
                                                        stmt.executeUpdate("insert into step values(" + stepId + ",'" + field.get("Date") + "','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )", Statement.RETURN_GENERATED_KEYS);
                                                    else
                                                        stmt.executeUpdate("insert into step values(" + stepId + ",'1000-01-01 00:00:00','" + field.get("Min") + "','" + field.get("Max") + "','" + field.get("Unit") + "'  )", Statement.RETURN_GENERATED_KEYS);
                                                }
                                            }
                                        }
                                    /*****************test*********************/
                                    // Check if the test contains a fail message
                                    if (failMessageId == -1) {

                                        stmt.executeUpdate("insert into test values(" + productId + ",default, '" + line.get("Date") + "','" + line.get("Status") + "',null," + benchId + ")", Statement.RETURN_GENERATED_KEYS);
                                        seqKey = stmt.getGeneratedKeys();
                                        seqKey.next();
                                        testNb = seqKey.getInt(2);
                                    } else {
                                        stmt.executeUpdate("insert into test values(" + productId + ",default, '" + line.get("Date") + "','" + line.get("Status") + "'," + failMessageId + "," + benchId + ")", Statement.RETURN_GENERATED_KEYS);
                                        seqKey = stmt.getGeneratedKeys();
                                        seqKey.next();
                                        testNb = seqKey.getInt(2);


                                    }
                                    if (!line.get("Step").toString().equals("[{}]"))
                                        for (Object step : (JSONArray) ((JSONObject) obj).get("Step")) {

                                            JSONObject field = (JSONObject) step;
                                            rs = stmt.executeQuery("select * from stepname where name='" + ((String) (field.get("Name"))).replace("'", "") + "'");
                                            rs.next();
                                            stepId = rs.getInt("stepid");
                                            rs = stmt.executeQuery("select MAX(measurenb) as measurenb from measure where fk_productid=" + productId + " and fk_stepid=" + stepId + " and fk_testid=" + testNb + "");
                                            String stepDate;
                                            /*****************Measure*********************/


                                            if (field.get("Date") != null) stepDate = (String) field.get("Date");
                                            else stepDate = (String) line.get("Date");

                                            if (!rs.next())
                                                stmt.executeUpdate("insert into measure values(" + productId + "," + stepId + "," + testNb + ", TIMESTAMP '" + stepDate + ",' 1,'" + field.get("Status") + "','" + field.get("Value") + "',null)");
                                            else {
                                                stmt.executeUpdate("insert into measure values(" + productId + "," + stepId + "," + testNb + ", TIMESTAMP '" + line.get("Date") + "' ," + (rs.getInt("measurenb") + 1) + ",'" + field.get("Status") + "','" + field.get("Value") + "',null)");
                                            }
                                        }

                                }
                            } catch (SQLException e) {
                                //Handle errors for JDBC
                                System.out.println("Connection failed! Check output console");
                                System.out.println("Rolling back data here....");
                                try {
                                    // If there is an error then rollback the changes.
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
                            conn.commit();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return e.getMessage();
                    }

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException ignored) {
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

    public static void main(String[] args) {
        try {
            Server.add();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

