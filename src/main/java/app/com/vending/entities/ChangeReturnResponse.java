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

import app.com.vending.entities.Coins.COINVALUE;

@EntityScan
public class ChangeReturnResponse {

	private COINVALUE coinName;
	private int coinQuantity;

	public ChangeReturnResponse(COINVALUE coinName, int qty) {
		super();
		this.coinName = coinName;
		this.coinQuantity = qty;
	}
	
	public COINVALUE getCoin() {
		return coinName;
	}
	public void setCoin(COINVALUE coin) {
		this.coinName = coin;
	}
	public int getCoinQuantity() {
		return coinQuantity;
	}
	public void setCoinQuantity(int qty) {
		this.coinQuantity = qty;
	}

	@Override
	public String toString() {
		return "ChangeReturn [coinName=" + coinName + ", coinQuantity=" + coinQuantity + "]";
	}
	
}
