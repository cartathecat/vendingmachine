package app.com.vending.machine.service;

/**
 * Vending Machine Change
 * 
 * Manage change
 * 
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import app.com.vending.entities.Coins;

@ComponentScan
public class VendingMachineChange extends Coins {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineChange.class);

	private int changeValue;

	public VendingMachineChange() {
		log.debug("VendingMachineChange");
	}

	public VendingMachineChange(int value) {
		this.changeValue = value;
	}

	public int GetChangeValue() {
		return this.changeValue;
	}
	public void SetChangeValue(int value) {
		this.changeValue = value;
	}

	@Override
	public String toString() {
		return "VendingMachineChange [changeValue=" + GetChangeValue() + "]";
	}
	
	
}
