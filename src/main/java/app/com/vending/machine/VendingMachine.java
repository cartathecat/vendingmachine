package app.com.vending.machine;

/**
 * VendingMachine
 * 
 * March 2022
 * 
 * Vending machine object
 * 
 * 24/03 - Amended ComponentScan to Component
 * 
 */

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
//import app.com.vending.entities.Coins.COINVALUE;
import app.com.vending.entities.*;
import app.com.vending.machine.VendingMachine.STATUS;
import app.com.vending.machine.service.VendingMachineServiceImpl;

@Component
public class VendingMachine extends Coins {

	private final static Logger log = LoggerFactory.getLogger(VendingMachine.class);

	public enum STATUS {
		INACTIVE,
		READY_TO_VEND,
		VENDING_IN_PROGRESS
	}
	
	private VendingMachineServiceImpl vendingMachineServiceImpl;
	
	@Autowired
	public void SetVendingMachineServiceImpl(VendingMachineServiceImpl v) {
		this.vendingMachineServiceImpl = v;
	}
	
	private STATUS status;
	
	public STATUS getStatus() {
		return status;
	}
	public void setStatus(STATUS status) {
		this.status = status;
	}
		
	public VendingMachine() {
		log.debug("VendingMachine");
		setStatus(STATUS.INACTIVE);
	}
		
	/**
	 * Initialise the vending machine
	 * 
	 * @param coins
	 *     Coins passed in
	 */
	public void Initialise(List<String> coins) {
		ValidateCoins(coins);
		if (getStatus().equals(STATUS.READY_TO_VEND)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine has already been initialized");
		}
		if (getStatus().equals(STATUS.VENDING_IN_PROGRESS)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is in vending status");
		}
		this.vendingMachineServiceImpl.Initialise(CalculateFloat(coins));
		setStatus(STATUS.READY_TO_VEND);
		log.debug("Products loaded ...");
		
	}
	
	/**
	 * Return a list of products
	 * 
	 * @return
	 *     Return a list of products
	 */
	public List<?> Products() {
		if (getStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		return (this.vendingMachineServiceImpl.GetAllProducts());
	}

	/**
	 * Validate the coins
	 *  
	 * @param coins
	 *     Coins passed in
	 */
	private void ValidateCoins(List<String> coins) {
		for (String c: coins) {
			String[] s = c.split(":");			
			boolean coinValid = CoinValid(s[0]);
			if (!coinValid) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,String.format("Coin %s is not valid",s[0]) );
			}
			log.debug("Coin   : {}", s[0]);
			log.debug("Number : {}", s[1]);
			log.debug("Coin valid for {} ", s[0]);
		}		
	}

	/**
	 * Deposit coins before vending
	 * 
	 * @param coins
	 *     Coins passed in
	 * @return
	 *     Response of the deposited amount
	 */
	public CoinsDepositedResponse DepositAmount(List<String> coins) {				
		ValidateCoins(coins);
		if (getStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		try {
			for (String c: coins) {
				String[] s = c.split(":");
				int qty = Integer.parseInt(s[1]);
				this.vendingMachineServiceImpl.VendingMachineDeposit().setDepositValue(qty * GetCoin(s[0].toUpperCase()).getCoinValue());
				this.vendingMachineServiceImpl.VendingMachineCoinBucket().AddToFloatCoinBucketByString(s[0].toUpperCase(), qty);				
				log.debug("Deposit Coin   : {}", s[0]);
				log.debug("Coin value     : {}", GetCoin(s[0].toUpperCase()).getCoinValue());
				
			}		
			setStatus(STATUS.VENDING_IN_PROGRESS);
			return (new CoinsDepositedResponse(this.vendingMachineServiceImpl.VendingMachineDeposit().getDepositValue(), "Deposit"));
						
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Error parsing coins :%s",coins));
		}
	}

	/**
	 * Calculate the float
	 * 
	 * @param coins
	 *     Coins passed in
	 * @return
	 *     Repsonse of float
	 */
	public int CalculateFloat(List<String> coins) {
		int floatValue = 0;
		try {
			for (String c: coins) {
				String[] s = c.split(":");
				int qty = Integer.parseInt(s[1]);
				floatValue += (qty * GetCoin(s[0].toUpperCase()).getCoinValue());			
				this.vendingMachineServiceImpl.VendingMachineCoinBucket().AddToFloatCoinBucketByString(s[0].toUpperCase(), qty);				
				log.debug("Coin   : {}", s[0]);
				log.debug("Number : {}", s[1]);
				log.debug("Coin {} has a value of {}", s[0], qty * GetCoin(s[0].toUpperCase()).getCoinValue());				
			}
			log.debug("Float value is {}", floatValue );		
			return floatValue;
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Error parsing coins :%s",coins));	
		}
	}

	/**
	 * Generic response
	 * 
	 * @param msg
	 *     Generic message passed in
	 * @return
	 *     Generic response
	 */
	public GenericResponse GenericResponse(String msg) {
		return (new GenericResponse(msg));
	}
	
	/**
	 * Find a product 
	 * 
	 * @param id
	 *     Vend id passed in
	 * @return
	 *     Response of item vend
	 */
	public VendResponse VendItem(int id) {
		if (getStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		if (!getStatus().equals(STATUS.VENDING_IN_PROGRESS)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please deposit money");
		}

		VendResponse v = this.vendingMachineServiceImpl.FindItem(id);		
		this.vendingMachineServiceImpl.VendingMachineDeposit().resetDepositValue(0);
		setStatus(STATUS.READY_TO_VEND);
		return v;	
	}

	/**
	 * Give a refund
	 *  
	 * @return
	 *     Response of a refund
	 */
	public VendResponse IssueRefund() {
		if (getStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		VendResponse v = this.vendingMachineServiceImpl.IssueRefund();
		this.vendingMachineServiceImpl.VendingMachineDeposit().resetDepositValue(0);
		setStatus(STATUS.READY_TO_VEND);
		return (v);
	}
	
	/**
	 * Get the float / deposit value
	 * 
	 * @return
	 *     Response for float
	 */
	public MoneyResponse GetFloat() {
		if (getStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}		
		int floatValue = this.vendingMachineServiceImpl.VendingMachineFloat().getFloatValue();
		int depositValue = this.vendingMachineServiceImpl.VendingMachineDeposit().getDepositValue();		
		return (new MoneyResponse(floatValue, depositValue));
	}

	/**
	 * Get a list of coins from the coin bucket by using the string name
	 * 
	 * @return
	 *     Return a list of coins in the coin bucket
	 */	
	public Map<String, Integer> GetCoinBucketByString() {
		if (getStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		return (this.vendingMachineServiceImpl.VendingMachineCoinBucket().getFloatCoinBucketByString());
	}	

	/**
	 * Get the current vending status
	 * 
	 * @return
	 *     Current status of the vending machine
	 */
	public VendingMachineStatusResponse GetVendingStatus() {
		return (new VendingMachineStatusResponse(getStatus()));
	}
	
}
