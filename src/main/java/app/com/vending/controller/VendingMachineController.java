package app.com.vending.controller;

/**
 * VendingMachineController
 * 
 * March 2022
 * 
 * Controller to accept requests
 *  
 */

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import app.com.vending.entities.CoinsDepositedResponse;
import app.com.vending.entities.VendResponse;
import app.com.vending.machine.VendingMachine;
import app.com.vending.machine.VendingMachine.STATUS;

@RestController
@RequestMapping(path="vendingmachine/v1")
public class VendingMachineController {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineController.class);
	
	@Autowired
	public VendingMachine vendingMachine;

	public VendingMachineController() {
		log.debug("VendingMachineController");
	}
	
	/**
	 * Initialise endpoint
	 * 
	 * @param coins
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/init/{coins}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> initVending(@PathVariable List<String> coins) {
		log.debug("initVending end-point");
		log.debug("Coins: {}", coins);

		// Validate the coins
		this.vendingMachine.ValidateCoins(coins);

		if (this.vendingMachine.GetStatus().equals(STATUS.READY_TO_VEND)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine has already been initialized");
		}
		if (this.vendingMachine.GetStatus().equals(STATUS.VENDING_IN_PROGRESS)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is in vending status");
		}

		// calculate the float value
		this.vendingMachine.Initialise(this.vendingMachine.CalculateFloat(coins));		
		
		log.debug("Status of vending machine ...{}", this.vendingMachine.GetStatus() );
		return new ResponseEntity<>(this.vendingMachine.GenericResponse("Vending initialised"), HttpStatus.OK);
	}

	/**
	 * Deposit coins endpoint
	 * 
	 * @param coins
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deposit/{coins}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> deposit(@PathVariable List<String> coins) {
		log.debug("deposit end-point");

		this.vendingMachine.ValidateCoins(coins);

		if (this.vendingMachine.GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}

		return new ResponseEntity<CoinsDepositedResponse>(this.vendingMachine.DepositAmount(coins), HttpStatus.OK);
	}

	/**
	 * Products endpoint
	 * 
	 * @return
	 */
	@GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> products() {
		log.debug("products end-point");
		
		if (this.vendingMachine.GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}

		return new ResponseEntity<List<?>>(this.vendingMachine.Products(), HttpStatus.OK);
	}
	
	/**
	 * Vend endpoint
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/vend/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> vend(@PathVariable int id) {
		log.debug("vend end-point");

		if (this.vendingMachine.GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		if (!this.vendingMachine.GetStatus().equals(STATUS.VENDING_IN_PROGRESS)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please deposit money");
		}
		
		return new ResponseEntity<VendResponse>(this.vendingMachine.VendItem(id), HttpStatus.OK);
	}

	/**
	 * Refund endpoint
	 * 
	 * @return
	 */
	@GetMapping(value = "/refund", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> refund() {
		log.debug("refund end-point");
		
		if (this.vendingMachine.GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		
		return new ResponseEntity<VendResponse>(this.vendingMachine.IssueRefund(), HttpStatus.OK);
	}

	/**
	 * Floatvalue endpoint
	 * 
	 * @return
	 */
	@GetMapping(value = "/floatvalue", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> floatValue() {
		log.debug("floatvalue end-point");
		if (this.vendingMachine.GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		
		return new ResponseEntity<Object>(this.vendingMachine.GetFloat(), HttpStatus.OK);
	}

	/**
	 * Coinbucket endpoint
	 * 
	 * @return
	 */
	@GetMapping(value = "/coinbucket", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> coinBucket() {
		log.debug("coinBucket end-point");
		if (this.vendingMachine.GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}

		return new ResponseEntity<Object>(this.vendingMachine.GetCoinBucket(), HttpStatus.OK);
	}
	
	/**
	 * Status endpoint
	 * 
	 * @return
	 */
	@GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> getStatus() {
		log.debug("status end-point");
		return new ResponseEntity<Object>(this.vendingMachine.GetVendingStatus(), HttpStatus.OK);
	}
	
}
