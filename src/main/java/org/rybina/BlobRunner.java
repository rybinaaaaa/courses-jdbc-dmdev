package org.rybina;

import util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BlobRunner {
    public static void main(String[] args) throws SQLException {
        saveImage();
    }

    private static void saveImage() throws SQLException {
        String sql = """
                update aircraft set image = ? where id = 1
                """;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

//            ПОСТГРЕС НЕ ПОДДЕРЖИВАЕТ БЛОБ И КЛОБ
//            Blob blob = connection.createBlob();
//            blob.setBytes(1, Files.readAllBytes(Path.of("resources", "Boeing.jpg")));
//
//            preparedStatement.setBlob(1, blob);

            preparedStatement.setBytes(1, Files.readAllBytes(Path.of("src/main/resources", "Boeing.jpg")));

            preparedStatement.executeUpdate();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
