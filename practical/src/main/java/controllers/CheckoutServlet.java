package controllers;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bll.BusinessFactory;
import bll.OrderCalculation;
import dal.ProductFactory;
import dal.StoreDAL;
import models.Coupon;
import models.OrderCalculationResult;
import models.Product;
import models.ProductsList;

/**
 * Servlet implementation class CheckoutServlet
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckoutServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession hs = req.getSession(false);
		HashMap<Integer, Integer> productCount = (HashMap<Integer, Integer>) hs.getAttribute("productCount");
		HashMap<Integer, Product> productSet = (HashMap<Integer, Product>) hs.getAttribute("productSet");
		StoreDAL pd = ProductFactory.getProductsDALImpl();
		OrderCalculation oc = BusinessFactory.getOrderCalculationImpl();
		String dcpn_code = req.getParameter("dcpn_code");
//		int dcpn_value = 0;
		int dcpn_noc=0;
		Coupon c = null;
		if (!dcpn_code.equals(null)) {
            c=pd.getCouponValue(dcpn_code);
//			dcpn_value = c.getDcpn_value();
//			System.out.println("value:" + dcpn_value);
            
            dcpn_noc=pd.updateNoOfCoupons(dcpn_code);
			c.setDcpn_noc(dcpn_noc);

		}
//		System.out.println("value:" + dcpn_value);

		String email = (String) hs.getAttribute("user");
		OrderCalculationResult ocr = oc.getTotal(productCount, productSet, c);
		hs.setAttribute("OrderCalculationResult", ocr);
		ProductsList products = new ProductsList();
		products.addAll(productSet.values());
		Integer cid = (Integer) hs.getAttribute("cid");
		String cname = (String) hs.getAttribute("cname");

		// call to dal layer to set order details
		int oid = pd.setOrderDetails(ocr.getOrder_total(), cid);
		pd.updateStock(productCount);
		// call to dal layer to set order_products details
		pd.setProductDetails(oid, products);
		// setting variables in session
		hs.setAttribute("cart_total", ocr.getOrder_total());
		hs.setAttribute("order_id", oid);
		hs.setAttribute("customer_name", cname);
	}
}
