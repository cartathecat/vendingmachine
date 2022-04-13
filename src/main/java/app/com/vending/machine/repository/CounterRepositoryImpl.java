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

import app.com.vending.entities.Counter;
import app.com.vending.entities.Product;

@Service
@EnableTransactionManagement
public class CounterRepositoryImpl  {

	private final static Logger log = LoggerFactory.getLogger(CounterRepositoryImpl.class);

	@Autowired
	private ICounterRepository counterRepository;
	
	public CounterRepositoryImpl() {
		log.debug("CounterRepository");
	}

	/**
	 * Load products 
	 */
	@Transactional
	@PostConstruct
	public void CreateCounter() {
		log.info("Counter PostConstruct");
		Counter c = new Counter(1, 0);
		counterRepository.save(c);
	}
	
	//@Transactional
	public void UpdateCounter() {
		Counter c = counterRepository.getById(1);
		c.setCount(c.getCount()+1);
		counterRepository.save(c);
	}
	
	@Transactional
	public Counter GetCountFromDB(int id) {
		Counter c = counterRepository.getById(id);
		return c;
	}
	

}
