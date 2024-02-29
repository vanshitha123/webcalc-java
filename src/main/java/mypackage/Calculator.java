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

    public long addFucn(long first, long second) {
        return first + second;
    }

    public long subFucn(long first, long second) {
        return second - first;
    }

    public long mulFucn(long first, long second) {
        return first * second;
    }

    // Database connectivity parameters
    private static final String JDBC_URL = "jdbc:mysql://192.168.138.114/myDB";
    private static final String JDBC_USER = "mysql";
    private static final String JDBC_PASSWORD = "mysql";

    // Database connection method
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    // Sample method for database interaction
    public int retrieveDataFromDatabase() {
        int result = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT your_column FROM your_table");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                result = resultSet.getInt("your_column");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }

        return result;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            int a1 = Integer.parseInt(request.getParameter("n1"));
            int a2 = Integer.parseInt(request.getParameter("n2"));

            if (request.getParameter("r1") != null) {
                out.println("<h1>Addition</h1>" + addFucn(a1, a2));
            }
            if (request.getParameter("r2") != null) {
                out.println("<h1>Subtraction</h1>" + subFucn(a1, a2));
            }
            if (request.getParameter("r3") != null) {
                out.println("<h1>Multiplication</h1>" + mulFucn(a1, a2));
            }

            // Database interaction example
            int dataFromDatabase = retrieveDataFromDatabase();
            out.println("<h1>Data from Database:</h1>" + dataFromDatabase);

            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
