package app.com.vending.machine.repository;

/**
 * Repository
 * 
 * March 2022
 * 
 * Repository to load products
 * 
 * Should use a datastore 
 *  
 */

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.com.vending.entities.Product;

public class Repository {

	private final static Logger log = LoggerFactory.getLogger(Repository.class);

	private List<Product> products;
	
	public Repository() {
		log.debug("Repository");
	}

	public List<Product> GetProducts() {
		return this.products;
	}

	public void LoadProducts() {
		LoadProductsDetails();
	}
	
	private void LoadProductsDetails() {

		this.products = new ArrayList<Product>();
		
		Product cheeseAndOnionsCrisps = new Product(1, "Cheese and Onion Crisps", 150, 10);
		products.add(cheeseAndOnionsCrisps);

		Product saltAndVinegarCrisps = new Product(2, "Salt and Vinegar Crisps", 150, 10);
		products.add(saltAndVinegarCrisps);

		Product prawnCocktailCrisps = new Product(3, "Prawn Cocktail Crisps", 150, 10);
		products.add(prawnCocktailCrisps);
		
		Product twixBar = new Product(4, "Twix Chocolate Bar", 100, 10);
		products.add(twixBar);

		Product marsBar = new Product(5, "Mars Chocolate Bar", 110, 5);
		products.add(marsBar);

		Product snickersBar = new Product(6, "Snickers Chocolate Bar", 120, 5);
		products.add(snickersBar);

		Product cokaCola = new Product(7, "Coka Cola", 140, 5);
		products.add(cokaCola);

		Product dietCokaCola = new Product(8, "Diet Coka Cola", 140, 5);
		products.add(dietCokaCola);

		Product fanta = new Product(9, "Fanta", 125, 6);
		products.add(fanta);

		Product dietFanta = new Product(10, "Diet Fanta", 125, 6);
		products.add(dietFanta);

		Product gum = new Product(11, "Chewing Gum", 5, 6);
		products.add(gum);

	}
	
	/**
	 * Return a give product (by id) or null of not found
	 * 
	 * @param id
	 * @return
	 */
	public Product GetProduct(int id) {
		Product p = GetProducts().stream()
				.filter(product -> id == product.getId())
				.findAny()
				.orElse(null);
		
		return p;
		
	}
}
