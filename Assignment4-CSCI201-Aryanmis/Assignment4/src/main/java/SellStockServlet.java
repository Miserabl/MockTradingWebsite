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

@WebServlet("/sellStock")
public class SellStockServlet extends HttpServlet{
	
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
            String query = "SELECT ticker, SUM(numStock) AS totalStock FROM portfolio WHERE email = ? GROUP BY ticker";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();
            
            
            
            if (!rs.next()) {
                out.print(false);
                
            } else {
            	int total = Integer.parseInt(rs.getString("totalStock"));
            	
            	if (num <= total ) {
            		
            		if (num == total) {
            			String s4 = "DELETE FROM portfolio WHERE email = ? AND ticker = ?";
            			PreparedStatement pst4 = conn.prepareStatement(s4);
                		pst4.setString(1, email);
                		pst4.setString(2,  ticker);
                		int rs4 = pst4.executeUpdate();
            		}
            		else {
            			//is there any  way to write a query that takes the last row of the returned call, 
            			//and if the number of a column is less than what i have to get rid of, it deletes the row and the amount
            			//remaining, and keeps doing this until there is no more of the amount im getting rid off
            			//ChatGPT, GPT-4,Open AI,23 April, 2024, chat.openai.com/chat
            			// 26 Lines
            			int numtoSell = num;
            			while (numtoSell > 0) {
            				System.out.println(numtoSell);
            				String s5 = "SELECT trade_id, numStock FROM portfolio WHERE email = ? AND ticker = ? ORDER BY trade_id DESC LIMIT 1";
            				PreparedStatement pst5 = conn.prepareStatement(s5);
            				pst5.setString(1, email);
            		        pst5.setString(2, ticker);
            		        ResultSet rs5 = pst5.executeQuery();
            		        if (rs5.next()) {
            		            int id = rs5.getInt("trade_id");
            		            int numStock = rs5.getInt("numStock");
            		            if (numStock <= numtoSell) {
            		                String deleteSql = "DELETE FROM portfolio WHERE trade_id = ?";
            		                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            		                    deleteStmt.setInt(1, id);
            		                    deleteStmt.executeUpdate();
            		                }
            		                numtoSell -= numStock; 
            		            } else {
            		            	System.out.println("a");
            		                String updateSql = "UPDATE portfolio SET numStock = numStock - ?, price = price - ((SELECT (price / numStock) * ? FROM (SELECT price, numStock FROM portfolio WHERE trade_id = ?) AS temp)) WHERE trade_id = ?";
            		                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            		                    updateStmt.setInt(1, numtoSell);
            		                    updateStmt.setInt(2, numtoSell);
            		                    updateStmt.setInt(3, id);
            		                    updateStmt.setInt(4, id);
            		                    updateStmt.executeUpdate();
            		                }
            		                numtoSell = 0; 
            		            }
            		        }
            			}
            		}
            		String q2 = "UPDATE Users SET balance = balance + ? WHERE email = ?";
            		PreparedStatement pst2 = conn.prepareStatement(q2);
            		pst2.setDouble(1, price * num);
            		pst2.setString(2, email);
            		int rs2 = pst2.executeUpdate();
            		
            		out.print(true);
            		 
            	}
            	else {
            		System.out.println("asdsa");
            		out.print(false);
            	}
            }
   
        }	catch (Exception e) {
        	out.print(false);
	        e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

}
