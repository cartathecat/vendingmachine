package app.com.vending.machine.service;

/**
 * Vending Machine Coin Bucket
 * 
 * March 2022
 * 
 * Hold a collection of coins
 * 
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import app.com.vending.entities.ChangeReturnResponse;
import app.com.vending.entities.Coin;
import app.com.vending.entities.Coins;

@ComponentScan
public class VendingMachineCoinBucket extends Coins {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineCoinBucket.class);

	// TreeMap to ensure natural order
	//private Map<Coins.COINVALUE, Integer> floatCoinBucket = new TreeMap<Coins.COINVALUE, Integer>();
	//private Map<String, Integer> floatCoinBucketByString = new LinkedHashMap<String, Integer>();
	private Map<String, Integer> floatCoinBucketByString = new LinkedHashMap<String, Integer>();

	/**
	 * Get the coins from the 'float coin bucket' by string coin name
	 * 
	 * @return
	 *     Return the coin bucket
	 */	
	public Map<String, Integer> getFloatCoinBucketByString() {	
		return this.floatCoinBucketByString;		
	}
	
	public VendingMachineCoinBucket() {
		log.debug("VendingMachineCoinBucket");
	}

	/**
	 * Check coins to return
	 * 
	 * @param money
	 *     The amount of money passed into check for coins to return in the list
	 * @return
	 *     List of coins
	 */
	public List<ChangeReturnResponse> CheckCoinsForChangeByString(int money) {
		
		List<Coin> coins = getNewCoins();
		
		List<ChangeReturnResponse> crList = new ArrayList<ChangeReturnResponse>();

		int change = money;
		int coinsNeeded = 0;
		int numberOfCoins = 0;

		for (Coin c: coins) {
			log.debug("Coin : " + c.getCoinName().toString());
			log.debug("Value: " + c.getCoinValue());

			if (change > 0) {			
				coinsNeeded = (change / c.getCoinValue());
				numberOfCoins = this.floatCoinBucketByString.get(c.getCoinName());
				log.debug("TwoPound       :{}",coinsNeeded);
				log.debug("NumberOfCoins  :{}",numberOfCoins);
				log.debug("Change         :{}",change);
				
				if (coinsNeeded > 0 && numberOfCoins > 0) {
					if (coinsNeeded <= numberOfCoins) {				
						ChangeReturnResponse cr = new ChangeReturnResponse(c.getCoinName(), coinsNeeded);
						crList.add(cr);
						log.debug("Found : " + c.getCoinName());
						change = change - (coinsNeeded * c.getCoinValue());
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(c.getCoinName(), numberOfCoins);
						crList.add(cr);
						log.debug("Found {} {} coins", numberOfCoins, c.getCoinName());
						change = change - (numberOfCoins * c.getCoinValue());
					}
				}
			}
			
		}
		
		log.debug("Change : {} ", change);
		if (change > 0) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Not enough coins to return change for product"));
		}
		
		return crList;		
		
	}
		
	/**
	 * Add coins to the coin bucket
	 * 
	 * @param coin
	 *     Coin value
	 * @param qty
	 *     Number of coins
	 */
	public void AddToFloatCoinBucketByString(String coin, int qty) {
		int q = 0;
		if (!this.floatCoinBucketByString.containsKey(GetCoin(coin.toUpperCase()).getCoinName())) {
			this.floatCoinBucketByString.put(coin.toUpperCase().trim(), q+qty);					
		} else {
			q = this.floatCoinBucketByString.get(GetCoin(coin.toUpperCase()).getCoinName());
			this.floatCoinBucketByString.put(coin.toUpperCase().trim(), q+qty);					
		}
		
	}
		
	/**
	 * Remove cons from the coin bucket
	 * 
	 * @param coin
	 *     Coin passed in to remove
	 * @param qty
	 *     Quanity of coins
	 */
	public void RemoveCoinFromCoinBucketByString(String coin, int qty) {
		int q = 0;
		if (this.floatCoinBucketByString.containsKey(coin)) {
			q = this.floatCoinBucketByString.get(GetCoin(coin.toUpperCase()).getCoinName());
			this.floatCoinBucketByString.put(coin, q-qty);					
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Coin not found for :" + coin);
		}
	}
	
	@Override
	public String toString() {
		return String.format("VendingMachineCoinBucket [floatCoinBucket=%s]", floatCoinBucketByString);
	}
	
	
}
