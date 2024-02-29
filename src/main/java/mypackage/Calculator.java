package mypackage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    private Connection getDBConnection() throws SQLException {
        // Replace with your database connection details
        String jdbcUrl = "jdbc:mysql://192.168.138.114:3306/myDB";
        String jdbcUser = "mysql";
        String jdbcPassword = "mysql";
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }

    private void saveToDatabase(String operation, long result) {
        try (Connection connection = getDBConnection()) {
            String query = "INSERT INTO calculations (operation, result) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, operation);
                statement.setLong(2, result);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Data successfully inserted into the database.");
                } else {
                    System.err.println("Failed to insert data into the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            int a1 = Integer.parseInt(request.getParameter("n1"));
            int a2 = Integer.parseInt(request.getParameter("n2"));

            if (request.getParameter("r1") != null) {
                long result = addFucn(a1, a2);
                out.println("<h1>Addition</h1>" + result);
                saveToDatabase("Addition", result);
            }
            if (request.getParameter("r2") != null) {
                long result = subFucn(a1, a2);
                out.println("<h1>Substraction</h1>" + result);
                saveToDatabase("Subtraction", result);
            }
            if (request.getParameter("r3") != null) {
                long result = mulFucn(a1, a2);
                out.println("<h1>Multiplication</h1>" + result);
                saveToDatabase("Multiplication", result);
            }

            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test the functionality with a simple main method
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        long resultAdd = calculator.addFucn(5, 3);
        long resultSub = calculator.subFucn(5, 3);
        long resultMul = calculator.mulFucn(5, 3);

        System.out.println("Addition: " + resultAdd);
        System.out.println("Subtraction: " + resultSub);
        System.out.println("Multiplication: " + resultMul);
    }
}
