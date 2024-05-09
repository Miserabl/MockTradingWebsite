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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

@WebServlet("/getUserStocks")
public class UserPortfolioServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		try {
			Gson gson = new Gson();
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_info", "root", "root");
	        
	        //" How to sum values in an SQL Query" GPT-4, 4/23/2024. (1) line
	        //ChatGPT, Version 3.5, OpenAI, 22 April 2024, chat.openai.com/chat. 1 line
	        String q1 = "SELECT ticker, SUM(numStock) AS totalStock, SUM(price) AS totalPrice FROM portfolio WHERE email = ? GROUP BY ticker";
	        
	        PreparedStatement pst1 = conn.prepareStatement(q1);
	        pst1.setString(1, email);
	        
	        List<Stock> stocks = new ArrayList<Stock>();
	        String apikey = "cnt8q91r01qi1jjgs07gcnt8q91r01qi1jjgs080";
	        
	        ResultSet rs1 = pst1.executeQuery();
	        
	        while (rs1.next()) {
	        	 String ticker = rs1.getString("ticker");
	        	 String apiquote = "https://finnhub.io/api/v1/quote?symbol=" + ticker + "&token=" + apikey;
	         	 String apiprofile = "https://finnhub.io/api/v1//stock/profile2?symbol=" + ticker + "&token=" + apikey;
	        	 Double price = rs1.getDouble("totalPrice");
	         	 int quant = rs1.getInt("totalStock");
	         	 
	         	 URL urlQ = new URL(apiquote);
	             HttpURLConnection connection = (HttpURLConnection) urlQ.openConnection();
	             connection.setRequestMethod("GET");
	             BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	             String inputLine;
	             String responseApiQ = "";
	             while ((inputLine = in.readLine()) != null) {
	                     responseApiQ += inputLine;
	              }
	             
	             Quote q = gson.fromJson(responseApiQ, Quote.class);
	             
	             //apiprofile get
	             URL urlP = new URL(apiprofile);
	             HttpURLConnection connection2 = (HttpURLConnection) urlP.openConnection();
	             connection2.setRequestMethod("GET");
	             BufferedReader in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
	             String inputLine2;
	             String responseApiP = "";
	             while ((inputLine2 = in2.readLine()) != null) {
	                     responseApiP += inputLine2;
	              }
	             Profile p = gson.fromJson(responseApiP, Profile.class);
	             
	             Stock s = new Stock(p, q);
	             in.close();
	             in2.close();
	             s.quant = quant;
	             s.total = price;
	             stocks.add(s);
	             
	          }
	       String json = gson.toJson(stocks);
	       out.print(json);
		} catch (Exception e) {
            out.print("false");
            e.printStackTrace();    
		}finally {
			out.flush();
	        out.close();
		}
	}
}
