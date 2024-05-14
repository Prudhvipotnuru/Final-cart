package controllers;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dal.ProductFactory;
import dal.StoreDAL;
import models.Customer;
import models.Product;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String cemail = req.getParameter("cemail");
		String cpassword = req.getParameter("cpassword");
		StoreDAL d = ProductFactory.getProductsDALImpl();
		Customer c = d.verifyUser(cemail, cpassword);
		System.out.println(c);
		if (c != null) {
			// On successful login, set session attribute and redirect to store.jsp
			HttpSession session = req.getSession();
			session.setAttribute("user", cemail);
			session.setAttribute("cid", c.getCid());
			session.setAttribute("cname", c.getCname());
			session.setAttribute("pages", 1);
			HashMap<Integer, Integer> productCount = new HashMap<>();
			HashMap<Integer, Product> productSet = new HashMap<>();
			session.setAttribute("productCount", productCount);
			session.setAttribute("productSet", productSet);
			res.sendRedirect("store.jsp");
		} else {
			// If login fails, re-dispatch to login.html with an error message
			RequestDispatcher rd = req.getRequestDispatcher("login.html");
			res.setContentType("text/html");
			res.getWriter().write(
					"<div class='error-message'>Your credentials are incorrect. Please check or register.</div>");
			rd.include(req, res); // Include the error message and re-dispatch to login.html
		}
	}

}
