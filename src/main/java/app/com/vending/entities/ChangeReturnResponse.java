package app.com.vending.entities;

/**
 * ChangeReturnResponse
 * 
 * March 2022
 * 
 * Response for the number of each coins to return
 *  
 */

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class ChangeReturnResponse {

	private String coinName;
	private int coinQuantity;
	
	public ChangeReturnResponse(String coinName, int qty) {
		super();
		this.coinName = coinName;
		this.coinQuantity = qty;
	}

	public int getCoinQuantity() {
		return coinQuantity;
	}
	public void setCoinQuantity(int qty) {
		this.coinQuantity = qty;
	}

	public String getCoin() {
		return coinName;
	}
	public void setCoin(String coin) {
		this.coinName = coin;
	}

	@Override
	public String toString() {
		return String.format("ChangeReturnResponse [coinName=%s, coinQuantity=%s]", coinName, coinQuantity);
		
	}
	
}
