package edu.ucla.cs.cs144;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SuppressWarnings("serial")
public class MakeOrderServlet extends HttpServlet implements Servlet {
   private static final String API_KEY = "AIzaSyDve_kvrQSTQt-Exg3b4Sbiji0LlfwABWk";

   public MakeOrderServlet() {}

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      HttpSession session = request.getSession(true);
      OrderInfo orderInfo = (OrderInfo)session.getAttribute("orderInfo");
      if(orderInfo!=null){
         request.setAttribute("svn",request.getServerName());
         request.setAttribute("svp",request.getServerPort());
         request.setAttribute("name", orderInfo.name);
         request.setAttribute("id", orderInfo.id);
         request.setAttribute("price", orderInfo.buyPrice);
         request.setAttribute("API_KEY", API_KEY);
         request.getRequestDispatcher("/makeOrder.jsp").forward(request, response);
         session.setAttribute("svn",request.getServerName());
         session.setAttribute("svp",request.getServerPort());
   }  
   }
}