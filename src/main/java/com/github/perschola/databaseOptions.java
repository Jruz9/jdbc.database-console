package com.github.perschola;

import com.mysql.cj.protocol.Resultset;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.StringJoiner;


public class databaseOptions implements Runnable {

        public void run() {
        registerJDBCDriver();//class
            Connection mySqlConnection =getConnection("mysql");

            executeStatement(mySqlConnection,"DROP DATABASE IF EXISTS databaseName;");
            executeStatement((mySqlConnection, "CREATE DATABASE IF NOT EXIST databaseName;");
            executeStatement((mySqlConnection,"USE databaseName;");
            executeStatement((mySqlConnection,new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS databaseName.pokemonTable(")
            .append("id int auto_increment primary key,")
                    .append("name text not null,")
            .append("primary_type int not null,")
            .append("secondary_type int null);").toString());

            executeStatement(mySqlConnection,new StringBuilder()
                    .append("INSERT INTO databaseName.pokemonTable ")
                    .append("(id, name, primary_type, secondary_type)")
                    .append(" VALUES (12, 'Ivysaur', 3, 7);").toString());

            String query ="SELECT * FROM databaseName.pokemonTable;";
            Resultset resultset =executeQuery(mySqlConnection,query);
            printResults(resultset);


        }



 void registerJDBCDriver()
 {
     //attempts to  JDBC Driver
     try{
         DriverManager.registerDriver((Driver.class.newInstance()));
     }
     catch (IllegalAccessError | SQLException | InstantiationException | IllegalAccessException e1){
         throw new  Error(e1);
     }
 }
 public Connection getConnection(String dbVendor)
 {
     String username="root";
     String password ="";
     String url="jdbc"+dbVendor+"://127.0.0.1/";
     try{
         return DriverManager.getConnection(url,username,password);
     }
     catch (SQLException e){
         throw  new Error(e);
     }
 }
 public Statement getScrollableStatement(Connection connection)
 {
     int resultSetType= ResultSet.TYPE_FORWARD_ONLY;        //tells the result type to go forward in the results ??
     int resultSetConcurrency =ResultSet.CONCUR_READ_ONLY;  //activates concurrency
     try{
         return  connection.createStatement(resultSetType,resultSetConcurrency);
     }
     catch (SQLException e){
         throw new Error(e);
     }
 }

     void executeStatement (Connection connection,String sqlStatement){
         try{
             Statement statement=getScrollableStatement(connection);
             statement.execute(sqlStatement);
         }
         catch (SQLException e)
         {
             throw new Error(e);
         }
 }
     Resultset executeQuery(Connection connection,String sqlQuery){
            try{
                Statement statement = getScrollableStatement(connection);
                return (Resultset) statement.executeQuery(sqlQuery);
            }
            catch (SQLException e)
            {
                throw new Error(e);
            }
 }

 public void printResults(Resultset resultset)
 {
     try
     {
    for (Integer rowNumber =0;resultset.next();rowNumber++)
    {
        String firstColumnData=resultset.getString(1);
        String secondColumnData=resultset.getString(2);
        String thirdColumnData=resultset.getString(3);

        System.out.println(new StringJoiner("\n")
                .add("Row number = " + rowNumber.toString())
                .add("First Column = " + firstColumnData)
                .add("Second Column = " + secondColumnData)
                .add("Third column = " + thirdColumnData)
                .toString());
    }
     }
     catch (SQLException e)
     {
         throw new Error(e);
     }
 }






}
