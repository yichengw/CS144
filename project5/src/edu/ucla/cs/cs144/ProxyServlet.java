package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet implements Servlet {

   public ProxyServlet() {}
   
   private final String USER_AGENT = "Mozilla/5.0";

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      // your codes here
      final String query = request.getParameter("q");
      String urlString = "";
      try{
         // escape characters
         URI uri = new URI(
            "http", 
            "google.com", 
            "/complete/search",
            "output=toolbar&q=" + query,
            null);
         
         urlString = uri.toASCIIString();
      } catch(Exception e){
        
      }
      
      // make a GET request (default type)
		URL obj = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + urlString);
		System.out.println("Response Code : " + responseCode);
 
      // get response
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer result = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			result.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(result.toString());
		
		response.setContentType("text/xml");
		response.getWriter().write(result.toString());
		
   }
}
