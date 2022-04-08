package app.com.vending.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="validcoins")
public class CoinConfig {
	
	private final static Logger log = LoggerFactory.getLogger(CoinConfig.class);
	
	private List<Coin> newCoins = new ArrayList<Coin>();

	public CoinConfig() {
		log.debug("CoinConfig");
	}
	
	public List<Coin> getCoins() {
		return newCoins;
	}
	public Coin getCoin(String key) {
		Coin c = newCoins.stream()
				.filter(coin -> key.equals(coin.getCoinName()))
				.findAny()
				.orElse(null);
		
		return c;
	}

}
