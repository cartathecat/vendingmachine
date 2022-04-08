package app.com.vending.controller;

/**
 * VendingMachineController
 * 
 * March 2022
 * 
 * Controller to accept requests
 * Refactored to move throws into service layer
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import app.com.vending.machine.VendingMachine;

@RestController
@RequestMapping(path="vendingmachine/v1")
public class VendingMachineController {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineController.class);
	
	private VendingMachine vendingMachine;	
	@Autowired
	public void SetVendingMachine(VendingMachine v) {
		this.vendingMachine = v;
	}
	
	public VendingMachineController() {
		log.debug("VendingMachineController");
	}
	
	/**
	 * Initialise endpoint
	 * 
	 * @param coins
	 *     List of coins that can be passed in, delimited by a colon (:) for the quantity
	 *     <p>
	 *     TWOPOUND:5,ONEPOUND:10,FIFTY:10,TWENTY:10,TEN:20,FIVE:20,TWO:20,ONE:20
	 *     
	 * @return
	 *     Returns a response to indicate the vending machine is initialised or an error
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/init/{coins}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> initVending(@PathVariable List<String> coins) {
		log.debug("initVending end-point");
		log.debug("Coins: {}", coins);
		this.vendingMachine.Initialise(coins);		
		log.debug("Status of vending machine ...{}", this.vendingMachine.getStatus() );
		return new ResponseEntity<>(this.vendingMachine.GenericResponse("Vending initialised"), HttpStatus.OK);
	}

	/**
	 * Deposit coins endpoint
	 * 
	 * @param coins
	 *     List of coins that can be passed in, delimited by a colon (:) for the quantity
	 *     <p>
	 *     TWOPOUND:5,ONEPOUND:10,FIFTY:10,TWENTY:10,TEN:20,FIVE:20,TWO:20,ONE:20
	 *     
	 * @return
	 *     Returns a response to show deposited money or an error
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deposit/{coins}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> deposit(@PathVariable List<String> coins) {
		log.debug("deposit end-point");
		return new ResponseEntity<>(this.vendingMachine.DepositAmount(coins), HttpStatus.OK);
	}

	/**
	 * Products endpoint
	 * 
	 * @return
	 *     Returns a response of a list of products
	 */
	@GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> products() {
		log.debug("products end-point");
		return new ResponseEntity<>(this.vendingMachine.Products(), HttpStatus.OK);
	}
	
	/**
	 * Vend endpoint
	 * 
	 * @param id
	 *     Vend id selected
	 *     
	 * @return
	 *     Returns a response to vended item or an error
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/vend/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> vend(@PathVariable int id) {
		log.debug("vend end-point");
		return new ResponseEntity<>(this.vendingMachine.VendItem(id), HttpStatus.OK);
	}

	/**
	 * Refund endpoint
	 * 
	 * @return
	 *     Returns a response for a refund or an error
	 */
	@GetMapping(value = "/refund", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> refund() {
		log.debug("refund end-point");
		return new ResponseEntity<>(this.vendingMachine.IssueRefund(), HttpStatus.OK);
	}

	/**
	 * Floatvalue endpoint
	 * 
	 * @return
	 *     Returns a response to show the float value or an error
	 */
	@GetMapping(value = "/floatvalue", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> floatValue() {
		log.debug("floatvalue end-point");
		return new ResponseEntity<>(this.vendingMachine.GetFloat(), HttpStatus.OK);
	}

	/**
	 * Coinbucket endpoint
	 * 
	 * @return
	 *     Returns a response to show the number of coins in the coin bucket or an error
	 */
	@GetMapping(value = "/coinbucket", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> coinBucket1() {
		log.debug("coinBucket end-point");
		return new ResponseEntity<>(this.vendingMachine.GetCoinBucketByString(), HttpStatus.OK);
	}
	
	/**
	 * Status endpoint
	 * 
	 * @return
	 *     Returns a response to indicate the vending machine status or an error
	 *     <p>
	 *     INACTIVE            - Vending machine is inactive and not yet initialised
	 *     <p>
	 *     READY_TO_VEND       - Vending machine is ready to vend a product
	 *     <p>
     *     VENDING_IN_PROGRESS - Vending is in progress \n
	 *     
	 */
	@GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> getStatus() {
		log.debug("status end-point");
		return new ResponseEntity<>(this.vendingMachine.GetVendingStatus(), HttpStatus.OK);
	}
	
}
