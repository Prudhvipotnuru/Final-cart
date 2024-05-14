package models;

public class OrderCalculationResult {
	private int totalInclGst;
	private int totalShipCharge;
	private int totalShipChargeGst;
	private int totalDiscount;
	private int totalProductsPrice;
	private int order_total;
	private Coupon coupon;
	public OrderCalculationResult(int totalInclGst, int totalShipCharge, int totalShipChargeGst, int totalDiscount,
			int totalProductsPrice, int order_total, Coupon c) {
		this.totalInclGst = totalInclGst;
		this.totalShipCharge = totalShipCharge;
		this.totalShipChargeGst = totalShipChargeGst;
		this.totalDiscount = totalDiscount;
		this.totalProductsPrice = totalProductsPrice;
		this.order_total = order_total;
		this.coupon=c;
	}

	public Coupon getCoupon() {
		return coupon;
	}


	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}


	public int getOrder_total() {
		return order_total;
	}

	public void setOrder_total(int order_total) {
		this.order_total = order_total;
	}

	public int getTotalInclGst() {
		return totalInclGst;
	}

	public void setTotalInclGst(int totalInclGst) {
		this.totalInclGst = totalInclGst;
	}

	public int getTotalShipCharge() {
		return totalShipCharge;
	}

	public void setTotalShipCharge(int totalShipCharge) {
		this.totalShipCharge = totalShipCharge;
	}

	public int getTotalShipChargeGst() {
		return totalShipChargeGst;
	}

	public void setTotalShipChargeGst(int totalShipChargeGst) {
		this.totalShipChargeGst = totalShipChargeGst;
	}

	public int getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(int totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public int getTotalProductsPrice() {
		return totalProductsPrice;
	}

	public void setTotalProductsPrice(int totalProductsPrice) {
		this.totalProductsPrice = totalProductsPrice;
	}
}
