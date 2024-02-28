package mypackage;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Calculator extends HttpServlet
{
	public long addFucn(long first, long second){
		
		return first+second;
	}
	
	public long subFucn(long first, long second){
		
		return second-first;
	}
	
	public long mulFucn(long first, long second){
		
		return first*second;
	}
	
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
        response.setContentType("text/html");
        PrintWriter out= response.getWriter();
        int a1= Integer.parseInt(request.getParameter("n1"));
        int a2= Integer.parseInt(request.getParameter("n2"));
        
        
        
        if(request.getParameter("r1")!=null)
        {
            out.println("<h1>Addition</h1>"+addFucn(a1, a2));
        }
        if(request.getParameter("r2")!=null)
        {
            out.println("<h1>Substraction</h1>"+subFucn(a1, a2));
        }
        if(request.getParameter("r3")!=null)
        {
            out.println("<h1>Multiplication</h1>"+mulFucn(a1, a2));
        }
        RequestDispatcher rd=request.getRequestDispatcher("/index.jsp");  
        rd.include(request, response);  
        }
        catch(Exception e)
        {

        }
    }
}
