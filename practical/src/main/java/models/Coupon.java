package models;

public class Coupon {
	int dcpn_id;
	String dcpn_code;
	int dcpn_value;
	int dcpn_noc;
	int order_minimum_value;
	public int getDcpn_id() {
		return dcpn_id;
	}

	public void setDcpn_id(int dcpn_id) {
		this.dcpn_id = dcpn_id;
	}

	public String getDcpn_code() {
		return dcpn_code;
	}

	public void setDcpn_code(String dcpn_code) {
		this.dcpn_code = dcpn_code;
	}

	public int getDcpn_value() {
		return dcpn_value;
	}

	public void setDcpn_value(int dcpn_value) {
		this.dcpn_value = dcpn_value;
	}

	public int getDcpn_noc() {
		return dcpn_noc;
	}

	public void setDcpn_noc(int dcpn_noc) {
		this.dcpn_noc = dcpn_noc;
	}

	public int getOrder_minimum_value() {
		return order_minimum_value;
	}

	public void setOrder_minimum_value(int order_minimum_value) {
		this.order_minimum_value = order_minimum_value;
	}

	public Coupon(int dcpn_id, String dcpn_code, int dcpn_value, int dcpn_noc, int order_minimum_value) {
		super();
		this.dcpn_id = dcpn_id;
		this.dcpn_code = dcpn_code;
		this.dcpn_value = dcpn_value;
		this.dcpn_noc = dcpn_noc;
		this.order_minimum_value = order_minimum_value;
	}

}
