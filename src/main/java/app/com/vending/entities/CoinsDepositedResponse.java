package app.com.vending.entities;

/**
 * CoinDepositedResponse
 * 
 * March 2022
 * 
 * Hold much money has been deposited each time the '/deposit' endpoint is invoked
 *  
 */

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class CoinsDepositedResponse {

	private int deposit;
	private String message;
	
	public CoinsDepositedResponse(int deposit, String msg) {
		this.deposit = deposit;
		this.message = msg;
	}
	
	public int getDeposit() {
		return deposit;
	}
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "Deposit [deposit=" + deposit + ", message=" + message + "]";
	}
	
}
