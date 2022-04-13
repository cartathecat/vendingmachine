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
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import app.com.vending.entities.Product;

@Service
@EnableTransactionManagement
public class ProductRepositoryImpl  {

	private final static Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);

	@Autowired
	private IProductRepository productRepository;
	
	private List<Product> products;
	
	public ProductRepositoryImpl() {
		log.debug("ProductRepository");
	}

	public List<Product> GetAllProducts() {
		return this.products;
	}

	/**
	 * Load products 
	 */
	@Transactional
	@PostConstruct
	public void LoadProducts() {
		log.info("Repository PostConstruct");
		LoadProductsDetails();
	}

	/*
	 * Load products ...
	 * 
	 * If there are products on the database, then load them
	 * ... otherwise, create some new products
	 */
	private void LoadProductsDetails() {

		this.products = GetProductsFromDB();
		if (this.products.size() > 0) {
			return;
		}
		
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

		productRepository.saveAll(this.products);

	}
	
	@Transactional
	private List<Product> GetProductsFromDB() {
		List<Product> products = new ArrayList<Product>();
		products = productRepository.findAll();
		return products;
	}

	
	//@Transactional
	public void UpdateProduct(Product p) {
		productRepository.save(p);
	}
	
	@Transactional
	public Product GetProductFromDB(int id) {
		Product p = productRepository.getById(id);
		return p;
	}
	
	/**
	 * Return a give product (by id) or null of not found
	 * 
	 * @param id
	 *     Product id to find
	 * @return
	 *     Return the product or null
	 */
	public Product FindProduct(int id) {
		Product p = GetAllProducts().stream()
				.filter(product -> id == product.getId())
				.findAny()
				.orElse(null);
		
		return p;
		
	}

}
