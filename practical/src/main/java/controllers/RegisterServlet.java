package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dal.ProductFactory;
import models.Customer;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		int cid = (int) (Math.random() * (99999 - 10000) * 10000);
		String cname = req.getParameter("cname");
		String cmobile = req.getParameter("cmobile");
		String cemail = req.getParameter("cemail");
		String clocation = req.getParameter("clocation");
		String cpassword = req.getParameter("cpassword");
		Customer c = new Customer(cid, cname, cmobile, cemail, clocation, cpassword);
		Boolean status = ProductFactory.getProductsDALImpl().registerUser(c);
		System.out.println("register called");
		if (status) {
			res.sendRedirect("login.html");
		} else {
			res.sendRedirect("register.html");
		}
	}

}
