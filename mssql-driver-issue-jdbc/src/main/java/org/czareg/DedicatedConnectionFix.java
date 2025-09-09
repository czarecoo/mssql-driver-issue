package org.czareg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DedicatedConnectionFix {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=YourStrong!Passw0rd;encrypt=false";

        try (Connection conn = DriverManager.getConnection(url)) { // SPECIAL CONNECTION USED ONLY FOR ONE SELECT
            System.out.println("Connected to master.");

            // Big query from sys.all_objects cross join
            String hugeQuery = "SELECT TOP (10000000)\n" +
                    "    a.name AS col1,\n" +
                    "    b.name AS col2\n" +
                    "FROM sys.all_objects a\n" +
                    "CROSS JOIN sys.all_objects b;";

            Statement stmt1 = conn.createStatement();

            ResultSet rs1 = stmt1.executeQuery(hugeQuery);
            System.out.println("Huge query started...");

            int count = 0;
            while (rs1.next() && count < 1000) { // just fetch 1000 rows for demo
                rs1.getString(1);
                rs1.getString(2);
                count++;
            }
            System.out.println("Huge query partially read, first 1000 rows processed");

        }
        try (Connection conn = DriverManager.getConnection(url)) { // normal connection from the pool
            System.out.println("Connected to master.");

            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT 1");

            System.out.println("Sleeping for observation...");
        }
        Thread.sleep(600_000); // 10 minutes
    }
}
