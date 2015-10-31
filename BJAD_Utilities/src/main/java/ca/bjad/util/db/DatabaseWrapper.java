package ca.bjad.util.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Database wrapper used to perform common tasks against a database,
 * such as C/U/D operations and queries.
 * 
 * @author Ben Dougall
 */
public class DatabaseWrapper implements AutoCloseable
{
   /**
    * Used to set the connection string for the wrapper objects to use
    * globally, so users do not have to apply the connection string
    * over and over again.
    */
   protected static String globalConnectionString = null;

   /**
    * Sets the globally configured connection string, which will be
    * used for instances of the wrapper object that do not pass the
    * connection string to the constructor.
    * 
    * @param connectionString
    *           The connection string to apply.
    */
   public static void setGlobalConnectionString(String connectionString)
   {
      globalConnectionString = connectionString;
   }

   private String            connectionString = "";
   private String            sqlString        = "";

   private boolean           batchMode        = false;

   private Connection        dbConnection     = null;
   private PreparedStatement statement        = null;
   private ResultSet         resultSet        = null;

   /**
    * Constructor, accepting the sql statement that will be executed
    * by the wrapper.
    *
    * @param sqlString
    *    The SQL statement to execute.
    * @param arguments
    *    The parameters for the sql statement.
    * @throws java.sql.SQLException
    *    Any SQL Exceptions will be thrown back to 
    *    the calling function. 
    */
   public DatabaseWrapper(String sqlString, Object... arguments) throws SQLException
   {
      this(globalConnectionString, sqlString, arguments);
   }

   /**
    * Constructor taking in a custom connection string as will as the
    * SQL statement to execute against the database the connection
    * string points to.
    *
    * @param connectionString
    *     The connection string to use when connecting to a
    *     database.
    * @param sqlString
    *     The SQL statement to execute.
    * @param arguments
    *    The parameters for the sql statement.
    * @throws java.sql.SQLException
    *    Any SQL Exceptions will be thrown back to 
    *    the calling function. 
    */
   public DatabaseWrapper(String connectionString, String sqlString, Object... arguments) throws SQLException
   {
      this.connectionString = connectionString;
      this.sqlString = sqlString;
      
      dbConnection = DriverManager.getConnection(this.connectionString);
      statement = dbConnection.prepareStatement(this.sqlString);

      if (arguments.length > 0)
      {
         setArguments(arguments);
      }
   }

   /**
    * Adds another set of arguments to the cmd so it
    * will execute in one batch.
    * 
    * @param args
    *    the arguments for the sql statement.
    * @throws SQLException
    *    Any exception will be thrown.
    */
   public void addToBatch(Object... args) throws SQLException
   {
      statement.addBatch();
      setArguments(args);
      batchMode = true;
   }

   /**
    * Executes a query against the database, sending each row to
    * the processor implementation passed to the method. 
    * 
    * @param processor
    *    The result set processor that will process each row. This 
    *    cannot be null.
    * @return
    *    Number of rows in the query.
    * @throws SQLException
    *    Any exceptions will be thrown.
    */
   public int executeQuery(ResultSetProcessor processor) throws SQLException
   {
      int totalRows = 0;
      
      resultSet = statement.executeQuery();
      while (resultSet.next())
      {
         totalRows++;
         processor.processRow(resultSet);
      }
      
      return totalRows;
   }
   
   /**
    * Executes a query against the database, sending the 
    * result set, row by row, to the mapper implementation
    * that will create objects based on each row in the 
    * result set.
    * 
    * @param mapper
    *    The result set mapper that will create objects 
    *    based on the row the result set passed to the mapper
    *    represents.
    * @param <T>
    *    The type of object the row mapper will create.
    * @return
    *    Number of rows in the query.
    * @throws SQLException
    *    Any exceptions will be thrown.
    */
   public <T> List<T> executeQuery(ResultSetMapper<T> mapper) throws SQLException
   {
      ArrayList<T> results = new ArrayList<>();
     
      resultSet = statement.executeQuery();
      while (resultSet.next())
      {
         results.add(mapper.processRow(resultSet));
      }
    
      return results;
   }

   /**
    * Executes a non-query operation against the database. 
    * 
    * @return
    *    The number of rows affected by the statement
    *    executed.
    *    
    * @throws SQLException
    *    Any exceptions will be thrown.
    */
   public int executeNonQuery() throws SQLException
   {
      if (!batchMode)
      {
         return statement.executeUpdate();
      }

      int resultCount = 0;
      statement.addBatch();
      int[] results = statement.executeBatch();
      for (int i : results)
      {
         resultCount += i;
      }
      return resultCount;     
   }


   /**
    * Implementation of the auto close option, allowing for the
    * database objects to be used and closed using the
    * try-with-resources statement.
    */
   public void close() throws Exception
   {
      if (resultSet != null) 
      {
         try {resultSet.close(); } catch (Exception ex) { ; }
      }
      
      try {statement.close(); } catch (Exception ex) { ; }     
      try {dbConnection.close(); } catch (Exception ex) { ; }
   }

   /**
    * Sets the arguments for the database command
    * 
    * @param values
    *    The argument values
    * @throws java.sql.SQLException
    *    Any SQL Exceptions will be thrown back to 
    *    the calling function. 
    */
   private void setArguments(Object[] values) throws SQLException
   {
      for (int index = 0; index != values.length; ++index)
      {
         if (values[index] instanceof Integer)
         {
            statement.setInt(index + 1, ((Integer) values[index]).intValue());
         }
         else if (values[index] instanceof Long)
         {
            statement.setLong(index + 1, (Long) values[index]);
         }
         else if (values[index] instanceof BigDecimal)
         {
            statement.setBigDecimal(index + 1, (BigDecimal) values[index]);
         }
         else if (values[index] instanceof String)
         {
            statement.setString(index + 1, (String) values[index]);
         }
         else if (values[index] instanceof java.util.Date)
         {
            statement.setDate(index + 1, new java.sql.Date(((java.util.Date) values[index]).getTime()));
         }
         else if (values[index] instanceof Boolean)
         {
            statement.setBoolean(index + 1, (Boolean) values[index]);
         }
         else
         {
            throw new IllegalArgumentException(
                  "Index " + index + " contains a class that is not support (" + values[index].getClass().getCanonicalName());
         }
      }
   }
}
