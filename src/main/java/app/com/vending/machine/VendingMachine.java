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
	
	@Autowired
	public VendingMachineServiceImpl vendingMachineServiceImpl;
	
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
	 * @param floatValue
	 */
	public void Initialise(int floatValue) {
		this.vendingMachineServiceImpl.Initialise(floatValue);
		setStatus(STATUS.READY_TO_VEND);
		log.debug("Products loaded ...");
	}
	
	/**
	 * Return a list of products
	 * @return
	 */
	public List<?> Products() {
		return (this.vendingMachineServiceImpl.GetProducts());
	}

	/**
	 * Validate the coins 
	 * @param coins
	 */
	public void ValidateCoins(List<String> coins) {
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
		try {
			for (String c: coins) {
				String[] s = c.split(":");
				int qty = Integer.parseInt(s[1]);
				this.vendingMachineServiceImpl.VendingMachineDeposit().setDepositValue(qty * Coins.COINVALUE.coinValue(s[0].toUpperCase()));
				vendingMachineServiceImpl.VendingMachineCoinBucket().AddToFloatCoinBucket(s[0].toUpperCase(), qty);
				log.debug("Deposit Coin   : {}", s[0]);
				log.debug("Coint value : {}", Coins.COINVALUE.coinValue(s[0].toUpperCase()) );
				
			}		
			setStatus(STATUS.VENDING_IN_PROGRESS);
			return (new CoinsDepositedResponse(this.vendingMachineServiceImpl.VendingMachineDeposit().getDepositValue(), "Deposit"));
						
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
		VendResponse v = this.vendingMachineServiceImpl.VendItem(id);		
		this.vendingMachineServiceImpl.VendingMachineDeposit().resetDepositValue(0);
		setStatus(STATUS.READY_TO_VEND);
		return v;
	
	}

	/**
	 * Give a refund 
	 * @return
	 */
	public VendResponse IssueRefund() {
		VendResponse v = this.vendingMachineServiceImpl.IssueRefund();
		this.vendingMachineServiceImpl.VendingMachineDeposit().resetDepositValue(0);
		setStatus(STATUS.READY_TO_VEND);
		return (v);
		
	}
	
	/**
	 * Get the float / deposit value
	 * @return
	 */
	public MoneyResponse GetFloat() {
		int floatValue = this.vendingMachineServiceImpl.VendingMachineFloat().getFloatValue();
		int depositValue = this.vendingMachineServiceImpl.VendingMachineDeposit().getDepositValue();		
		return (new MoneyResponse(floatValue, depositValue));
	}

	/**
	 * Get a list of coins from the coin bucket
	 * @return
	 */
	public Map<COINVALUE, Integer> GetCoinBucket() {
		return (this.vendingMachineServiceImpl.VendingMachineCoinBucket().GetFloatCoinBucket());
	}

	/**
	 * Get the current vending status
	 * @return
	 */
	public VendingMachineStatusResponse GetVendingStatus() {
		return (new VendingMachineStatusResponse(getStatus()));
	}
	
}
