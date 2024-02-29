// Import necessary packages
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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

    // Method to store calculation results in the database
    public void storeResult(long result, String operation, long first, long second) {
        String jdbcUrl = "jdbc:mysql://192.168.138.114:3306/myDB";
        String jdbcUser = "mysql";
        String jdbcPassword = "mysql";

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

            // Create a PreparedStatement
            String query = "INSERT INTO calculator_results (first_number, second_number, operation, result) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, first);
            preparedStatement.setLong(2, second);
            preparedStatement.setString(3, operation);
            preparedStatement.setLong(4, result);

            // Execute the query
            preparedStatement.executeUpdate();

            // Close resources
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
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
                storeResult(result, "addition", a1, a2);
            }
            if (request.getParameter("r2") != null) {
                long result = subFucn(a1, a2);
                out.println("<h1>Subtraction</h1>" + result);
                storeResult(result, "subtraction", a1, a2);
            }
            if (request.getParameter("r3") != null) {
                long result = mulFucn(a1, a2);
                out.println("<h1>Multiplication</h1>" + result);
                storeResult(result, "multiplication", a1, a2);
            }

            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
