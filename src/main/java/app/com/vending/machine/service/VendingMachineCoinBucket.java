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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import app.com.vending.entities.ChangeReturnResponse;
import app.com.vending.entities.Coins;
//import app.com.vending.entities.Coins.COINVALUE;

@ComponentScan
public class VendingMachineCoinBucket extends Coins {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineCoinBucket.class);

	// TreeMap to ensure natural order
	private Map<Coins.COINVALUE, Integer> floatCoinBucket = new TreeMap<Coins.COINVALUE, Integer>();

	public VendingMachineCoinBucket() {
		log.debug("VendingMachineCoinBucket");
	}

	/**
	 * Check to see if we have the correct coins to return
	 * 
	 * *** Can be refactored ***
	 */
	public List<ChangeReturnResponse> CheckCoinsForChange(int money) {
		
		List<ChangeReturnResponse> crList = new ArrayList<ChangeReturnResponse>();

		int change = money;
		int coinsNeeded = 0;
		int numberOfCoins = 0;

		if (change > 0) {			
			coinsNeeded = (change / Coins.COINVALUE.TWOPOUND.getValue());
			numberOfCoins = GetCoinValue(Coins.COINVALUE.TWOPOUND);
			log.debug("TwoPound       :{}",coinsNeeded);
			log.debug("NumberOfCoins  :{}",numberOfCoins);
			log.debug("Change         :{}",change);
			
			if (coinsNeeded > 0 && numberOfCoins > 0) {
				if (coinsNeeded <= numberOfCoins) {				
					int changeDiff = change - (coinsNeeded * Coins.COINVALUE.TWOPOUND.getValue());
					if (changeDiff == 0) {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TWOPOUND, coinsNeeded);
						crList.add(cr);
						log.debug("Found £2 pence coin");
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TWOPOUND, coinsNeeded);
						crList.add(cr);
						log.debug("Found {} £2 coin", coinsNeeded);

					}
					change = change - (coinsNeeded * Coins.COINVALUE.TWOPOUND.getValue());
				}
			}
		}
		
		if (change > 0) {			
			coinsNeeded = (change / Coins.COINVALUE.ONEPOUND.getValue());
			numberOfCoins = GetCoinValue(Coins.COINVALUE.ONEPOUND);
			log.debug("Pound          :{}",coinsNeeded);
			log.debug("NumberOfCoins  :{}",numberOfCoins);
			log.debug("Change         :{}",change);
			
			if (coinsNeeded > 0 && numberOfCoins > 0) {
				if (coinsNeeded <= numberOfCoins) {				
					int changeDiff = change - (coinsNeeded * Coins.COINVALUE.ONEPOUND.getValue());
					if (changeDiff == 0) {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.ONEPOUND, coinsNeeded);
						crList.add(cr);
						log.debug("Found £1 coin");
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.ONEPOUND, coinsNeeded);
						crList.add(cr);
						log.debug("Found {} £1 coin", coinsNeeded);

					}
					change = change - (coinsNeeded * Coins.COINVALUE.ONEPOUND.getValue());
				} else {
					ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.ONEPOUND, numberOfCoins);
					crList.add(cr);
					log.debug("Found {} £1.00 coins", numberOfCoins);
					change = change - (numberOfCoins * Coins.COINVALUE.ONEPOUND.getValue());

				}
			}
		}
		
		if (change > 0) {			
			coinsNeeded = (change / Coins.COINVALUE.FIFTY.getValue());
			numberOfCoins = GetCoinValue(Coins.COINVALUE.FIFTY);
			log.debug("FiftyPence     :{}",coinsNeeded);
			log.debug("NumberOfCoins  :{}",numberOfCoins);
			log.debug("Change         :{}",change);
			
			if (coinsNeeded > 0 && numberOfCoins > 0) {
				if (coinsNeeded <= numberOfCoins) {				
					int changeDiff = change - (coinsNeeded * Coins.COINVALUE.FIFTY.getValue());
					if (changeDiff == 0) {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.FIFTY, coinsNeeded);
						crList.add(cr);
						log.debug("Found £0.50 coin");
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.FIFTY, coinsNeeded);
						crList.add(cr);
						log.debug("Found {} £0.50 coin", coinsNeeded);

					}
					change = change - (coinsNeeded * Coins.COINVALUE.FIFTY.getValue());
				} else {
					ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.FIFTY, numberOfCoins);
					crList.add(cr);
					log.debug("Found {} £0.50 coins", numberOfCoins);
					change = change - (numberOfCoins * Coins.COINVALUE.FIFTY.getValue());

				}
			}
		}
		
		if (change > 0) {
			coinsNeeded = (change / Coins.COINVALUE.TWENTY.getValue());
			numberOfCoins = GetCoinValue(Coins.COINVALUE.TWENTY);
			log.debug("TwentyPence    :{}",coinsNeeded);
			log.debug("NumberOfCoins  :{}",numberOfCoins);
			log.debug("Change         :{}",change);

			if (coinsNeeded > 0 && numberOfCoins > 0) {
				if (coinsNeeded <= numberOfCoins) {				
					int changeDiff = change - (coinsNeeded * Coins.COINVALUE.TWENTY.getValue());
					if (changeDiff == 0) {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TWENTY, coinsNeeded);
						crList.add(cr);
						log.debug("Found £0.20 pence coins");
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TWENTY, coinsNeeded);
						crList.add(cr);
						log.debug("Found {} £0.20 coins", coinsNeeded);

					}
					change = change - (coinsNeeded * Coins.COINVALUE.TWENTY.getValue());
					
				} else {
					ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TWENTY, numberOfCoins);
					crList.add(cr);
					log.debug("Found {} £0.20 coins", numberOfCoins);
					change = change - (numberOfCoins * Coins.COINVALUE.TWENTY.getValue());
				}
			}
		}

		if (change > 0) {
			coinsNeeded = (change / Coins.COINVALUE.TEN.getValue());
			numberOfCoins = GetCoinValue(Coins.COINVALUE.TEN);
			log.debug("TenPence       :{}",coinsNeeded);
			log.debug("NumberOfCoins  :{}",numberOfCoins);
			log.debug("Change         :{}",change);

			if (coinsNeeded > 0 && numberOfCoins > 0) {
				if (coinsNeeded <= numberOfCoins) {								
					int changeDiff = change - (coinsNeeded * Coins.COINVALUE.TEN.getValue());
					if (changeDiff == 0) {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TEN, coinsNeeded);
						crList.add(cr);
						log.debug("Found £0.10 coins");
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TEN, coinsNeeded);
						crList.add(cr);
						log.debug("Found {} £0.10 coins", coinsNeeded);
	
					}
					change = change - (coinsNeeded * Coins.COINVALUE.TEN.getValue());
					
				} else {
					ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TEN, numberOfCoins);
					crList.add(cr);
					log.debug("Found {} £0.10 coins", numberOfCoins);
					change = change - (numberOfCoins * Coins.COINVALUE.TEN.getValue());	
				}
			}
		}

		if (change > 0) {
			coinsNeeded = (change / Coins.COINVALUE.FIVE.getValue());
			numberOfCoins = GetCoinValue(Coins.COINVALUE.FIVE);
			log.debug("FivePence      :{}",coinsNeeded);
			log.debug("NumberOfCoins  :{}",numberOfCoins);
			log.debug("Change         :{}",change);

			if (coinsNeeded > 0 && numberOfCoins > 0) {
				if (coinsNeeded <= numberOfCoins) {				
					int changeDiff = change - (coinsNeeded * Coins.COINVALUE.FIVE.getValue());
					if (changeDiff == 0) {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.FIVE, coinsNeeded);
						crList.add(cr);
						log.debug("Found £0.05 coins");
	
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.FIVE, coinsNeeded);
						crList.add(cr);
						log.debug("Found {} £0.05 coins", numberOfCoins);
	
					}
					change = change - (coinsNeeded *Coins.COINVALUE.FIVE.getValue());
				} else {
					ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.FIVE, numberOfCoins);
					crList.add(cr);
					log.debug("Found {} £0.05 coins", numberOfCoins);
					change = change - (numberOfCoins * Coins.COINVALUE.FIVE.getValue());
				}
			}
		}

		if (change > 0) {
			coinsNeeded = (change / Coins.COINVALUE.TWO.getValue());
			numberOfCoins = GetCoinValue(Coins.COINVALUE.TWO);
			log.debug("TwoPen         :{}",coinsNeeded);
			log.debug("NumberOfCoins  :{}",numberOfCoins);
			log.debug("Change         :{}",change);

			if (coinsNeeded > 0 && numberOfCoins > 0) {
				if (coinsNeeded <= numberOfCoins) {				
					int changeDiff = change - (coinsNeeded * Coins.COINVALUE.TWO.getValue());
					if (changeDiff == 0) {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TWO, coinsNeeded);
						crList.add(cr);
						log.debug("Found £0.02 coins");
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TWO, coinsNeeded);
						crList.add(cr);
						log.debug("Found {} £0.02 coins", numberOfCoins);
					}
					change = change - (coinsNeeded * Coins.COINVALUE.TWO.getValue());
				} else {
					ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.TWO, numberOfCoins);
					crList.add(cr);
					log.debug("Found {} £0.02 coins", numberOfCoins);
					change = change - (numberOfCoins * Coins.COINVALUE.TWO.getValue());
				}
			}
		}

		if (change > 0) {
			coinsNeeded = (change / Coins.COINVALUE.ONE.getValue());
			numberOfCoins = GetCoinValue(Coins.COINVALUE.ONE);
			log.debug("One            :{}",numberOfCoins);
			log.debug("NumberOfCoins  :{}",numberOfCoins);
			log.debug("Change         :{}",change);

			if (coinsNeeded > 0 && numberOfCoins > 0) {
				if (coinsNeeded <= numberOfCoins) {				
					int changeDiff = change - (coinsNeeded * Coins.COINVALUE.ONE.getValue());
					if (changeDiff == 0) {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.ONE, coinsNeeded);
						crList.add(cr);
						log.debug("Found £0.01 coins");
					} else {
						ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.ONE, coinsNeeded);
						crList.add(cr);
						log.debug("Found {} £0.01 coins", numberOfCoins);
					}
					change = change - (coinsNeeded * Coins.COINVALUE.ONE.getValue());				
				} else {
					ChangeReturnResponse cr = new ChangeReturnResponse(Coins.COINVALUE.ONE, numberOfCoins);
					crList.add(cr);
					log.debug("Found {} £0.01 coins", numberOfCoins);
					change = change - (numberOfCoins * Coins.COINVALUE.ONE.getValue());
				}
			}
		}
		if (change > 0) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Not enough coins to return change for product "));
		}
		
		return crList;
		
	}
	
	/**
	 * Return coin value of ENUM
	 * 
	 * @param coin
	 * @return
	 */
	public int GetCoinValue(COINVALUE coin) {		
		int ret = 0;
		try {
			switch (coin) {
			case TWOPOUND:
				ret = this.floatCoinBucket.get(Coins.COINVALUE.TWOPOUND);
				break;
			case ONEPOUND:
				ret = this.floatCoinBucket.get(Coins.COINVALUE.ONEPOUND);
				break;
			case FIFTY:
				ret = this.floatCoinBucket.get(Coins.COINVALUE.FIFTY);
				break;
			case TWENTY:
				ret = this.floatCoinBucket.get(Coins.COINVALUE.TWENTY);
				break;
			case TEN:
				ret = this.floatCoinBucket.get(Coins.COINVALUE.TEN);
				break;
			case FIVE:
				ret = this.floatCoinBucket.get(Coins.COINVALUE.FIVE);
				break;
			case TWO:
				ret = this.floatCoinBucket.get(Coins.COINVALUE.TWO);
				break;
			case ONE:
				ret = this.floatCoinBucket.get(Coins.COINVALUE.ONE);
				break;
				
			default:
				break;
			}
		} catch (Exception e) {
			// do nothing ...
		}
		return ret;
	}

	
	/**
	 * Add coins to the coin bucket
	 */
	public void AddToFloatCoinBucket(String coin, int qty) {	
		int q = 0;
		if (!this.floatCoinBucket.containsKey(Coins.COINVALUE.valueOf(coin.toUpperCase()))) {
			this.floatCoinBucket.put(Coins.COINVALUE.valueOf(coin.toUpperCase()), q+qty);					
		} else {
			q = this.floatCoinBucket.get(Coins.COINVALUE.valueOf(coin.toUpperCase()));
			this.floatCoinBucket.put(Coins.COINVALUE.valueOf(coin.toUpperCase()), q+qty);					

		}
	}
	
	/**
	 * Get the coins from the 'float coin bucket'
	 */
	public Map<Coins.COINVALUE, Integer> GetFloatCoinBucket() {	
		return this.floatCoinBucket;		
	}
	
	/**
	 * Remove cons from the coin bucket
	 * @param coin
	 * @param qty
	 */
	public void RemoveCoinFromCoinBucket(COINVALUE coin, int qty) {
		int q = 0;
		if (this.floatCoinBucket.containsKey(coin)) {
			q = this.floatCoinBucket.get(coin);
			this.floatCoinBucket.put(coin, q-qty);					
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Coin not found for :" + coin);
		}
		
	}
	
	@Override
	public String toString() {
		return "VendingMachineChange [coinBucket=" + GetFloatCoinBucket() + "]";
	}
	
	
}
