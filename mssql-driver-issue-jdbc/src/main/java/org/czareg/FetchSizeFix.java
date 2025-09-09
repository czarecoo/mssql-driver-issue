package org.czareg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FetchSizeFix {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=YourStrong!Passw0rd;encrypt=false";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to master.");

            // Big query from sys.all_objects cross join
            String hugeQuery = "SELECT TOP (10000000)\n" +
                    "    a.name AS col1,\n" +
                    "    b.name AS col2\n" +
                    "FROM sys.all_objects a\n" +
                    "CROSS JOIN sys.all_objects b;";

            Statement stmt1 = conn.createStatement();


            stmt1.setFetchSize(1000); // <- FIX?


            ResultSet rs1 = stmt1.executeQuery(hugeQuery);
            System.out.println("Executed huge query (not consuming).");

            // Immediately fire a second query
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT 1");
            System.out.println("Executed second query while first is still open.");

            System.out.println("Sleeping for observation...");
            Thread.sleep(600_000); // 10 minutes
        }
    }
}
