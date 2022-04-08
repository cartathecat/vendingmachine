package app.com.vending.entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class Coin {
	
	private String coinName;
	private int coinValue;
	
	public Coin(String name, int value) {
		this.coinName = name;
		this.coinValue = value;
	}
	
	public String getCoinName() {
		return coinName;
	}
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}
	public int getCoinValue() {
		return coinValue;
	}
	public void setCoinValue(int coinValue) {
		this.coinValue = coinValue;
	}

	@Override
	public String toString() {
		return String.format("Coin [coinName=%s, coinValue=%s]", coinName, coinValue);
	}
	
}
