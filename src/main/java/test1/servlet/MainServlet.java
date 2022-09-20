package test1.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// SQL query
	private static final String SELECT_CARS_QUERY = 
	    "select * from cars where model = '";
	
	public MainServlet() {
		super();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  // variables
	      PrintWriter pw = null;
	      Connection con = null;
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	      boolean flag = false;

	      // set content type
	      response.setContentType("text/html");
	      // get Writer
	      pw = response.getWriter();

	      // get form data
	      String model = request.getParameter("model");

	      try {
	    	  
	         // register JDBC driver
	         Class.forName("com.mysql.cj.jdbc.Driver");
	         // create JDBC connection
	         con = DriverManager.getConnection(
	               "jdbc:mysql://localhost:3306/taskdb", "root", "452136");
	         // compile SQL query and store it in
	         // PreparedStatement object
	         if (con != null)
	            ps = con.prepareStatement(SELECT_CARS_QUERY + model + "'");
	         // execute the query
	         if (ps != null)
	            rs = ps.executeQuery();

	         // process the result
	         if (rs != null) {
	            while (rs.next()) {
	               // display result
	               flag = true;
	               pw.println("ID: " + rs.getString("id") + ", Model: " 
	            		   + rs.getString("model") + ", Reg Number: " 
	            		   + rs.getString("regnumber") + ", Year: " + rs.getString("year") + "<br>");
	            }
	         }

	         // Student not found
	         if (!flag) {
	            pw.println("<h1>Cars Not Found.</h1>");
	         }

	      } catch (SQLException se) {
	         se.printStackTrace();
	         pw.println("Error Occured");
	      } catch (Exception e) {
	         e.printStackTrace();
	         pw.println("Unknown Exception Occured");
	      } finally {
	         // close JDBC connection
	         try {
	            if (rs != null)
	               rs.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	         try {
	            if (ps != null)
	               ps.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	         try {
	            if (con != null)
	               con.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }

	         // Link to home
	         pw.println("<h3><a href='index.jsp'>Back</a></h3>");
	         // close stream
	         pw.close();
	      }
	}
}
