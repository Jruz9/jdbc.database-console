package com.github.perschola;

import com.mysql.cj.protocol.Resultset;
import com.github.perschola.utils.IOConsole;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.StringJoiner;


public class databaseOptions implements Runnable {
    IOConsole ioConsole =new IOConsole();
        public void run() {

        registerJDBCDriver();//class
            Connection mySqlConnection =getConnection("mysql");

            executeStatement(mySqlConnection,"DROP DATABASE IF EXISTS databaseName;");
            executeStatement(mySqlConnection, "CREATE DATABASE IF NOT EXIST databaseName;");
            executeStatement(mySqlConnection,"USE databaseName;");
            executeStatement(mySqlConnection,new StringBuilder()
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
            printResults((ResultSet) resultset);
            String userChoice="";
            do{
                userChoice = ioConsole.getStringInput("Select from the following options:"+
                        "\n\t add entity\n\t  remove entity\n\t  get entity\n\t  update entity\n\t  quit");
                if ((userChoice.equalsIgnoreCase("add-entity")))
                {
                    addEntity(mySqlConnection);
                }
                else if (userChoice.equalsIgnoreCase("remove-entity")){
                    removeEntity(mySqlConnection);
                }
                else if(userChoice.equalsIgnoreCase("update entity")) {
                    updateEntity(mySqlConnection);
                }


            }while(!userChoice.equalsIgnoreCase("quit"));


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

 public void printResults(ResultSet resultset)
 {
     try
     {
         ResultSetMetaData metaData= resultset.getMetaData();
         while (resultset.next())
         {
             for (int i =1; i<= metaData.getColumnCount();i++)
             {
                 ioConsole.print(metaData.getColumnClassName(i)+":"+resultset.getString(i));
             }
             ioConsole.println("");
         }
     }
     catch (SQLException e)
     {
         throw new Error(e);
     }
 }

void addEntity(Connection mysqlDbConnection)
{
    String table= ioConsole.getStringInput("Enter input");
    Integer id;
    String name;
    Integer primary_type;
    Integer secondary_type;
    id=ioConsole.getIntegerInput("Enter Id");
    name=ioConsole.getStringInput("Enter pokemon name");
    primary_type = ioConsole.getIntegerInput("Enter primary type");
    secondary_type=ioConsole.getIntegerInput("Enter Secondary Type");

    executeStatement(mysqlDbConnection,"INSERT INTO userdatabase."+table+" "+"(id, name, primary_type, secondary_type)"+
            " VALUES (" + id + ", '" + name + "', " + primary_type + ", " + secondary_type + ")");

}

void removeEntity(Connection mySqlConnection){
            String table =ioConsole.getStringInput("Enter your table");
            Integer id=ioConsole.getIntegerInput("Enter the ID you want to remove from your table ");

            executeStatement(mySqlConnection,"DELETE FROM "+table+" WHERE id ="+id+";");
    }
    void updateEntity(Connection mySqlConnection)
    {
        String table =ioConsole.getStringInput("Enter input");
        Integer id =ioConsole.getIntegerInput("Enter the id location of the parameter");
        String property =ioConsole.getStringInput("Enter the property to update");
        Integer newItem=ioConsole.getIntegerInput("Enter new item");

        executeStatement(mySqlConnection,"UPDATE "+table+" SET "+property+"="+newItem+" WHERE id="+id+";");
    }




}
