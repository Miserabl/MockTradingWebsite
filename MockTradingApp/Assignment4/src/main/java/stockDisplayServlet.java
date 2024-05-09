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
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

@WebServlet("/showStock")
public class stockDisplayServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String apikey = "cnt8q91r01qi1jjgs07gcnt8q91r01qi1jjgs080";
        

        try {
        	String ticker = request.getParameter("ticker");
        	String apiquote = "https://finnhub.io/api/v1/quote?symbol=" + ticker + "&token=" + apikey;
        	String apiprofile = "https://finnhub.io/api/v1//stock/profile2?symbol=" + ticker + "&token=" + apikey;
        	 Gson gson = new GsonBuilder()
         			.setPrettyPrinting()
         			.create();
        	
        	//apiquote get
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
            

            String jsonOut = gson.toJson(s);
            out.print(jsonOut);
        } finally {
            out.flush();
            out.close();
        }
    }
	

	

}
