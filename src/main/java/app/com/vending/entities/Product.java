package app.com.vending.entities;

import javax.persistence.Column;

/**
 * Products
 * 
 * March 2022
 * 
 * List of products
 *  
 */

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="product")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="product_id")
	private int id;
	
	@Column(name="product_description")
	private String description;

	@Column(name="product_price")
	private int price;
	
	@Column(name="product_quantity")
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
		return String.format("Product [id=%s, description=%s, price=%s, quantityCount=%s]", id, description, price,
				quantityCount);
	}
	
}
