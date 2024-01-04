package org.rybina;

import util.ConnectionManager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetaDataRunner {
    /**
     * "метаданные" относятся к данным, описывающим структуру базы данных,
     * а не к самим данным базы данных. Эти метаданные включают информацию
     * о таблицах, столбцах, типах данных, ключах и других аспектах базы
     * данных. В Java, DatabaseMetaData предоставляет способы для извлечения этой информации.
     */
    private static void checkMetaData() {
        try (Connection connection = ConnectionManager.open()) {
            DatabaseMetaData metaData = connection.getMetaData();

//          -------------------------------------------------
//          CATALOG
            ResultSet catalog = metaData.getCatalogs();
            while (catalog.next()) {
                System.out.println(catalog.getString(1));
//                EQUAL TO
//                System.out.println(catalog.getString("TABLE_CAT"));

//                -------------------------------------------------
//                SCHEMA
                ResultSet schemas = metaData.getSchemas();
                while (schemas.next()) {
                    String schema = schemas.getString("TABLE_SCHEM");
                    System.out.println(schema);

//                  -------------------------------------------------
//                  TABLES
                    ResultSet tables = metaData.getTables(catalog.getString(1), schemas.getString("TABLE_SCHEM"), "%", new String[]{"TABLE"});

                    if (schema.equals("public")) {
                        while (tables.next()) {
                            System.out.println(tables.getString("TABLE_NAME"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        checkMetaData();
    }
}
