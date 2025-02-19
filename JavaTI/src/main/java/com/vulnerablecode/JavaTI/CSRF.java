package com.vulnerablecode.JavaTI;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/transfer")
public class CSRF extends HttpServlet {
	

	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        // Get parameters from request
	        String fromAccount = request.getParameter("fromAccount");
	        String toAccount = request.getParameter("toAccount");
	        double amount = Double.parseDouble(request.getParameter("amount"));
	        // Transfer money
	        Bank.transfer(fromAccount, toAccount, amount);
	        response.sendRedirect("/success");
	    }
	}
}
