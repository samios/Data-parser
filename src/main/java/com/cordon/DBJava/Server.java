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
        ArrayList<String> tmp=new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        File folder = new File("/home/sami/IdeaProjects/DBJava/json"); // TODO : répertoire à modifiermail
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles!=null) { // Do nothing if the directory is empty


            try
            {
                Class.forName("org.postgresql.Driver");
                System.out.println("Connecting to database...");

                conn = DriverManager
                        .getConnection("jdbc:postgresql://localhost:5433/bubendorff", "sami", "reploidx"); // TODO: change the connection parameters
                conn.setAutoCommit(false);
                stmt = conn.createStatement();

                //stmt.executeUpdate("insert into  product values(default,'MCI3',1, true, true  )");

                /*****************product*********************/
                // Check if the product is already in the Db if yes we have to update if not then we have to insert
                // TODO :Change the values to the corresponding variables
                int stepId,productId,testNb,parameterId,failMessageId;
                rs =stmt.executeQuery("select * from sn where \"SerialNumber\"='number'");
                if(rs.next()) //if the productid already exists
                {

                    productId=rs.getInt("ProductId");
                    // TODO :Change the values to the corresponding variables and increment the total number of tests
                    stmt.executeUpdate("update product set \"LastStatus\"='true' where \"ProductId\"="+productId);

                    /*****************failmessage*********************/
                    // Check if the fail message is already in the Db if yes we have to update if not then we have to insert
                    // TODO :Change the values to the corresponding variables
                    failMessageId=-1;
                    rs = stmt.executeQuery("select * from failmessage where \"FailMessage\"='message'"); // Upper cases must be escaped with a backslash

                    if (!rs.next()) //ifja the message doesn't exist
                    {
                        failMessageId=stmt.executeUpdate("insert into failmessage values(Default,'Message')",Statement.RETURN_GENERATED_KEYS);
                    }
                    else
                    {
                        //do nothing
                        }

                    /*****************ParameterName*********************/
                    // Check if the parameter name is already in the Db if yes we have to update if not then we have to insert
                    // TODO :Change the values to the corresponding variables
                    rs = stmt.executeQuery("select * from parametername where \"ParameterName\"='param'"); // Upper cases must be escaped with a backslash

                    if (!rs.next()) //if the parameter name doesn't exist
                    {
                        parameterId=stmt.executeUpdate("insert into parametername values(Default,'param')",Statement.RETURN_GENERATED_KEYS);
                        stmt.executeUpdate("insert into parameter values("+productId+","+parameterId+",'2008-11-11 13:23:44','null')");
                    }
                    else
                    {
                        //do nothing
                    }

                    /*****************test*********************/
                    // Check if the test is already in the Db if yes we have to update if not then we have to insert
                    // TODO :Change the values to the corresponding variables
                    rs =stmt.executeQuery("select * from test where \"ProductId\"='step'"); // Upper cases must be escaped with a backslash


                    if(!rs.next()) //if the test doesn't exist
                    {
                        if(failMessageId==-1)
                        testNb=stmt.executeUpdate("insert into test values("+productId+", '2008-11-11 13:23:44','1',default,'passed',,)",Statement.RETURN_GENERATED_KEYS);
                        else
                        {
                            rs =stmt.executeQuery("select * from failmessage where \"FailedMessage\"='message'");
                            testNb=stmt.executeUpdate("insert into test values("+productId+", '2008-11-11 13:23:44','1',default,'passed',"+rs.getInt("FailMessageId")+","+failMessageId+")",Statement.RETURN_GENERATED_KEYS);
                        }

                        /*****************stepname*********************/
                        // Check if the stepname is already in the Db if yes we have to update if not then we have to insert
                        // TODO :Change the values to the corresponding variables

                        rs = stmt.executeQuery("select * from stepname where \"StepName\"='step'"); // Upper cases must be escaped with a backslash

                        if (rs.next()) //if the stepname already exists
                        {
                            stepId=rs.getInt("StepId");
                            /*****************step*********************/
                            // TODO :Change the values to the corresponding variables and increment the total number of tests
                            stmt.executeQuery("select * from step where \"StepId\"=" + stepId + " and \"Date\"='2008-11-11 13:23:44'"); // Upper cases must be escaped with a backslash
                            if (!rs.next()) // if the step doesn't exist
                            {
                                stepId=stmt.executeUpdate("insert into step values(Default,'2008-11-11 13:23:44','OK','OK','OK'  )",Statement.RETURN_GENERATED_KEYS);
                            }
                            else // if the step exists
                            {
                                //do nothing
                            }


                        }
                        else // if the stepname doesn't exist
                        {
                            // TODO :Change the values to the corresponding variables
                            stepId=stmt.executeUpdate("insert into stepname values(default,'step'  )",Statement.RETURN_GENERATED_KEYS);
                            stmt.executeUpdate("insert into step values("+stepId+",'2008-11-11 13:23:44','OK','OK','OK'  )");
                        }
                        /*****************Measures*********************/
                        stmt.executeUpdate("insert into measures values("+productId+","+stepId+","+testNb+",'2008-11-11 13:23:44','OK','OK',)");

                    }
                    else//if the test already exists
                    {}
                }

                else
                {
                    // TODO :Change the values to the corresponding variables
                    productId=stmt.executeUpdate("insert into sn values(1,'sn')",Statement.RETURN_GENERATED_KEYS);
                    // TODO :Change the values to the corresponding variables
                    stmt.executeUpdate("insert into product values("+productId+"'pref',1,'true','true')");
                    slec
                }




                conn.commit();
            }
            catch (SQLException e)
            {
                //Handle errors for JDBC
                System.out.println("Connection Failed! Check output console");
                System.out.println("Rolling back data here....");
                try
                {
                    // If there is an error then rollback the changes.
                    if(conn!=null)
                        conn.rollback();
                }
                catch(SQLException se2)
                {
                    se2.printStackTrace();
                }
                e.printStackTrace();
                return e.getMessage();
            }

            catch (Exception e)
            { // sometimes the driver may not be found so we need to throw an exception and catch it
                e.printStackTrace();
                System.exit(99);
            }
            finally
            {
                //finally block used to close resources
                try
                {
                    if(stmt!=null)
                        stmt.close();
                }
                catch(SQLException se2)
                {}
                try
                {
                    if(conn!=null)
                        conn.close();
                }
                catch(SQLException se)
                {
                    se.printStackTrace();
                }
            }

            for (File listOfFile : listOfFiles)
                if (listOfFile.isFile())
                    tmp.add(listOfFile.getName());

            for (String json : tmp) {
                JSONParser parser = new JSONParser();
                try
                {
                    Object obj = null;
                    try {
                        obj = parser.parse(new FileReader("/home/sami/IdeaProjects/DBJava/json/"+json));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return e.getMessage();
                    }
                    JSONArray tests=(JSONArray)obj;
                    for (Object test : tests) {

                        for (Object field : (JSONArray) ((JSONObject) test).get("Step"))
                        {

                        }
                    }
                }

                catch (ParseException e)
                {
                    e.printStackTrace();
                    return e.getMessage();
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

