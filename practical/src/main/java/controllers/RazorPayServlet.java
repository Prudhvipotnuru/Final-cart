package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

/**
 * 
 * - Servlet implementation class OrderCreation
 */
@WebServlet("/order")
public class RazorPayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * - @see HttpServlet#HttpServlet()
	 */
	public RazorPayServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * - @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println(request.getParameter("amount"));
		int amount = Integer.parseInt(request.getParameter("amount"));
		System.out.println(amount);
		System.out.println("in order requeeest");
		try {
			RazorpayClient razorpay = new RazorpayClient("rzp_test_oylZTllNukvCHh", "h5C4sfnvqOrfmfqkYetKIwtS");
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount * 100); // Amount in paise
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "receipt#1");
			JSONObject notes = new JSONObject();
			notes.put("notes_key_1", "Tea, Earl Grey, Hot");
			orderRequest.put("notes", notes);
			Order order = razorpay.Orders.create(orderRequest);
			// Send the order ID back to the client or process it further
			response.getWriter().println(order.toJson());
		} catch (RazorpayException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * - @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			RazorpayClient razorpay = new RazorpayClient("rzp_test_oylZTllNukvCHh", "h5C4sfnvqOrfmfqkYetKIwtS");
			String secret = "EnLs21M47BllR3X8PSFtjtbd";

			// Extract the parameters from the request
			String razorpayOrderId = request.getParameter("razorpay_order_id");
			String razorpayPaymentId = request.getParameter("razorpay_payment_id");
			String razorpaySignature = request.getParameter("razorpay_signature");

			// Prepare the JSON object with the received parameters
			JSONObject options = new JSONObject();
			options.put("razorpay_order_id", razorpayOrderId);
			options.put("razorpay_payment_id", razorpayPaymentId);
			options.put("razorpay_signature", razorpaySignature);

			// Verify the payment signature
			boolean status = Utils.verifyPaymentSignature(options, secret);

			// Process the payment status based on 'status' variable
			if (status) {
				// Payment is successful
				response.getWriter().println("Payment successful");
				// You can perform further actions like updating the order status in your database, sending confirmation
				// emails, etc.
			} else {
				// Payment failed or signature verification failed
				response.getWriter().println("Payment failed or signature verification failed");
				// You can log the error or handle it accordingly
			}

		} catch (RazorpayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
