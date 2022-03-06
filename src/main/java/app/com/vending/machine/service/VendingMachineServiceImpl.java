package app.com.vending.machine.service;

/**
 * VedingMachineService implementation
 *
 * Implement a vending machine service
 * 
 * March 2022
 *  
 */

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import app.com.vending.entities.ChangeReturnResponse;
import app.com.vending.entities.Product;
import app.com.vending.entities.VendResponse;
import app.com.vending.entities.VendResponse.VENDTYPE;
import app.com.vending.machine.repository.Repository;

public class VendingMachineServiceImpl implements VendingMachineService {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineServiceImpl.class);
			
	@Autowired
	public Repository repository;

	@Autowired
	private VendingMachineFloat vendingMachineFloat;

	@Autowired
	private VendingMachineDeposit vendingMachineDeposit;

	@Autowired
	private VendingMachineChange vendingMachineChange;

	@Autowired
	public VendingMachineCoinBucket vendingMachineCoinBucket;
	
	public VendingMachineFloat VendingMachineFloat() {
		return this.vendingMachineFloat;
	}
	
	public VendingMachineDeposit VendingMachineDeposit() {
		return this.vendingMachineDeposit;
	}

	public VendingMachineChange VendingMachineChange() {
		return this.vendingMachineChange;
	}

	public VendingMachineCoinBucket VendingMachineCoinBucket() {
		return this.vendingMachineCoinBucket;
	}

	public VendingMachineServiceImpl() {
		log.debug("VendingMachineImpl");
	}
	
	/**
	 * Initialise - setting float and load the products
	 */
	public void Initialise(int floatValue) {
		log.debug("VendingMachineImpl - Initialise");
		this.vendingMachineFloat.SetFloatValue(floatValue);
		repository.LoadProducts();		
	}
	
	/**
	 * Return a list of products
	 * @return
	 */
	public List<?> GetProducts() {
		return (repository.GetProducts());
	}

	/**
	 * Vend an item 
	 * 
	 */
	public VendResponse VendItem(int id) {

		Product p = repository.GetProduct(id);		
		if (p == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product code %s not found", id));
		}
		if (p.getQuantityCount() == 0) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Product code %s has sold out",id));
		}
		if (this.vendingMachineDeposit.getDepositValue() < p.getPrice()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Insufficient funds to purchase product code %s",id));
		}

		// Do we have the coins for the change ?
		List<ChangeReturnResponse> crList = this.vendingMachineCoinBucket.CheckCoinsForChange(this.vendingMachineDeposit.getDepositValue() - p.getPrice());
		if (crList.size() == 0 && (this.vendingMachineDeposit.getDepositValue() - p.getPrice() > 0)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Not enough coins to return change for product %s",id));
		}

		// Remove coins from the coin bucket
		RemoveCoins(crList);
		
		// Update stock for item
		int qty = p.getQuantityCount();
		p.setQuantityCount(qty - 1);
		log.debug("Quantity amended ...");
	
		// Calculate any change
		this.vendingMachineChange.setChangeValue(this.vendingMachineDeposit.getDepositValue() - p.getPrice());
		log.debug("Change is {}", vendingMachineChange.getChangeValue());
		
		this.vendingMachineFloat.SetFloatValue(p.getPrice());
		
		log.debug("deposit    is {}", this.vendingMachineDeposit.getDepositValue());
		log.debug("floatValue is {}", this.vendingMachineFloat.getFloatValue());

		return (new VendResponse(p, this.vendingMachineChange.getChangeValue(), VENDTYPE.PRODUCT, crList));
	
	}

	/**
	 * Remove coins from the coin bucket
	 * 
	 * @param crList
	 */
	public void RemoveCoins(List<ChangeReturnResponse> crList) {
		for (ChangeReturnResponse r : crList) {
			log.debug("Coin   : ()", r.getCoin());
			log.debug("Qty    : ()", r.getCoinQuantity());
			this.vendingMachineCoinBucket.RemoveCoinFromCoinBucket(r.getCoin(), r.getCoinQuantity());
		}	
	}

	/**
	 * Give a refund
	 * 
	 * @return
	 */
	public VendResponse IssueRefund() {

		// Do we have the coins for the change ?
		List<ChangeReturnResponse> crList = this.vendingMachineCoinBucket.CheckCoinsForChange(this.vendingMachineDeposit.getDepositValue());
		if (crList.size() == 0 && (this.vendingMachineDeposit.getDepositValue() > 0)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Not enough coins to return change for refund"));
		}

		// Build a response
		VendResponse v = new VendResponse(null, this.VendingMachineDeposit().getDepositValue(), VENDTYPE.REFUND, crList);

		// Remove coins from the coin bucket and reset deposited amount
		RemoveCoins(crList);

		return v;
	}
	
}
