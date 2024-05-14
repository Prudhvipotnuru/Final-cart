package bll;

public class BusinessFactory {
	private static OrderCalculation oc;

	public static OrderCalculation getOrderCalculationImpl() {
		if (oc == null) {
			oc = new OrderCalculationImpl();
		}
		return oc;
	}
}
