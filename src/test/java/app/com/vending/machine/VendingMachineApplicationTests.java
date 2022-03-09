package app.com.vending.machine;

/**
 * Tests for each endpoint 
 * // https://docs.spring.io/spring-boot/docs/2.1.18.RELEASE/reference/html/boot-features-testing.html

 * NOTE
 * Each test is defined as testA, testB, testC etc ... as the @Order annotation doesn't seem to be working
 * 
 * 
 */

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.com.vending.entities.VendResponse;
import org.junit.Assert;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMetrics
class VendingMachineApplicationTests {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Try to depsit money before initialised
	 * 
	 * @throws Exception
	 */
	@Test
	public void testA() throws Exception {
		String endpoint = "/vendingmachine/v1/deposit/ONEPOUND:1";
		this.mvc.perform(post(endpoint))
		.andExpect(status()
		.is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
		.andExpect(status().reason(containsString("Vending machine is not yet initialised")));
	
	}
	
	/**
	 * Initialise, check coinbucket, floatvalue
	 * 
	 * @throws Exception
	 */
	@Test
	public void testB() throws Exception {		
		String endpoint = "/vendingmachine/v1/init/TWOPOUND:5,ONEPOUND:10,FIFTY:10,TWENTY:10,TEN:20,FIVE:20,TWO:20,ONE:20";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.string("{\"message\":\"Vending initialised\"}"));
		
		endpoint = "/vendingmachine/v1/coinbucket";
		this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"ONE\":20,\"TWO\":20,\"FIVE\":20,\"TEN\":20,\"TWENTY\":10,\"FIFTY\":10,\"ONEPOUND\":10,\"TWOPOUND\":5}"));

		endpoint = "/vendingmachine/v1/floatvalue";
		this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"floatValue\":3060,\"depositValue\":0}"));
		
		
	}

	/**
	 * Initialise with no parameter, will response NOT_FOUND
	 * 
	 * @throws Exception
	 */
	@Test
	public void testC() throws Exception {
		String endpoint = "/vendingmachine/v1/init";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.is(HttpStatus.NOT_FOUND.value()))
			.andDo(MockMvcResultHandlers.print());
	}

	/**
	 * Coin not valid
	 * 
	 * @throws Exception
	 */
	@Test
	public void testD() throws Exception {
		String endpoint = "/vendingmachine/v1/init/NOTVALID:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.is(HttpStatus.BAD_REQUEST.value()))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
			.andExpect(status().reason(containsString("Coin NOTVALID is not valid")));

	}

	/**
	 * ReInitialise will response with an error
	 * @throws Exception
	 */
	@Test
	public void testE() throws Exception {
		String endpoint = "/vendingmachine/v1/init/ONEPOUND:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
			.andExpect(status().reason(containsString("Vending machine has already been initialized")));
		
	}

	/**
	 * Deposit money
	 * @throws Exception
	 */
	@Test
	public void testF() throws Exception {
		String endpoint = "/vendingmachine/v1/deposit/ONEPOUND:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()			
			.json("{\"deposit\":100,\"message\":\"Deposit\"}"));
		
		endpoint = "/vendingmachine/v1/deposit/ONEPOUND:1,FIFTY:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"deposit\":250,\"message\":\"Deposit\"}"));

		endpoint = "/vendingmachine/v1/floatvalue";
		this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"floatValue\":3060,\"depositValue\":250}"));
		
		endpoint = "/vendingmachine/v1/coinbucket";
		this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"ONE\":20,\"TWO\":20,\"FIVE\":20,\"TEN\":20,\"TWENTY\":10,\"FIFTY\":11,\"ONEPOUND\":12,\"TWOPOUND\":5}"));
		
	}

	/**
	 * ReInitialise will response with an error
	 * @throws Exception
	 */
	@Test
	public void testG() throws Exception {
		String endpoint = "/vendingmachine/v1/init/ONEPOUND:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
			.andExpect(status().reason(containsString("Vending machine is in vending status")));
		
	}

	/*
	 * Vend item 1 ...
	 */
	@Test
	public void testH() throws Exception {

		String endpoint = "/vendingmachine/v1/products";	
		this.mvc.perform(get(endpoint))
		.andExpect(status()
		.isOk())
		.andDo(MockMvcResultHandlers.print());

		String actualProduct = "Cheese and Onion Crisps";	
		endpoint = "/vendingmachine/v1/vend/1";		
		MvcResult result = this.mvc.perform(put(endpoint))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		VendResponse response = objectMapper.readValue(contentAsString, VendResponse.class);
		Assert.assertEquals(response.getProduct().getDescription(), actualProduct);
	}	

	
	@Test
	public void testI() throws Exception {
		String endpoint = "/vendingmachine/v1/deposit/ONEPOUND:3";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()			
			.json("{\"deposit\":300,\"message\":\"Deposit\"}"));
		
		endpoint = "/vendingmachine/v1/deposit/ONEPOUND:1,FIFTY:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"deposit\":450,\"message\":\"Deposit\"}"));
	
		String actualProduct = "Coka Cola";	
		endpoint = "/vendingmachine/v1/vend/7";		
		MvcResult result = this.mvc.perform(put(endpoint))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		VendResponse response = objectMapper.readValue(contentAsString, VendResponse.class);
		Assert.assertEquals(response.getProduct().getDescription(), actualProduct);

		endpoint = "/vendingmachine/v1/coinbucket";
		this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"ONE\":20,\"TWO\":20,\"FIVE\":20,\"TEN\":19,\"TWENTY\":10,\"FIFTY\":12,\"ONEPOUND\":14,\"TWOPOUND\":4}"));

		endpoint = "/vendingmachine/v1/floatvalue";
		this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"floatValue\":3350,\"depositValue\":0}"));
		
	}
	
	@Test
	public void testJ() throws Exception {
		
		String endpoint = "/vendingmachine/v1/deposit/ONEPOUND:1,FIFTY:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"deposit\":150,\"message\":\"Deposit\"}"));
		
		endpoint = "/vendingmachine/v1/refund";
		this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"change\":150,\"vendType\": \"REFUND\",\"changeReturn\":[{\"coinQuantity\":1,\"coin\":\"ONEPOUND\"},{\"coinQuantity\":1,\"coin\":\"FIFTY\"}]}"));

	
	}
	
	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testK() throws Exception {
		String endpoint = "/vendingmachine/v1/vend/7";
		this.mvc.perform(put(endpoint))
			.andExpect(status()
			.is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
			.andExpect(status().reason(containsString("Please deposit money")));
	
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testX() throws Exception {
		String endpoint = "/actuator/prometheus";		
		MvcResult result = this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();

		// init
		boolean match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"POST\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/init/{coins}\",} 1.0");
		Assert.assertTrue(match);		

		// products
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/products\",} 1.0");
		Assert.assertTrue(match);		

		// Deposit
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"POST\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/deposit/{coins}\",} 5.0");
		Assert.assertTrue(match);		

		// Refund
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/refund\",} 1.0");
		Assert.assertTrue(match);		

		// Coin bucket
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/coinbucket\",} 3.0");
		Assert.assertTrue(match);		

		// Vend
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"PUT\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/vend/{id}\",} 2.0");
		Assert.assertTrue(match);		

		// Float
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/floatvalue\",} 3.0");
		Assert.assertTrue(match);		

		
		// Deposit error
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"ResponseStatusException\",method=\"POST\",outcome=\"SERVER_ERROR\",status=\"500\",uri=\"/vendingmachine/v1/deposit/{coins}\",} 1.0");
		Assert.assertTrue(match);		
	
		// Init client error
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"ResponseStatusException\",method=\"POST\",outcome=\"CLIENT_ERROR\",status=\"400\",uri=\"/vendingmachine/v1/init/{coins}\",} 1.0");
		Assert.assertTrue(match);		
		
		// Vend error
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"ResponseStatusException\",method=\"PUT\",outcome=\"SERVER_ERROR\",status=\"500\",uri=\"/vendingmachine/v1/vend/{id}\",} 1.0");
		Assert.assertTrue(match);		

		// Vend server error
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"ResponseStatusException\",method=\"POST\",outcome=\"SERVER_ERROR\",status=\"500\",uri=\"/vendingmachine/v1/init/{coins}\",} 2.0");
		Assert.assertTrue(match);		
		
		
	}

}
