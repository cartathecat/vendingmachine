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
import org.springframework.context.annotation.ComponentScan;
import app.com.vending.entities.Coins;

@ComponentScan
public class VendingMachineDeposit extends Coins {

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
		return "VendingMachineDeposit [depositValue=" + GetDepositValue() + "]";
	}
	
	
}
