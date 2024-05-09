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

@WebServlet("/buyStock")
public class BuyStockServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int num = Integer.parseInt(request.getParameter("quantity"));
		double price = Double.parseDouble(request.getParameter("price"));
		String email = request.getParameter("email");
		String ticker = request.getParameter("ticker");
		PrintWriter out = response.getWriter();
		System.out.println(email);
		try {	
			
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_info", "root", "root");
            String query = "SELECT email,balance FROM Users WHERE email = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            
            if (!rs.next()) {
                out.print(false);
                
            } else {
            	double cost = Double.parseDouble(rs.getString("balance"));
            	if (cost >= price * num) {
            		String q2 = "UPDATE Users SET balance = ? WHERE email = ?";
            		PreparedStatement pst2 = conn.prepareStatement(q2);
            		pst2.setDouble(1, cost - (price*num));
            		pst2.setString(2, email);
            		int rs2 = pst2.executeUpdate();
            		
            		
            		String s3 = "INSERT INTO Portfolio (email, ticker, numStock, price) VALUES (?, ?, ?, ?) ";
            		PreparedStatement pst3 = conn.prepareStatement(s3);
            		 
            		pst3.setString(1, email);
                    pst3.setString(2, ticker);
                    pst3.setInt(3, num);
                    pst3.setDouble(4, price * num);
                    
                    int rs3 = pst3.executeUpdate();
                    out.print(true);
            		 
            	}
            	else {
            		System.out.println("asdsa");
            		out.print(false);
            	}
            }
   
        }	catch (Exception e) {
        	out.print("false"); // Return false if there's an exception
	        e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

}
