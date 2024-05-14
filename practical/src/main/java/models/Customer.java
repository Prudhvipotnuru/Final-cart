package models;

public class Customer {
	@Override
	public String toString() {
		return "Customer [cid=" + cid + ", cname=" + cname + ", cemail=" + cemail + ", cmobile=" + cmobile
				+ ", clocation=" + clocation + ", cpassword=" + cpassword + "]";
	}

	int cid;
	String cname;
	String cemail;
	String cmobile;
	String clocation;
	String cpassword;

	public Customer(int cid, String cname, String cemail, String cmobile, String clocation, String cpassword) {
		super();
		this.cid = cid;
		this.cname = cname;
		this.cmobile = cmobile;
		this.cemail = cemail;
		this.clocation = clocation;
		this.cpassword = cpassword;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCmobile() {
		return cmobile;
	}

	public void setCmobile(String cmobile) {
		this.cmobile = cmobile;
	}

	public String getClocation() {
		return clocation;
	}

	public void setClocation(String clocation) {
		this.clocation = clocation;
	}

	public String getCpassword() {
		return cpassword;
	}

	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}
}
