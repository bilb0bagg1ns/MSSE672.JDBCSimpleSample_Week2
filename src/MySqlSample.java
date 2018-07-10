
/*-
 * 1. Make sure you are done with MySQL tutorial of installing MySQL and
 * configuring it.
 *
 * 2. Follow instructions in readme.txt file to download JDBC driver jar
 *
 * Note: Some IDEs (for example NetBeans) include these jars as part of its
 * installations. One could use this to compile and run JDBC code. However, one has
 * to remember that in the real world, when code gets delivered to QA and on, all
 * source code and dependency libraries need to be packaged (much like how we did
 * in MSSE 670, week8, where we used Ant (Maven is another tool) to build our
 * JARs). Using pre-loaded IDE JARs can become a major issue. Ok to do so in an
 * academic setting but recognize its limitation.
 *
 * 3. Ensure that JDBC Driver is in your classpath.
 *
 * 4. Start MySql database
 * - Confirm by logging into it via command line or using MySql Workbench UI
 *
 * 5. Compile and Run MySqlSample.java
 *
 * NOTE: If you are using ant, then  you need to set the property correctly in
 * build.xml file.
 *
 *
 *
 * Execution: Upon successfully running, you should see an output like this:
 *
    run:
    Registering MySQL Driver successful
    Retrieving MySQL connection successful

    Result set returned: 
    Host: localhost
    User: mysql.sys
    Host: localhost
    User: root
 *
 * @author Mike Prasad
 */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlSample {

    public MySqlSample() {
    }

    /**
     * Registers driver, and gets connection to the database
     *
     * @return Connection Connection returned on success otherwise returns null
     */
    private Connection fetchConnection() {

        String url;
        String userid;
        String password;

        Connection connection = null;

        /*-
         **********************************************************
         * CHANGE THE URL, USERID and PASSWORD BELOW TO YOUR SETTINGS
         ***********************************************************
         */

        /*
	This is sample code. In your HW, the connection string and the 
        credentials should be loaded from the properties file.
         */
        //mysql after 3306 indicates the use of the default database.
        url = "jdbc:mysql://localhost:3306/mysql";
        userid = "root";   // userid that I used to while installing MySql
        password = "root"; // password that I set in my install. POOR PASSWORD!

        // load and register JDBC driver then connect to database
        // Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new 
        // driver class is `com.mysql.cj.jdbc.Driver'. 
        // The driver is automatically registered via the SPI and manual 
        // loading of the driver class is generally unnecessary.

        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            System.out.println("Registering MySQL Driver successful");

            connection = DriverManager.getConnection(url, userid, password);
            System.out.println("Retrieving MySQL connection successful");

        } catch (SQLException e) {
            System.err.print("Could not load and register JDBC driver or connect to database.");
            System.err.println(e.getMessage());
        }
        return connection;
    }

    /**
     * Fetches connection from database and lists contents of a USER table
     */
    public void listContents() {
        Statement stmt = null;
        Connection conn = null;

        try {
            //fetch db connection
            conn = fetchConnection();

            if (conn != null) //if we have a connection
            {
                stmt = conn.createStatement();

                // Select the columns from the USER table.
                ResultSet rset = stmt.executeQuery("select HOST, USER from USER");

                // Iterate through the result set and print its contents.
                System.out.println("\nResult set returned: ");

                while (rset.next()) {
                    System.out.println("Host: " + rset.getString(1));
                    System.out.println("User: " + rset.getString(2));
                }
            } else {
                System.out.println("Issue during fetching connection.");
            }
        } catch (SQLException e) {
            //replace with log4 logging
            //log.error  (e.getClass()+": "+ e.getMessage(), e);	
            System.out.println(e.getClass() + ": " + e.getMessage());
            StringWriter errors;
            e.printStackTrace(new PrintWriter(errors = new StringWriter()));
            System.out.println(errors.toString());
        } finally //must release all open resources in finally block
        {
            try {
                // check for null first before closing resources
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                //replace with log4 logging
                //log.error  (e.getClass()+": "+ e.getMessage(), e);	
                System.out.println(e.getClass() + ": " + e.getMessage());
                StringWriter errors;
                e.printStackTrace(new PrintWriter(errors = new StringWriter()));
                System.out.println(errors.toString());
            }
        }
    } // end listContents()

    public static void main(String[] args) {
        MySqlSample myConn = new MySqlSample();
        myConn.listContents();
    }//end main

}//end MySqlSample
