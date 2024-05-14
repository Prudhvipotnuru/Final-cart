package bll;

import java.util.HashMap;

import models.Coupon;
import models.OrderCalculationResult;
import models.Product;

public interface OrderCalculation {
	public OrderCalculationResult getTotal(HashMap<Integer, Integer> productCount, HashMap<Integer, Product> productSet,
			Coupon c);
}
