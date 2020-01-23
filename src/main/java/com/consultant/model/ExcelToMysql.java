import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Iterator;

public class ExcelToMysql {

    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/mirado_internal";
        String username = "panosstylianides";
        String password = "panosroot";

        String excelFilePath = "/Users/panosstylianides/Desktop/Candidates.xlsx";

        Connection connection = null;

        try {
            long start = System.currentTimeMillis();

            FileInputStream inputStream = new FileInputStream(excelFilePath);

            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();

            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            java.sql.Statement maxStatement = connection.createStatement();
            String sql = "select max(id) as maxId from candidates;";
            ResultSet rs = maxStatement.executeQuery(sql);

            int id = findMax(rs);

            sql = "INSERT INTO candidates  (id,role,company,location,comment,linkedin_url,consultant,source,diverse) " +
                    "VALUES (?, ?, ?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            rowIterator.next(); // skip the header row

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = nextRow.cellIterator();
                statement.setInt(1, id);
                int column = 2;
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();

                    String name = nextCell.getStringCellValue();
                    if (name.isEmpty()) {
                        break;
                    }
                    statement.setString(column, name);
                    column++;

                }
                id++;

                statement.addBatch();
                statement.executeBatch();

            }

            workbook.close();

            // execute the remaining queries
            statement.executeBatch();

            connection.commit();
            connection.close();

            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));

        } catch (IOException ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
        } catch (SQLException ex2) {
            System.out.println("Database error");
            ex2.printStackTrace();
        }

    }

    private static int findMax(ResultSet rs) throws SQLException {
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        id++;
        return id;
    }
}
