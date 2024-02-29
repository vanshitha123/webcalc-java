package mypackage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class Calculator extends HttpServlet {

    // Database connectivity parameters
    private static final String JDBC_URL = "jdbc:mysql://your_mysql_host:3306/your_database";
    private static final String JDBC_USER = "your_mysql_user";
    private static final String JDBC_PASSWORD = "your_mysql_password";

    // Database connection method
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    // Create table SQL statement
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS calculator_data (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "calculation_type VARCHAR(20)," +
                    "operand1 INT," +
                    "operand2 INT," +
                    "result INT" +
                    ")";

    // Execute table creation
    static {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE_SQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sample method for database interaction
    public void storeCalculationResult(String calculationType, long operand1, long operand2, long result) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO calculator_data (calculation_type, operand1, operand2, result) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, calculationType);
            preparedStatement.setLong(2, operand1);
            preparedStatement.setLong(3, operand2);
            preparedStatement.setLong(4, result);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }
    }

    public long addFucn(long first, long second) {
        return first + second;
    }

    public long subFucn(long first, long second) {
        return second - first;
    }

    public long mulFucn(long first, long second) {
        return first * second;
    }

    public int retrieveDataFromDatabase() {
        int result = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT result FROM calculator_data");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                result += resultSet.getInt("result");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }

        return result;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            int a1 = Integer.parseInt(request.getParameter("n1"));
            int a2 = Integer.parseInt(request.getParameter("n2"));

            if (request.getParameter("r1") != null) {
                long result = addFucn(a1, a2);
                out.println("<h1>Addition</h1>" + result);
                storeCalculationResult("Addition", a1, a2, result);
            }
            if (request.getParameter("r2") != null) {
                long result = subFucn(a1, a2);
                out.println("<h1>Subtraction</h1>" + result);
                storeCalculationResult("Subtraction", a1, a2, result);
            }
            if (request.getParameter("r3") != null) {
                long result = mulFucn(a1, a2);
                out.println("<h1>Multiplication</h1>" + result);
                storeCalculationResult("Multiplication", a1, a2, result);
            }

            // Database interaction example
            int dataFromDatabase = retrieveDataFromDatabase();
            out.println("<h1>Data from Database:</h1>" + dataFromDatabase);

            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error Occurred</h1>");
        }
    }
}
