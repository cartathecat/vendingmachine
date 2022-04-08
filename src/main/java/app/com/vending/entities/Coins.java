package app.com.vending.entities;

/**
 * Coins
 * 
 * March 2022
 * 
 * Hold the list of valid coins
 *  
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public abstract class Coins {

	private final static Logger log = LoggerFactory.getLogger(Coins.class);
	
	private CoinConfig coinConfig;
	@Autowired
	public void SetCoinConfig(CoinConfig c) {
		this.coinConfig = c;
	}
		
	public Coins() {
		log.debug("Coins");		
	}

	public List<Coin> getNewCoins() {
		List<Coin> newCoins = coinConfig.getCoins();
		for (Coin  c : newCoins) {
			log.debug("Coin name  :" + c.getCoinName());
			log.debug("Coin value :" + c.getCoinValue());
		}
		return coinConfig.getCoins();
	}
	
	public Coin GetCoin(String s) {
		Coin c = coinConfig.getCoin(s);
		return c;
	}
	
	public boolean CoinValid(String s) {
		Coin c = coinConfig.getCoin(s);
		if (c != null) {
			return true;
		} else {
			return false;
		}
	}

}
