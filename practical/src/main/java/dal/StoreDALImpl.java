package dal;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import jdbcUtilities.JdbcConnection;
import models.Coupon;
import models.Customer;
import models.Product;
import models.ProductsList;

public class StoreDALImpl implements StoreDAL {

	public static Connection con = JdbcConnection.getConnection();

	public ResultSet rs;
	public PreparedStatement st;
	public String query;

	private boolean status;

	public ProductsList getProducts(String category, String sort, Integer pages) {
		// TODO Auto-generated method stub
		ProductsList pl = null;
		System.out.println(category + sort);
		System.out.println("db:" + pages);
		try {
			query = "SELECT * FROM get_products_pages1(?,?,?);";
			st = con.prepareStatement(query);
			st.setString(1, category);
			st.setString(2, sort);
			st.setInt(3, pages);
			rs = st.executeQuery();
			pl = new ProductsList();
			while (rs.next()) {
				pl.add(new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5),
						rs.getInt(6), rs.getInt(7)));

			}
			for (Product p : pl) {
				System.out.println(p);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return pl;
	}

	public int setOrderDetails(Integer total, Integer cid) {
		// TODO Auto-generated method stub // TODO Auto-generated method stub
		int oid = 0;
		try {
			query = "Insert into os_orders1(ototal,cid) values(?,?) returning oid";
			st = con.prepareStatement(query);
			st.setInt(1, total);
			st.setInt(2, cid);
			rs = st.executeQuery();
			while (rs.next()) {
				oid = rs.getInt("oid");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return oid;
	}
	// a method to get customer id from email
	// public Integer getCid(String email) {
	// Integer x = 0;
	// // TODO Auto-generated method stub
	// try {
	// query = "Select cid from os_customer where cemail= ?";
	// st = con.prepareStatement(query);
	// st.setString(1, email);
	// rs = st.executeQuery();
	// rs.next();
	// x = rs.getInt(1);
	// } catch (Exception e) {
	// System.out.println(e);
	// }
	// return x;
	// }

	// public String getCname(String email) {
	// String x = "";
	// // TODO Auto-generated method stub
	// try {
	// query = "Select cname from os_customer where cemail= ?";
	// st = con.prepareStatement(query);
	// st.setString(1, email);
	// rs = st.executeQuery();
	// rs.next();
	// x = rs.getString(1);
	// } catch (Exception e) {
	// System.out.println(e);
	// }
	// return x;
	// }

	public void setProductDetails(Integer oid, ProductsList p) {
		try {
			query = "insert into os_order_products values(?,?)";
			st = con.prepareStatement(query);
			for (Product pr : p) {
				st.setInt(1, oid);
				st.setInt(2, pr.getPid());
				st.addBatch();
			}
			st.executeBatch();
		} catch (Exception e) {
		}
	}

	public ArrayList<Integer> getgst(ArrayList<Integer> hsn) {
		// TODO Auto-generated method stub
		ArrayList<Integer> gstList = new ArrayList<>(); // Mutable list for results

		try {
			// Convert ArrayList to SQL Array (for PostgreSQL, for example)
			Array sqlArray = con.createArrayOf("INTEGER", hsn.toArray());

			// SQL query with array parameter
			String query = "SELECT getGST(?)"; // Example query
			st = con.prepareStatement(query);
			st.setArray(1, sqlArray); // Set the SQL array

			rs = st.executeQuery();

			// Loop through the ResultSet
			if (rs.next()) {
				// Extract the returned array
				Array gstArray = rs.getArray(1); // Get the array from the first column
				Integer[] gstValues = (Integer[]) gstArray.getArray(); // Convert to Integer array

				for (Integer gst : gstValues) {
					gstList.add(gst); // Add elements to the list
				}

				System.out.println("GST Values: " + gstList); // Display the GST values
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return gstList;
	}

	public Customer verifyUser(String cemail, String cpassword) {
		// boolean ispwd = false;
		Customer c = null;
		try {
			query = "Select * from os_customer where cemail=?";
			st = con.prepareStatement(query);
			st.setString(1, cemail);
			rs = st.executeQuery();
			if (rs.next()) {
				if (cpassword.equals(rs.getString(6))) {
					// ispwd = true;
					c = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
							rs.getString(6));

				}

			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return c;
	}

	public Boolean registerUser(Customer c) {
		query = ("insert into os_customer values(?,?,?,?,?,?);");
		try {

			st = con.prepareStatement(query);
			st.setInt(1, c.getCid());
			st.setString(2, c.getCname());
			st.setString(3, c.getCmobile());
			st.setString(4, c.getCemail());
			st.setString(5, c.getClocation());
			st.setString(6, c.getCpassword());
			status = st.execute();

		} catch (Exception e) {
			System.out.println(e);
		}
		return status;
	}

	public ArrayList<Integer> getUnserviceableProductIds(ArrayList<Integer> pList, int pincode) {
		ArrayList<Integer> UnserviceableProductIds = new ArrayList<Integer>();
		System.out.println("getUnserviceableProductIds" + pList.size());
		try {

			Array sqlArray = con.createArrayOf("INTEGER", pList.toArray());

			String query = "SELECT getUnserviceableProductIds(?,?)";
			st = con.prepareStatement(query);
			st.setArray(1, sqlArray);
			st.setInt(2, pincode);
			rs = st.executeQuery();
			if (rs.next()) {

				Array arr = rs.getArray(1);
				Integer[] ids = (Integer[]) arr.getArray();

				for (Integer id : ids) {
					UnserviceableProductIds.add(id);
				}
			}
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

		return UnserviceableProductIds;
	}

//	@Override
//	public ArrayList<Integer> getPriceList(ArrayList<Integer> keysList, ArrayList<Integer> gst) {
//
//		ArrayList<Integer> priceList = new ArrayList<>();
//
//		try {
//			Array sqlArray = con.createArrayOf("INTEGER", keysList.toArray());
//			Array sqlArray2 = con.createArrayOf("INTEGER", gst.toArray());
//			String query = "SELECT getPrices(?,?)";
//			st = con.prepareStatement(query);
//			st.setArray(1, sqlArray);
//			st.setArray(2, sqlArray2);
//
//			rs = st.executeQuery();
//
//			if (rs.next()) {
//				Array priceArray = rs.getArray(1);
//				Integer[] priceValues = (Integer[]) priceArray.getArray();
//
//				for (Integer price : priceValues) {
//					priceList.add(price);
//				}
//
//				System.out.println("price Values: " + priceList);
//			}
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//
//		return priceList;
//	}

	@Override
	public Coupon getCouponValue(String dcpn_code) {
//		int dcpn_value = 0;
		Coupon c = null;
		try {
			st = con.prepareStatement("select * from os_discount_coupon where dcpn_code=(?);");
			st.setString(1, dcpn_code);
			rs = st.executeQuery();
			if (rs.next()) {
				c=new Coupon(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	public int updateNoOfCoupons(String dcpn_code) {
        String query = "UPDATE os_discount_coupon SET dcpn_noc = dcpn_noc - 1 WHERE dcpn_code = ? RETURNING dcpn_noc";  // Update query with RETURNING clause
        PreparedStatement st = null;
        ResultSet rs = null;  // Result set to capture the returned value
        int updatedValue = -1;  // Default value for error cases

        try {
            st = con.prepareStatement(query);  // Create the prepared statement
            st.setString(1, dcpn_code);        // Set the parameter for dcpn_code
            rs = st.executeQuery();            // Execute the query and get the result set

            if (rs.next()) {  // Check if there's a returned result
                updatedValue = rs.getInt(1);  // Get the returned dcpn_noc value
            }

        } catch (SQLException e) {           // Handle SQL exceptions
            e.printStackTrace();             // Print the stack trace for debugging
        } finally {
            // Clean up the resources to avoid memory leaks
            if (rs != null) {
                try {
                    rs.close();             // Close the result set
                } catch (SQLException e) {
                    e.printStackTrace();    // Handle exceptions during cleanup
                }
            }
            if (st != null) {
                try {
                    st.close();             // Close the prepared statement
                } catch (SQLException e) {
                    e.printStackTrace();    // Handle exceptions during cleanup
                }
            }
        }

        return updatedValue;  // Return the updated coupon count
    }

	@Override
	public void updateStock(HashMap<Integer, Integer> productCount) {
		try {
			st=con.prepareStatement("update os_product_stock set pstock=pstock-? where pid=?");
			for(Entry<Integer,Integer> a:productCount.entrySet()) {
				st.setInt(1, a.getValue());
				st.setInt(2, a.getKey());
				st.addBatch();
			}
			st.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
