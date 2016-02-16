package ca.bjad.util.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * General interface for processing results from the
 * a database job object's query.
 * 
 * <br><br><i>Note: you should only be using the result
 * set's getXXX(x) methods to get data, as the database 
 * job will increment the result set row by row. </i>
 * 
 * @author
 *    Ben Dougall
 * @version
 *    1.0 (January 2012)
 *    - Interface created.     
 *
 */
public interface ResultSetProcessor
{
   /**
    * Receives the result set object so the implementation 
    * can gather data from the database query. 
    * 
    * <br><br>
    * <i>Note: you should only be using the result
    * set's getXXX(x) methods to get data, as the database 
    * job will increment the result set row by row. </i>
    * 
    * @param rs
    *    The result set object containing the row's data.
    *    
    * @throws SQLException
    *    Any exceptions will be thrown.
    */
   public void processRow(ResultSet rs) throws SQLException;
}

