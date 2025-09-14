package org.czareg;

import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class DataInserter {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=master";
    private static final String USER = "sa";
    private static final String PASSWORD = "YourStrong!Passw0rd";

    private static final int CHILDREN_COUNT = 1_000_000;   // number of children to generate
    private static final int BATCH_SIZE = 10_000;          // tune for performance

    public static void main(String[] args) {
        Faker faker = new Faker();
        Random random = new Random();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            // 1) Insert parent node
            long parentId = 1L;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO node (id, name, created_at, modified_at, description, version) VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setLong(1, parentId);
                ps.setString(2, "Parent Node");
                ps.setDate(3, Date.valueOf(LocalDate.now()));
                ps.setDate(4, Date.valueOf(LocalDate.now()));
                ps.setString(5, "This is the parent node");
                ps.setLong(6, 1L);
                ps.executeUpdate();
            }

            // 2) Prepare statements
            PreparedStatement nodeStmt = conn.prepareStatement(
                    "INSERT INTO node (id, name, created_at, modified_at, description, version) VALUES (?, ?, ?, ?, ?, ?)");
            PreparedStatement assocStmt = conn.prepareStatement(
                    "INSERT INTO child_assoc (id, parent_id, child_id) VALUES (?, ?, ?)");

            long idCounter = 2; // start after parent

            // 3) Generate children + associations
            for (int i = 0; i < CHILDREN_COUNT; i++) {
                long childId = idCounter++;
                long assocId = idCounter++;

                // child node
                nodeStmt.setLong(1, childId);
                nodeStmt.setString(2, faker.book().title());
                nodeStmt.setDate(3, Date.valueOf(LocalDate.now().minusDays(random.nextInt(1000))));
                nodeStmt.setDate(4, Date.valueOf(LocalDate.now()));
                nodeStmt.setString(5, faker.lorem().sentence(10));
                nodeStmt.setLong(6, 1L);
                nodeStmt.addBatch();

                // association
                assocStmt.setLong(1, assocId);
                assocStmt.setLong(2, parentId);
                assocStmt.setLong(3, childId);
                assocStmt.addBatch();

                // flush batches
                if ((i + 1) % BATCH_SIZE == 0) {
                    nodeStmt.executeBatch();
                    assocStmt.executeBatch();
                    conn.commit();
                    System.out.printf("Inserted %,d children%n", i + 1);
                }
            }

            // final flush
            nodeStmt.executeBatch();
            assocStmt.executeBatch();
            conn.commit();

            nodeStmt.close();
            assocStmt.close();

            System.out.printf("✅ Done! Inserted %d children with parent %d%n", CHILDREN_COUNT, parentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
