<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="models.ProductsList, models.Product,java.util.*" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>Shopping Cart</title>
    <!-- Include some basic styling -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
    *{
     font-style:italic;
     font-weight:bold;
    }
    .qty {
      height: 40px; /* Set height */
      width: 120px; /* Increase width for better spacing */
      display: flex; /* Use flexbox for alignment */
      justify-content: space-between; /* Distribute space evenly between elements */
      align-items: center; /* Center items vertically */
      border-radius: 10px; /* Smooth corners */
      padding: 5px; /* Add padding for inner space */
      transition: all 0.3s; /* Smooth transitions for hover effects */
    }

    .incdec {
      background: #4CAF50; /* Green background for increment/decrement buttons */
      padding: 5px 10px; /* Padding for spacing */
      border-radius: 5px; /* Slightly rounded corners */
      font-size: 1rem; /* Font size for readability */
      font-weight: 700; /* Bold text */
      color: white; /* White text color for contrast */
      transition: all 0.3s; /* Smooth transitions for hover effects */
    }

    .incdec:hover {
      background: #66bb6a; /* Lighter green on hover */
      box-shadow: 0 0 5px rgba(0, 0, 0, 0.2); /* Light shadow effect */
      cursor: pointer; /* Change cursor to pointer on hover */
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

    .table th,
    .table td {
        padding: 1rem;
        border: 1px solid #dee2e6;
    }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1 class="header text-center py-4">Your Shopping Cart</h1>
        
        <%
            HashMap<Integer,Product> productSet = (HashMap<Integer,Product>) session.getAttribute("productSet");
            HashMap<Integer, Integer> productCount =(HashMap<Integer, Integer>) session.getAttribute("productCount");
            if (productSet == null || productSet.isEmpty()) {
                out.println("<p>Your cart is empty.</p>");
            } else {
        %>
            <table class="table">
                <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Image</th>
                        <th class="th">Quantity</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Map.Entry< Integer,Product> product : productSet.entrySet()) {
                    %>
                        <tr>
                            <td><%= product.getValue().getPid() %></td>
                            <td><%= product.getValue().getPname() %></td>
                            <td><%= product.getValue().getPrice() %></td>
                            <td><img src="<%= product.getValue().getImage() %>" alt="<%= product.getValue().getPname() %>" width="50" height="50"/></td>
                            <td>
                            <div class="qty">
                                <div><p id="add" class="incdec" onclick="add(<%= product.getKey() %>)">+</p></div>
                                
                                <div><p id="count"><%=productCount.get(product.getKey()) %></p></div>
                                <div><p id="sub" class="incdec" onclick="sub(<%= product.getKey() %>)">-</p></div>
                            </div>
                            </td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        <%
            }
        %>
        
        <!-- Add pincode input field -->
        <div class="form-group">
            <label for="pincode">Enter Pincode:</label>
            <input type="text" class="form-control" id="pincode" name="pincode">
        </div>
        <div>
            <h2>Select a Discount Coupon</h2>

        <!-- Bootstrap dropdown (select) -->
        <div class="form-group">
            <label for="couponSelect">Choose a coupon:</label>
            <select class="form-control" id="couponSelect" name="coupon">
                <option value="DISCOUNT10">DISCOUNT10</option>
                <option value="DISCOUNT20">DISCOUNT20</option>
                <option value="SAVE15">SAVE15</option>
                <option value="BLACKFRIDAY">BLACKFRIDAY</option>
                <option value="SUMMERSALE">SUMMERSALE</option>
                <option value="WINTERSALE">WINTERSALE</option>
                <option value="CYBERMONDAY">CYBERMONDAY</option>
                <option value="NEWUSER">NEWUSER</option>
                <option value="WELCOME">WELCOME</option>
                <option value="VIPCUSTOMER">VIPCUSTOMER</option>
            </select>
        </div>

     
        </div>
        
        <!-- Add more UI elements like buttons to proceed to checkout -->
        <button id="btn" class="btn btn-info" >Proceed to Checkout</button>
    </div>
     <div class="modal fade" id="unavailableProductsModal" tabindex="-1" aria-labelledby="unavailableProductsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="unavailableProductsModalLabel">These products are not available.please check once</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <ul id="unavailableProductsList" class="list-group">
                    <!-- Unavailable products will be appended here -->
                </ul>
            </div>
            <div class="modal-footer">
                <button  id="close" type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


    <!-- Include Bootstrap JS for styling -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
    $("#btn").click(function () {
        var pincode = $("#pincode").val();

        fetch("cart?pincode=" + pincode)
            .then((r) => {
                return r.json(); // Parse the response as JSON
            })
            .then((unavailableProducts) => {
                if (unavailableProducts.length > 0) {
                    // Clear the previous content
                    $("#unavailableProductsList").empty();

                    // Construct the list of unavailable products
                    unavailableProducts.forEach((productName) => {
                        $("#unavailableProductsList").append("<li class='list-group-item'>" + productName + "</li>");
                    });

                    // Show the modal
                    $("#unavailableProductsModal").modal("show");
                }
                else{
                	var dcpn_code = $("#couponSelect").val();
                	fetch("checkout?dcpn_code="+dcpn_code);
                	window.location.href = "checkout.jsp";
                }
                
              
            })
            .catch((error) => {
                console.error("Fetch error:", error); // Log error
                alert("An error occurred. Please try again later."); // Inform the user
            });
    });

    $("#close").click(function(){
    	 
    	 window.location.href = "cart.jsp";
    })
    function add(pid){
        var url = "cart?action=add&pid="+pid; // The endpoint URL
        
        fetch(url, {
            method: "PUT", // Use POST method for creating/updating data
        })
        .catch((error) => {
            console.error("Fetch error:", error);
            // Consider adding user-friendly error handling here
        });
        console.log("add is called");
        window.location.href="cart.jsp";
        
      }

      function sub(pid){
        var url = "cart?action=sub&pid="+pid; // The endpoint URL
        
        fetch(url, {
            method: "PUT", // Use POST method for creating/updating data
            
        })
        .catch((error) => {
            console.error("Fetch error:", error);
            // Consider adding user-friendly error handling here
        });
        window.location.href="cart.jsp";
        console.log("sub is called");
      }
    </script>
</body>
</html>
