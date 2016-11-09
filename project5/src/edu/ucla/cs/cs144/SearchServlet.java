package edu.ucla.cs.cs144;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SearchServlet extends HttpServlet implements Servlet {

   public SearchServlet() {}

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
         String query = request.getParameter("q");
         int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
         int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));

      SearchResult[] search_result = null;
      try {


         if (query != null && query != "") {
            search_result = AuctionSearchClient.basicSearch(query, numResultsToSkip, numResultsToReturn+1);
         }
      } catch (final Exception ex) {
         ex.printStackTrace();
      }

      request.setAttribute("search_result", search_result);
      request.setAttribute("numResultsToSkip", numResultsToSkip);
      request.setAttribute("numResultsToReturn", numResultsToReturn);
      request.setAttribute("prev", search_result != null && numResultsToSkip-numResultsToReturn >= 0 && numResultsToReturn != 0);
      request.setAttribute("next", search_result != null && search_result.length == numResultsToReturn+1 && numResultsToReturn != 0);
      request.setAttribute("query", query);
      request.getRequestDispatcher("/search.jsp").forward(request, response);
   }
}