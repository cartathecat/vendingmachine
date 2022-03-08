package app.com.vending.machine.service;

/**
 * VendingMachineDeposit
 * 
 *  March 2022
 *  
* Manage the vending machines deposited coins 
  *  
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Vending MAchine Deposit
 * 
 * Manage coin deposits
 * 
 */
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class VendingMachineDeposit {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineDeposit.class);
	
	private int depositValue;

	public VendingMachineDeposit() {
		log.debug("VendingMachineDeposit");
	}

	public VendingMachineDeposit(int value) {
		this.depositValue = value;
	}

	// Deposit value
	public int GetDepositValue() {
		return this.depositValue;
	}
	
	public void SetDepositValue(int value) {
		this.depositValue += value;
	}

	public void ResetDepositValue(int value) {
		this.depositValue = value;
	}

	@Override
	public String toString() {
		return "VendingMachineDeposit [depositValue=" + depositValue + "]";
	}
	
	
}
