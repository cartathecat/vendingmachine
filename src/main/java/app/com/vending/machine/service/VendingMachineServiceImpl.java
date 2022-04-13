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

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import app.com.vending.entities.ChangeReturnResponse;
import app.com.vending.entities.CoinConfig;
import app.com.vending.entities.Product;
import app.com.vending.entities.VendResponse;
import app.com.vending.entities.VendResponse.VENDTYPE;
import app.com.vending.machine.repository.CounterRepositoryImpl;
import app.com.vending.machine.repository.ProductRepositoryImpl;

public class VendingMachineServiceImpl implements IVendingMachineService {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineServiceImpl.class);
			
	private ProductRepositoryImpl repository;
	private CounterRepositoryImpl counterRepository;

	private VendingMachineFloat vendingMachineFloat;
	private VendingMachineDeposit vendingMachineDeposit;
	private VendingMachineChange vendingMachineChange;
	private VendingMachineCoinBucket vendingMachineCoinBucket;
	
	private CoinConfig coinConfig;
	
	@Autowired
	public void SetProductRepositoryImpl(ProductRepositoryImpl v) {
		this.repository = v;
	}
	@Autowired
	public void SetCounterRepositoryImpl(CounterRepositoryImpl v) {
		this.counterRepository = v;
	}

	@Autowired
	public void SetVendingMachineFloat(VendingMachineFloat v) {
		this.vendingMachineFloat = v;
	}

	@Autowired
	public void SetVendingMachineDeposit(VendingMachineDeposit v) {
		this.vendingMachineDeposit = v;
	}
	@Autowired
	public void SetVendingMachineChange(VendingMachineChange v) {
		this.vendingMachineChange = v;
	}
	@Autowired
	public void SetVendingMachineCoinBucket(VendingMachineCoinBucket v) {
		this.vendingMachineCoinBucket = v;
	}

	@Autowired
	public void SetCoinConfig(CoinConfig v) {
		this.coinConfig = v;
	}

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
	 * Initialise - set float
	 * Products are loaded from a dstabase in the Repository class, using a method with PostConstruct
	 * 
	 * @param floatValue
	 *     Value of the float value to initialise the vending machine
	 */
	public void Initialise(int floatValue) {
		log.debug("VendingMachineImpl - Initialise");
		this.vendingMachineFloat.setFloatValue(floatValue);
	}
	
	/**
	 * Return a list of products
	 * 
	 * @return
	 *     A list of products
	 */
	public List<?> GetAllProducts() {
		return (repository.GetAllProducts());
	}

	/**
	 * Vend an item 
	 * 
	 * @param id
	 *     Item to vend
	 * @return
	 *     Return a vend item response
	 */
	public VendResponse FindItem(int id) {

		Product p = repository.FindProduct(id);		
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
		List<ChangeReturnResponse> crList = this.vendingMachineCoinBucket.CheckCoinsForChangeByString(this.vendingMachineDeposit.getDepositValue() - p.getPrice());
		if (crList.size() == 0 && (this.vendingMachineDeposit.getDepositValue() - p.getPrice() > 0)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Not enough coins to return change for product %s",id));
		}

		// Remove coins from the coin bucket
		RemoveCoins(crList);
		
		// Update stock for item
		int qty = p.getQuantityCount();
		p.setQuantityCount(qty - 1);
		System.out.println("Updating qty ...");
		UpdateDB(p);		
		
		log.debug("Quantity amended ...");
	
		// Calculate any change
		this.vendingMachineChange.setChangeValue(this.vendingMachineDeposit.getDepositValue() - p.getPrice());
		log.debug("Change is {}", vendingMachineChange.getChangeValue());
		
		this.vendingMachineFloat.setFloatValue(p.getPrice());
		
		log.debug("deposit    is {}", this.vendingMachineDeposit.getDepositValue());
		log.debug("floatValue is {}", this.vendingMachineFloat.getFloatValue());

		return (new VendResponse(p, this.vendingMachineChange.getChangeValue(), VENDTYPE.PRODUCT, crList));
	
	}

	@Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
	private void UpdateDB(Product p) {
		repository.UpdateProduct(p);
		counterRepository.UpdateCounter();
		
	}
	/**
	 * Remove coins from the coin bucket
	 * 
	 * @param crList
	 *     List of coins to remove from the coin bucket
	 */
	public void RemoveCoins(List<ChangeReturnResponse> crList) {
		for (ChangeReturnResponse r : crList) {
			log.debug("Coin   : ()", r.getCoin());
			log.debug("Qty    : ()", r.getCoinQuantity());
		//	this.vendingMachineCoinBucket.RemoveCoinFromCoinBucket(r.getCoin(), r.getCoinQuantity());
			this.vendingMachineCoinBucket.RemoveCoinFromCoinBucketByString(r.getCoin().toString(), r.getCoinQuantity());
		}	
	}

	/**
	 * Give a refund
	 * 
	 * @return
	 *     Vend response to a refund
	 */
	public VendResponse IssueRefund() {

		List<ChangeReturnResponse> crList = this.vendingMachineCoinBucket.CheckCoinsForChangeByString(this.vendingMachineDeposit.getDepositValue());

		// Do we have the coins for the change ?
		//List<ChangeReturnResponse> crList = this.vendingMachineCoinBucket.CheckCoinsForChange(this.vendingMachineDeposit.getDepositValue());
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
