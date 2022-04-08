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
import org.junit.jupiter.api.ClassOrderer.OrderAnnotation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
class VendingMachineApplicationTests {

	private MockMvc mvc;
	private ObjectMapper objectMapper;

	@Autowired
	public void SetMockMvc(MockMvc v) {
		this.mvc = v;
	}
	
	@Autowired
	public void SetObjectMapper(ObjectMapper v) {
		this.objectMapper = v;
	}
	
	/**
	 * Try to depsit money before initialised
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(1)
	public void whenVendingMachineIsNotInitialised_depostMoney_givingVendingMachineIsNotItitialisedError() throws Exception {
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
	@Order(2)
	public void whenVendingMachineIsNotInitialised_depositMoney_givingDepositedMoney_andFloatValue() throws Exception {		
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
	@Order(3)
	public void whenGivenAnInvalidEndpoint_tryInitialising_givingEndpointNotFound() throws Exception {
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
	@Order(4)
	public void whenInitialisingVendingMachine_passInvalidCoin_givingInvalidCoin() throws Exception {
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
	@Order(5)
	public void givenAnInitialisedVendingMachine_initalisedVendingMachine_givingVendingMachineAlreadyInitialised() throws Exception {
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
	@Order(6)
	public void givenOnePoundCoinToDeposit_depostCoins_givingCoinDeposited() throws Exception {
		String endpoint = "/vendingmachine/v1/deposit/ONEPOUND:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()			
			.json("{\"deposit\":100,\"message\":\"Deposit\"}"));
		
	}
	
	
	@Test
	@Order(7)
	public void givenOnePoundFiftyCoinsToDeposit_depostCoins_givingCoinDeposited_andFloatValueAndCoinsBucket() throws Exception {
		String endpoint = "/vendingmachine/v1/deposit/ONEPOUND:1,FIFTY:1";
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
	@Order(8)
	public void givenCoinsToInitialise_initialsiedTheVendingMachine_givenMachineIsInVendingStatus() throws Exception {
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
	@Order(9)
	public void givingAVendingMachineWithCoinsDeposited_listAndVendAProduct_givingAListOfProductsAndVendItem() throws Exception {
		String endpoint = "/vendingmachine/v1/products";	
		this.mvc.perform(get(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print());

		endpoint = "/vendingmachine/v1/vend/1";		
		this.mvc.perform(put(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"product\":{\"id\":1,\"description\":\"Cheese and Onion Crisps\",\"price\":150,\"quantityCount\":9},\"change\":100,\"vendType\":\"PRODUCT\",\"changeReturn\":[{\"coinQuantity\":1,\"coin\":\"ONEPOUND\"}]}"));

	}	

	
	@Test
	@Order(10)
	public void givenAnInitialisedVendingMachine_depositCoinsAndVend_givenDepositedAmountAndVendedItem() throws Exception {
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
	
		endpoint = "/vendingmachine/v1/vend/7";		
		this.mvc.perform(put(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"product\":{\"id\":7,\"description\":\"Coka Cola\",\"price\":140,\"quantityCount\":4},\"change\":310,\"vendType\":\"PRODUCT\",\"changeReturn\":[{\"coinQuantity\":1,\"coin\":\"TWOPOUND\"},{\"coinQuantity\":1,\"coin\":\"ONEPOUND\"},{\"coinQuantity\":1,\"coin\":\"TEN\"}]}"));
				
	}
	
	@Test
	@Order(11)
	public void givenAnInitialisedVendingMachine_requestCoinBucketAndFloat_givingCoinBucketAndFLoat() throws Exception {
		String endpoint = "/vendingmachine/v1/coinbucket";
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
	@Order(12)
	public void givenAnInitialisedVendingMachine_depostCoinsForRefund_givingAValidRefund() throws Exception {
		
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
	@Order(13)
	public void givenAnInitialisedVendingMachine_vendAnItemWithNoDepositedCoins_givingPleaseDepositMoney() throws Exception {
		String endpoint = "/vendingmachine/v1/vend/7";
		this.mvc.perform(put(endpoint))
			.andExpect(status()
			.is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
			.andExpect(status().reason(containsString("Please deposit money")));
	
	}

	@Test
	@Order(14)
	public void givenAnInitialisedVendingMachine_vendAnItemWhereThereIsNotEnoughCoinsForRefund_givingNotENoughCoinsError() throws Exception {

		String endpoint = "/vendingmachine/v1/deposit/TEN:1,TWO:1";
		this.mvc.perform(post(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"deposit\":12,\"message\":\"Deposit\"}"));

		endpoint = "/vendingmachine/v1/vend/11";
		this.mvc.perform(put(endpoint))
			.andExpect(status()
			.isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content()
			.json("{\"product\":{\"id\":11,\"description\":\"Chewing Gum\",\"price\":5,\"quantityCount\":5},\"change\":7,\"vendType\":\"PRODUCT\",\"changeReturn\":[{\"coinQuantity\":1,\"coin\":\"FIVE\"},{\"coinQuantity\":1,\"coin\":\"TWO\"}]}"));
				
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(15)
	public void givenPostedHttpRequests_validatePrometheusHttpCounts_givingExpectedHttpCountsPerCallType() throws Exception {
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
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"POST\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/deposit/{coins}\",} 6.0");
		Assert.assertTrue(match);		

		// Refund
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/refund\",} 1.0");
		Assert.assertTrue(match);		

		// Coin bucket
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/coinbucket\",} 3.0");
		Assert.assertTrue(match);		

		// Vend
		match = contentAsString.contains("http_server_requests_seconds_count{exception=\"None\",method=\"PUT\",outcome=\"SUCCESS\",status=\"200\",uri=\"/vendingmachine/v1/vend/{id}\",} 3.0");
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
