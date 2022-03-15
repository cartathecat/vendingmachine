package app.com.vending.entities;

/**
 * MoneyResponse
 * 
 * March 2022
 * 
 * Response for the Vending Machine money amounts - both floatValue and deposited money
 *  
 */

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class MoneyResponse {

	private int floatValue;
	private int depositValue;
	
	
	public MoneyResponse(int floatValue, int depositValue) {
		super();
		this.floatValue = floatValue;
		this.depositValue = depositValue;
	}
	
	public int getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(int floatValue) {
		this.floatValue = floatValue;
	}
	public int getDepositValue() {
		return depositValue;
	}
	public void setDepositValue(int depositValue) {
		this.depositValue = depositValue;
	}
	
	@Override
	public String toString() {
		return String.format("MoneyResponse [floatValue=%s, depositValue=%s]", floatValue, depositValue);
	}
	
}
