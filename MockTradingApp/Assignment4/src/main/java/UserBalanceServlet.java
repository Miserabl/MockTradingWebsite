import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

@WebServlet("/getUserBalance")
public class UserBalanceServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    
	    try {
	    	  Class.forName("com.mysql.cj.jdbc.Driver");
	          Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_info", "root", "root");
	          double balance;
	          
	          String q1 = "SELECT balance FROM Users WHERE email = ?";
	          PreparedStatement pst1 = conn.prepareStatement(q1);
	          pst1.setString(1, email);
	          ResultSet rs1 = pst1.executeQuery();
	          if (rs1.next()) {
	        	  balance = rs1.getDouble(1);
	          }
	          else {
	        	  balance = -1;
	          }
	          
	          
	          String query = "SELECT SUM(price) AS total FROM portfolio WHERE email = ?";
	          PreparedStatement pst2 = conn.prepareStatement(query);
	          pst2.setString(1, email);
	          ResultSet rs2 = pst2.executeQuery();
	          double value;
	          
	          if (rs2.next()) {
	                value = rs2.getDouble(1);
	            } else {
	            	value = -1;
	            }
	          
	          Map<String, Double> data = new HashMap<>();
	          data.put("balance",  balance);
	          data.put("totalval", value);
	          System.out.println(value);
	          
	          Gson gson = new Gson();
	          String json = gson.toJson(data);
	          
	          out.print(json);
	    } catch (Exception e) {
            out.print("false");
            e.printStackTrace();      
	    } finally {
	    	 out.flush();
	         out.close();
	    }
	}
	
	
}
