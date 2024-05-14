<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="models.ProductsList, models.Product,java.util.*,models.OrderCalculationResult,models.Coupon"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Shopping Cart</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style type="text/css">
* {
	font-style: italic;
	font-weight: bold;
}

.table {
	margin-top: 1rem;
	background: white;
	border-collapse: collapse;
	text-align: left;
}

.table thead {
	background-color: #f8f9fa;
}

.table th, .table td {
	padding: 1rem;
	border: 1px solid #dee2e6;
}

.modal-body, .modal-header {
	background: #20c997;
}
</style>
</head>
<body>
	<h1 class="header text-center py-4">CheckOut Page</h1>
	<div class="container mt-5">
		<%
		HashMap<Integer, Product> productSet = (HashMap<Integer, Product>) session.getAttribute("productSet");
		HashMap<Integer, Integer> productCount = (HashMap<Integer, Integer>) session.getAttribute("productCount");
		OrderCalculationResult ocr = (OrderCalculationResult) session.getAttribute("OrderCalculationResult");
		int oid = (int) session.getAttribute("order_id");
		String cid = ((String) session.getAttribute("customer_name")).toUpperCase();
		int total = (int) session.getAttribute("cart_total");
		%>
		<h3 style="color: green; display: inline;">
			<%=cid%>, Your Order is successful with order id:
		</h3>
		<h3 style="color: green; display: inline;"><%=oid%></h3>

		<h3 style="color: green">
			Total payable amount is:<span id="total"><%=total%></span>
		</h3>

		<!-- Button to trigger modal -->
		<button type="button" class="btn btn-info" data-toggle="modal"
			data-target="#cartModal">View Order items</button>

		<!-- Modal Structure -->
		<div class="modal fade" id="cartModal" tabindex="-1" role="dialog"
			aria-labelledby="cartModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="cartModalLabel">Ordered Items</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<!-- Table with cart items -->
						<table class="table table-hover">
							<thead>
								<tr>
									<th>Product ID</th>
									<th>Product Name</th>
									<th>Price</th>
									<th>Image</th>
									<th>Qty</th>
								</tr>
							</thead>
							<tbody>
								<%
								for (Map.Entry<Integer, Product> product : productSet.entrySet()) {
								%>
								<tr>
									<td><%=product.getValue().getPid()%></td>
									<td><%=product.getValue().getPname()%></td>
									<td><%=product.getValue().getPrice()%></td>
									<td><img src="<%=product.getValue().getImage()%>"
										alt="<%=product.getValue().getPname()%>" width="50"
										height="50" /></td>
									<td><%=productCount.get(product.getKey())%></td>
								</tr>
								<%
								}
								%>
							</tbody>
						</table>
						<div>
							<h4>
								Total Products Price:
								<%=ocr.getTotalProductsPrice()%></h4>
							<h4>
								(Total Inclusive GST):
								<%=ocr.getTotalInclGst()%></h4>
							<h4>
								+Total Shipping Charges:
								<%=ocr.getTotalShipCharge()%></h4>
							<h4>
								+Total Shipping Charges GST:
								<%=ocr.getTotalShipChargeGst()%></h4>
							<h4>
								-Total Discount:
								<%=ocr.getTotalDiscount()%></h4>
							<%
							if (ocr.getCoupon().getDcpn_value() > 0) {
							%>
							<h4>
								-Coupon Discount:
								<%=ocr.getCoupon().getDcpn_value()%></h4>
							<h4>
								Coupon Applied:
								<%=ocr.getCoupon().getDcpn_code()%>(Coupons Remaining::<%=ocr.getCoupon().getDcpn_noc()%>)
							</h4>
							<%}else if(ocr.getCoupon().getDcpn_value()==0){ %>
							<h4>
								Apologies <%=session.getAttribute("cname") %>, Coupon cannot be applied as we're currently out of <%=ocr.getCoupon().getDcpn_code() %> coupons for shopping.
							</h4>
							<%
							} else {
							%>
							<h4>
								Apologies <%=session.getAttribute("cname") %>,
								Coupon cannot be applied , Minimum order value of the
								<%=ocr.getCoupon().getDcpn_code()%>
								is
								<%=ocr.getCoupon().getOrder_minimum_value()%>
							</h4>
							<%
							}
							%>
							<h4 style="font-weight: bolder">
								Final Payable Amount:
								<%= ocr.getOrder_total() %>
						    </h4>
						</div>
					</div>
				</div>
			</div>
		</div>

		<button onclick="createRazorpayOrder()" class="btn btn-danger">Proceed
			to payment</button>
	</div>

	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	<script src="https://checkout.razorpay.com/v1/checkout.js"></script>

	<script>
        function createRazorpayOrder() {
        	var total=$("#total").text();
        console.log(total);
            fetch('order?amount='+total) // Assuming your servlet URL is '/OrderCreation'
            .then(response => response.json())
            .then(data => {
                // Once the order is created, initialize Razorpay checkout with the order details
                var options = {
                    "key": "rzp_test_oylZTllNukvCHh",
                    "amount": data.amount, // Use the amount received from the server
                    "currency": "INR",
                    "name": "Acme Corp",
                    "description": "Test Transaction",
                    "image": "https://example.com/your_logo",
                    "order_id": data.id, // Use the order ID received from the server
                    "handler": function (response) {
                        // Handle the payment success callback if needed
                    },
                    "prefill": {
                        "name": "Gaurav Kumar",
                        "email": "gaurav.kumar@example.com",
                        "contact": "9000090000"
                    },
                    "notes": {
                        "address": "Razorpay Corporate Office"
                    },
                    "theme": {
                        "color": "#20c997"
                    }
                };
                var rzp1 = new Razorpay(options);
                rzp1.open();
            })
            .catch(error => {
                console.error('Error creating Razorpay order:', error);
            });
        }

        // Function to handle the payment response from Razorpay
        function handleRazorpayResponse(response) {
            // Send the payment details to the server for verification
            fetch('OrderCreation', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(response)
            })
            .then(response => response.text())
            .then(data => {
                console.log('Payment verification response:', data);
                // Handle the response from the server as needed
            })
            .catch(error => {
                console.error('Error verifying payment:', error);
            });
        }
    </script>
</body>
</html>
