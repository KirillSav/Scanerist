package amber.server;

public class Product {
	 int product_id;
	 String barCode;
	 String productName;
	 String productType;
	 int amount;
	 String datetime;
	 double price;
	 String description;
	
	
	public Product(int product_id, String barCode, String productName, String productType, int amount,
			String datetime, double price, String description) {
		super();
		this.product_id = product_id;
		this.barCode = barCode;
		this.productName = productName;
		this.productType = productType;
		this.amount = amount;
		this.datetime = datetime;
		this.price = price;
		this.description = description;
	}
	
	public Product(){
		super();
	}

	@Override
	public String toString() {
		return "Product [product_id=" + product_id + ", barCode=" + barCode + ", productName=" + productName
				+ ", productType=" + productType + ", amount=" + amount + ", datetime=" + datetime + ", price=" + price
				+ ", description=" + description + "]";
	}
	
	
	
}


