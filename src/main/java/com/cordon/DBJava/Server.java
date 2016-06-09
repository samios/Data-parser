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

                // Check if the product is already in the Db if yes we have to update if not then we have to insert
                // TODO :Change the values to the corresponding variables
                rs =stmt.executeQuery("select * from sn where \"SerialNumber\"='number'");

                if(rs.next()) {
                    // TODO :Change the values to the corresponding variables and increment the total number of tests
                    stmt.executeUpdate("update product set \"LastStatus\"='true' where \"ProductId\"="+rs.getInt("ProductId"));
                    System.out.println("nop");

                }

                else
                {
                    // TODO :Change the values to the corresponding variables
                    stmt.executeUpdate("insert into sn values(1,'sn'  )");
                    // TODO :Change the values to the corresponding variables
                    stmt.executeUpdate("insert into product values(default,'pref',1,'true','true')");
                }

                // Check if the step is already in the Db if yes we have to update if not then we have to insert
                // TODO :Change the values to the corresponding variables

                rs =stmt.executeQuery("select * from stepname where \"StepName\"='step'"); // Upper cases must be escaped with a backslash

                if(rs.next())
                {
                    // TODO :Change the values to the corresponding variables and increment the total number of tests
                    stmt.executeQuery("select * from step where \"StepName\"="+rs.getInt("StepId")+" and \"Date\"='2008-11-11 13:23:44'"); // Upper cases must be escaped with a backslash
                    if(!rs.next())
                    {
                        stmt.executeUpdate("insert into step values(Default,'2008-11-11 13:23:44','OK','OK','OK'  )");

                    }

                }
                else
                {
                    // TODO :Change the values to the corresponding variables
                    stmt.executeUpdate("insert into stepname values(default,'step'  )");
                    stmt.executeUpdate("insert into step values(Default,'2008-11-11 13:23:44','OK','OK','OK'  )");

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

