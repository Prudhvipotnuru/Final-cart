package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import dal.ProductFactory;
import dal.StoreDAL;
import models.Product;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson;

	public CartServlet() {
		gson = new Gson();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		BufferedReader reader = req.getReader();
		StringBuilder jsonBody = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			jsonBody.append(line);
		}
		Product product = gson.fromJson(jsonBody.toString(), Product.class);
		int pid = product.getPid();

		HashMap<Integer, Product> productSet;
		HttpSession session = req.getSession(false); // Create session if it doesn't exist
		productSet = (HashMap<Integer, Product>) session.getAttribute("productSet");
		HashMap<Integer, Integer> productCount = (HashMap<Integer, Integer>) session.getAttribute("productCount");
		;
		productSet.put(product.getPid(), product);
		System.out.println(productSet.size());
		Integer curcount = productCount.get(pid);
		if (productCount.containsKey(pid)) {
			productCount.put(pid, curcount + 1);
		} else {
			productCount.put(pid, 1);
		}

		session.setAttribute("count", 1);

		session.setAttribute("productSet", productSet);
		session.setAttribute("productCount", productCount);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer pid = Integer.parseInt(req.getParameter("pid"));
		String action = req.getParameter("action");
		HttpSession session = req.getSession(false);
		HashMap<Integer, Integer> productCount = (HashMap<Integer, Integer>) session.getAttribute("productCount");
		Integer curcount = productCount.get(pid);
		if (action.equals("add")) {
			System.out.println("add is called");
			session.setAttribute("count", (Integer) session.getAttribute("count") + 1);
			productCount.put(pid, curcount + 1);
		} else if (action.equals("sub")) {
			productCount.put(pid, curcount - 1);
			System.out.println("sub is called");
			session.setAttribute("count", (Integer) session.getAttribute("count") - 1);
		}
		session.setAttribute("productCount", productCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAL pd = ProductFactory.getProductsDALImpl();

		HttpSession session = req.getSession(false); // Create session if it doesn't exist
		HashMap<Integer, Product> productSet = (HashMap<Integer, Product>) session.getAttribute("productSet");
		HashMap<Integer, Integer> productCount = (HashMap<Integer, Integer>) session.getAttribute("productCount");

		ArrayList<Integer> pList = new ArrayList<>(productSet.keySet());
		// getting pincode from user
		int pincode = Integer.parseInt(req.getParameter("pincode"));
		ArrayList<Integer> unserviceableProductsIds = pd.getUnserviceableProductIds(pList, pincode);

		ArrayList<String> unserviceableProductNames = new ArrayList<String>();
		for (int id : unserviceableProductsIds) {
			unserviceableProductNames.add(productSet.get(id).getPname());
			productSet.remove(id);
		}
		session.setAttribute("productSet", productSet);
		Gson gson = new Gson();
		String json = gson.toJson(unserviceableProductNames);
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(json);
	}

}
