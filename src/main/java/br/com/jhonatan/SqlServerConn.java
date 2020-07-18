package br.com.jhonatan;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

/**
 * This program demonstrates how to establish database connection to Microsoft
 * SQL Server.
 * @author www.codejava.net
 *
 */
public class SqlServerConn {

    public static Connection getConnection() {

        Connection conn = null;

        try {

            String dbURL = "jdbc:sqlserver://localhost:1433;database=LISTA";
            String user = "jhonatan";
            String pass = "jhonatan";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            conn = DriverManager.getConnection(dbURL, user, pass);
            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }

            return conn;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}