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
import org.springframework.stereotype.Component;

import app.com.vending.entities.Coins;

@ComponentScan
public class VendingMachineFloat extends Coins {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineFloat.class);
	
	private int floatValue;

	public VendingMachineFloat() {
		log.debug("VendingMachineFloat");
	}

	public VendingMachineFloat(int value) {
		this.floatValue = value;
	}


	// Float value
	public int getFloatValue() {
		return this.floatValue;
	}
	public void setFloatValue(int value) {
		this.floatValue += value;
	}
	public void reduceFloatValue(int value) {
		this.floatValue -= value;
	}
	

	@Override
	public String toString() {
		return String.format("VendingMachineFloat [floatValue=%s]", floatValue);
	}
	
	
}
