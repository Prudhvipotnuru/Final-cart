package dal;

public class ProductFactory {
	private static StoreDAL d = null;

	private ProductFactory() {

	}

	public static StoreDAL getProductsDALImpl() {
		if (d == null) {
			d = new StoreDALImpl();
		}
		return d;
	}

}
