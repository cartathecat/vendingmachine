package app.com.vending.machine.service;

/**
 * Vending Machine Float
 * 
 * March 2022
 * 
 * Manage the vending machines float 
 * 
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class VendingMachineFloat {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineFloat.class);
	
	private int floatValue;

	public VendingMachineFloat() {
		log.info("VendingMachineFloat");
	}

	public VendingMachineFloat(int value) {
		this.floatValue = value;
	}


	// Float value
	public int getFloatValue() {
		return this.floatValue;
	}
	public void SetFloatValue(int value) {
		this.floatValue = this.floatValue + value;
	}
	public void reduceFloatValue(int value) {
		this.floatValue = this.floatValue - value;
	}
	

	@Override
	public String toString() {
		return "VendingMachineFloat [floatValue=" + floatValue + "]";
	}
	
	
}
