package app.com.vending.machine;

/**
 * VendingMachine
 * 
 * March 2022
 * 
 * Vending machine object
 *  
 */

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import app.com.vending.entities.Coins.COINVALUE;
import app.com.vending.entities.*;
import app.com.vending.machine.service.VendingMachineServiceImpl;

@ComponentScan
public class VendingMachine {

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
	
	public STATUS GetStatus() {
		return status;
	}
	public void SetStatus(STATUS status) {
		this.status = status;
	}
		
	public VendingMachine() {
		log.debug("VendingMachine");
		SetStatus(STATUS.INACTIVE);
	}
		
	/**
	 * Initialise the vending machine
	 * @param floatValue
	 */
	public void Initialise(List<String> coins) {
		ValidateCoins(coins);
		if (GetStatus().equals(STATUS.READY_TO_VEND)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine has already been initialized");
		}
		if (GetStatus().equals(STATUS.VENDING_IN_PROGRESS)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is in vending status");
		}
		this.vendingMachineServiceImpl.Initialise(CalculateFloat(coins));
		SetStatus(STATUS.READY_TO_VEND);
		log.debug("Products loaded ...");
		
	}
	
	/**
	 * Return a list of products
	 * @return
	 */
	public List<?> Products() {
		if (GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		return (this.vendingMachineServiceImpl.GetProducts());
	}

	/**
	 * Validate the coins 
	 * @param coins
	 */
	private void ValidateCoins(List<String> coins) {
		for (String c: coins) {
			String[] s = c.split(":");			
			boolean coinValid = Coins.COINVALUE.coinValid(s[0].toUpperCase());
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
	 * @param coins
	 * @return
	 */
	public CoinsDepositedResponse DepositAmount(List<String> coins) {				
		ValidateCoins(coins);
		if (GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		try {
			for (String c: coins) {
				String[] s = c.split(":");
				int qty = Integer.parseInt(s[1]);
				this.vendingMachineServiceImpl.VendingMachineDeposit().SetDepositValue(qty * Coins.COINVALUE.coinValue(s[0].toUpperCase()));
				vendingMachineServiceImpl.VendingMachineCoinBucket().AddToFloatCoinBucket(s[0].toUpperCase(), qty);
				log.debug("Deposit Coin   : {}", s[0]);
				log.debug("Coint value : {}", Coins.COINVALUE.coinValue(s[0].toUpperCase()) );
				
			}		
			SetStatus(STATUS.VENDING_IN_PROGRESS);
			return (new CoinsDepositedResponse(this.vendingMachineServiceImpl.VendingMachineDeposit().GetDepositValue(), "Deposit"));
						
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Error parsing coins :%s",coins));
		}
	}

	/**
	 * Calculate the float
	 * @param coins
	 * @return
	 */
	public int CalculateFloat(List<String> coins) {
		int floatValue = 0;
		try {
			for (String c: coins) {
				String[] s = c.split(":");
				int qty = Integer.parseInt(s[1]);
				floatValue += (qty * Coins.COINVALUE.coinValue(s[0].toUpperCase()));			
				this.vendingMachineServiceImpl.VendingMachineCoinBucket().AddToFloatCoinBucket(s[0].toUpperCase(), qty);				
				log.debug("Coin   : {}", s[0]);
				log.debug("Number : {}", s[1]);
				log.debug("Coin {} has a value of {}", s[0], qty * Coins.COINVALUE.coinValue(s[0].toUpperCase()));
				
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
	 * @param coins
	 * @return
	 */
	public GenericResponse GenericResponse(String msg) {
		return (new GenericResponse(msg));
	}
	
	/**
	 * Find a product 
	 * @param id
	 * @return
	 */
	public VendResponse VendItem(int id) {
		if (GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		if (!GetStatus().equals(STATUS.VENDING_IN_PROGRESS)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please deposit money");
		}

		VendResponse v = this.vendingMachineServiceImpl.VendItem(id);		
		this.vendingMachineServiceImpl.VendingMachineDeposit().ResetDepositValue(0);
		SetStatus(STATUS.READY_TO_VEND);
		return v;	
	}

	/**
	 * Give a refund 
	 * @return
	 */
	public VendResponse IssueRefund() {
		if (GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		VendResponse v = this.vendingMachineServiceImpl.IssueRefund();
		this.vendingMachineServiceImpl.VendingMachineDeposit().ResetDepositValue(0);
		SetStatus(STATUS.READY_TO_VEND);
		return (v);
	}
	
	/**
	 * Get the float / deposit value
	 * @return
	 */
	public MoneyResponse GetFloat() {
		if (GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}		
		int floatValue = this.vendingMachineServiceImpl.VendingMachineFloat().GetFloatValue();
		int depositValue = this.vendingMachineServiceImpl.VendingMachineDeposit().GetDepositValue();		
		return (new MoneyResponse(floatValue, depositValue));
	}

	/**
	 * Get a list of coins from the coin bucket
	 * @return
	 */
	public Map<COINVALUE, Integer> GetCoinBucket() {
		if (GetStatus().equals(STATUS.INACTIVE)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Vending machine is not yet initialised");
		}
		return (this.vendingMachineServiceImpl.VendingMachineCoinBucket().GetFloatCoinBucket());
	}

	/**
	 * Get the current vending status
	 * @return
	 */
	public VendingMachineStatusResponse GetVendingStatus() {
		return (new VendingMachineStatusResponse(GetStatus()));
	}
	
}
