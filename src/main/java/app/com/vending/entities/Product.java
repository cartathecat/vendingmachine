package app.com.vending.entities;

/**
 * Products
 * 
 * March 2022
 * 
 * List of products
 *  
 */

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class Product {

	private int id;
	private String description;
	private int price;
	private int quantityCount;
	
	public Product() {
	}
	
	public Product(int id, String desc, int price, int qty) {
		this.id = id;
		this.description = desc;
		this.price = price;
		this.quantityCount = qty;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantityCount() {
		return this.quantityCount;
	}

	public void setQuantityCount(int qty) {
		this.quantityCount = qty;
	}

	@Override
	public String toString() {
		return "Product [description=" + description + ", price=" + price + ", quantity=" + quantityCount + "]";
	}
	
}
