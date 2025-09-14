package org.czareg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=master";
    private static final String USER = "sa";
    private static final String PASSWORD = "YourStrong!Passw0rd";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute("IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='node' AND xtype='U')\n" +
                    "                CREATE TABLE node (\n" +
                    "                    id BIGINT PRIMARY KEY,\n" +
                    "                    name NVARCHAR(255),\n" +
                    "                    created_at DATE,\n" +
                    "                    modified_at DATE,\n" +
                    "                    description NVARCHAR(1000),\n" +
                    "                    version BIGINT\n" +
                    "                );");

            stmt.execute("IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='child_assoc' AND xtype='U')\n" +
                    "                CREATE TABLE child_assoc (\n" +
                    "                    id BIGINT PRIMARY KEY,\n" +
                    "                    parent_id BIGINT NOT NULL,\n" +
                    "                    child_id BIGINT NOT NULL,\n" +
                    "                    FOREIGN KEY (parent_id) REFERENCES node(id),\n" +
                    "                    FOREIGN KEY (child_id) REFERENCES node(id)\n" +
                    "                );");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
