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

@WebServlet("/LoginUser")
public class LoginServlet extends HttpServlet{
			@Override
			protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String email = request.getParameter("email");
			String password = request.getParameter("password");  
	        response.setContentType("text/plain");
	        PrintWriter out = response.getWriter();
        
	        try {
	        	
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_info", "root", "root");
	            
	            String query = "SELECT email,password FROM Users WHERE email = ?";
	            PreparedStatement pst = conn.prepareStatement(query);
	            pst.setString(1, email);
	            ResultSet rs = pst.executeQuery();
	            
	            if (rs.next()) {
	                if (rs.getString("password").equals(password)) {
	                	out.println(true);
	                }
	                else {
	                	out.println(false);
	                }
	                
	            } else {
	                out.println(false);
	                
	            }
	            
	            pst.close();
	            conn.close();       
	        } catch (Exception e) {
	            out.print("false"); 
	            e.printStackTrace();
	        } finally {
	        	out.flush();
	        	out.close();
	        }
		}
	}
