package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dal.ProductFactory;
import models.ProductsList;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/product")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson;

	public ProductServlet() {
		super();
		gson = new Gson();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setHeader("Access-Control-Allow-Origin", "*"); // Allow all origins
		res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		String category = req.getParameter("category");
		String sort = req.getParameter("sort");
		Integer pages = Integer.parseInt(req.getParameter("pages"));
		System.out.println(pages);
		System.out.println(category + sort);
		if ("null".equalsIgnoreCase(category)) {
			category = null;
			System.out.println("Category is null or empty");
		}

		if ("null".equalsIgnoreCase(sort)) {
			sort = null;
		}
		ProductsList pl = ProductFactory.getProductsDALImpl().getProducts(category, sort, pages);
		res.setContentType("application/json");
		res.getWriter().write(gson.toJson(pl));
		// res.sendRedirect("www.google.com");
	}

}
