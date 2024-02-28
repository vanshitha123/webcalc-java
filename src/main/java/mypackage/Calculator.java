package mypackage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.*;
import javax.servlet.http.*;

public class Calculator extends HttpServlet {
    // ... existing methods ...

    // Method to establish database connection
    private Connection getDBConnection() throws Exception {
        String jdbcURL = "jdbc:mysql://192.168.138.126:3306/myDB";
        String dbUser = "mysql";
        String dbPassword = "mysql";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        return connection;
    }

    // Method to save calculation result to the database
    private void saveResultToDatabase(String operation, long result) {
        try (Connection connection = getDBConnection()) {
            String query = "INSERT INTO calculations (operation, result) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, operation);
                preparedStatement.setLong(2, result);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
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
                saveResultToDatabase("Addition", result);
            }
            if (request.getParameter("r2") != null) {
                long result = subFucn(a1, a2);
                out.println("<h1>Subtraction</h1>" + result);
                saveResultToDatabase("Subtraction", result);
            }
            if (request.getParameter("r3") != null) {
                long result = mulFucn(a1, a2);
                out.println("<h1>Multiplication</h1>" + result);
                saveResultToDatabase("Multiplication", result);
            }

            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
